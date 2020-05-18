package se1_prog_lab.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.commands.concrete.technical.Login;
import se1_prog_lab.client.commands.concrete.technical.Register;
import se1_prog_lab.client.interfaces.Client;
import se1_prog_lab.client.interfaces.CommandRepository;
import se1_prog_lab.client.interfaces.ServerIO;
import se1_prog_lab.util.AuthData;

import java.util.Scanner;

import static se1_prog_lab.util.AuthStrings.*;
import static se1_prog_lab.util.BetterStrings.yellow;
import static se1_prog_lab.util.ValidatingReader.readString;

/**
 * Класс клиентского приложения.
 */
@Singleton
public class ClientApp implements Client {
    private final Scanner consoleScanner;
    private final CommandRepository commandRepository;
    private final ServerIO serverIO;

    @Inject
    public ClientApp(Scanner consoleScanner, CommandRepository commandRepository, ServerIO serverIO) {
        this.consoleScanner = consoleScanner;
        this.commandRepository = commandRepository;
        this.serverIO = serverIO;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ClientModule());
        Client clientApp = injector.getInstance(Client.class);
        clientApp.start();
    }

    /**
     * Консоль.
     */
    @Override
    public void start() {
        System.out.println(yellow("Начало работы клиента"));

        serverIO.tryOpen();

        String serverResponse;
        while (true) {

            authorize();

            while (true) {
                System.out.print(">> ");
                String[] input = consoleScanner.nextLine().trim().split(" ");
                Command command = commandRepository.parseThenRun(input);

                if (command != null) {
                    serverResponse = serverIO.sendAndReceive(command);
                    System.out.println(serverResponse);
                    if (serverResponse.equals(INCORRECT_LOGIN_DATA.getMessage())
                            || serverResponse.equals(USERNAME_TAKEN.getMessage())
                            || serverResponse.equals(SERVER_ERROR.getMessage())
                    ) break;
                }
            }
        }
    }

    private void authorize() {
        System.out.println("Для работы с коллекцией зарегистрироваться/авторизоваться");

        String input;
        do {
            System.out.printf("Введите %s для регистрации или %s для авторизации \n",
                    yellow("register"), yellow("login"));
            input = consoleScanner.nextLine().trim();
        } while (!(input.equals("login") || input.equals("register")));

        String username, password, response;
        AuthCommand authCommand;
        String usernameMessage, passwordMessage;
        AuthData authData;
        do {

            if (input.equals("login")) {
                usernameMessage = "Введите ваше имя пользователя: ";
                passwordMessage = "Введите ваш пароль: ";
            } else {
                usernameMessage = "Придумайте имя пользователя: ";
                passwordMessage = "Придумайте пароль: ";
            }

            username = readString(consoleScanner, usernameMessage, false, 1);
            password = readString(consoleScanner, passwordMessage, false, 1);
            authData = new AuthData(username, password);

            if (input.equals("login")) authCommand = new Login(authData);
            else authCommand = new Register(authData);
            response = serverIO.authorize(authCommand);

        } while (!(response.equals(LOGIN_SUCCESSFUL.getMessage())
                || response.equals(REGISTRATION_SUCCESSFUL.getMessage())));

        System.out.println(response);
    }
}















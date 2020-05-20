package se1_prog_lab.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.ClientServerSideCommand;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.commands.concrete.technical.Login;
import se1_prog_lab.client.commands.concrete.technical.Register;
import se1_prog_lab.client.interfaces.Client;
import se1_prog_lab.client.interfaces.CommandRepository;
import se1_prog_lab.client.interfaces.ServerIO;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.api.ResponseType;
import se1_prog_lab.util.AuthData;
import se1_prog_lab.util.AuthStrings;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static se1_prog_lab.server.api.ResponseType.AUTH_STATUS;
import static se1_prog_lab.server.api.ResponseType.PLAIN_TEXT;
import static se1_prog_lab.util.AuthStrings.*;
import static se1_prog_lab.util.BetterStrings.*;
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

    public void handleResponse(Response response) {
        switch (response.getResponseType()) {
            case PLAIN_TEXT:
                System.out.println((String) response.getMessage());
                break;
            case AUTH_STATUS:
                AuthStrings authStatus = (AuthStrings) response.getMessage();
                System.out.println(authStatus.getMessage());
                break;
            case LABWORK_LIST:
                Collection<?> labWorks = (Collection<?>) response.getMessage();
                List<LabWork> parameterizedLabWorks = labWorks.stream().map((labWork) -> (LabWork) labWork).collect(Collectors.toList());
                String lines = multiline(parameterizedLabWorks.stream().map(LabWork::toString).toArray());
                System.out.println(lines);
        }
    }

    /**
     * Консоль.
     */
    @Override
    public void start() {
        System.out.println(yellow("Начало работы клиента"));

        serverIO.tryOpen();

        Response serverResponse;
        while (true) {

            authorize();

            while (true) {
                System.out.print(">> ");
                String[] input = consoleScanner.nextLine().trim().split(" ");
                Command command = commandRepository.parseThenRun(input);

                if (command instanceof ClientServerSideCommand) {
                    ClientServerSideCommand serverSideCommand = (ClientServerSideCommand) command;
                    serverResponse = serverIO.sendAndReceive(serverSideCommand);
                    handleResponse(serverResponse);
                    if (serverResponse.isRejected() && serverResponse.getResponseType() == AUTH_STATUS) {
                        AuthStrings authStatus = (AuthStrings) serverResponse.getMessage();
                        if (authStatus == INCORRECT_LOGIN_DATA ||
                            authStatus == USERNAME_TAKEN
                        ) break;
                    }
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

        String username, password;
        Response response;
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

            if (input.equals("login")) authCommand = new Login();
            else authCommand = new Register();
            response = serverIO.authorize(authCommand, authData);
            handleResponse(response);
        } while (response.isRejected());
    }
}















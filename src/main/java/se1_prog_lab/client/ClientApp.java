package se1_prog_lab.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.concrete.technical.Login;
import se1_prog_lab.client.commands.concrete.technical.Register;
import se1_prog_lab.client.gui.ClientView;
import se1_prog_lab.client.interfaces.ClientController;
import se1_prog_lab.client.interfaces.CommandRepository;
import se1_prog_lab.client.interfaces.ServerIO;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.util.AuthData;
import se1_prog_lab.util.AuthStrings;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static se1_prog_lab.util.BetterStrings.multiline;
import static se1_prog_lab.util.BetterStrings.yellow;
import static se1_prog_lab.util.ValidatingReader.readString;

/**
 * Класс клиентского приложения.
 * Controller.
 */
@Singleton
public class ClientApp implements ClientController {
    private final Scanner consoleScanner;
    private final CommandRepository commandRepository;
    private final ServerIO serverIO;
    private final ClientView view;

    @Inject
    public ClientApp(Scanner consoleScanner, CommandRepository commandRepository, ServerIO serverIO, ClientView view) {
        this.consoleScanner = consoleScanner;
        this.commandRepository = commandRepository;
        this.serverIO = serverIO;
        this.view = view;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ClientModule());

        ClientController controller = injector.getInstance(ClientController.class);
        controller.start();
    }


    /**
     * Консоль.
     */
    @Override
    public void start() {
        serverIO.tryOpen();
        SwingUtilities.invokeLater(view::initLoginWindow);


//        Response serverResponse;
//        while (true) {
//
//            authorize();
//
//            while (true) {
//                System.out.print(">> ");
//                String[] input = consoleScanner.nextLine().trim().split(" ");
//                Command command = commandRepository.parseThenRun(input);
//
//                if (command instanceof ClientServerSideCommand) {
//                    serverResponse = serverIO.sendAndReceive((ClientServerSideCommand) command);
//                    handleResponse(serverResponse);
//                    if (serverResponse.isRejected() && serverResponse.getResponseType() == AUTH_STATUS) {
//                        AuthStrings authStatus = (AuthStrings) serverResponse.getMessage();
//                        if (authStatus == INCORRECT_LOGIN_DATA || authStatus == USERNAME_TAKEN) break;
//                    }
//                }
//            }
//        }
    }

    @Override
    public void login(String username, String password) {
        Response response;
        AuthCommand authCommand;
        AuthData authData = new AuthData(username, password);
        authCommand = new Login();
        response = serverIO.authorize(authCommand, authData);
        handleResponse(response);
        handleAuthResponse(response);
    }

    @Override
    public void register(String username, String password) {
        Response response;
        AuthCommand authCommand;
        AuthData authData = new AuthData(username, password);
        authCommand = new Register();
        response = serverIO.authorize(authCommand, authData);
        handleResponse(response);
        handleAuthResponse(response);
    }

    private void handleAuthResponse(Response authResponse) {
        if (authResponse.isRejected()) {
            // todo это тупо, надо убирать эти геттеры и напрямую возвращать String
            AuthStrings authStatus = (AuthStrings) authResponse.getMessage();
            view.simpleAlert(authStatus.getMessage());
        } else {
            view.disposeLoginWindow();
            view.initMainWindow();
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
}














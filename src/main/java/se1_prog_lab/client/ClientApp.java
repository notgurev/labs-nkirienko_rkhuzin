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

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static se1_prog_lab.util.BetterStrings.multiline;

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
        AuthCommand authCommand;
        AuthData authData = new AuthData(username, password);
        authCommand = new Login();
        handleAuthResponse(serverIO.authorize(authCommand, authData));
    }

    @Override
    public void register(String username, String password) {
        AuthCommand authCommand;
        AuthData authData = new AuthData(username, password);
        authCommand = new Register();
        handleAuthResponse(serverIO.authorize(authCommand, authData));
    }

    private void handleAuthResponse(Response authResponse) {
        if (authResponse.isRejected()) {
            view.simpleAlert(authResponse.getStringMessage());
        } else {
            view.disposeLoginWindow();
            view.initMainWindow(serverIO.getUsername());
        }
    }

    // еще пригодится
    public void handleResponse(Response response) {
        switch (response.getResponseType()) {
            case PLAIN_TEXT:
            case AUTH_STATUS:
                System.out.println(response.getStringMessage());
                break;
            case LABWORK_LIST:
                Collection<?> labWorks = (Collection<?>) response.getMessage();
                List<LabWork> parameterizedLabWorks = labWorks.stream().map((labWork) -> (LabWork) labWork).collect(Collectors.toList());
                String lines = multiline(parameterizedLabWorks.stream().map(LabWork::toString).toArray());
                System.out.println(lines);
        }
    }
}














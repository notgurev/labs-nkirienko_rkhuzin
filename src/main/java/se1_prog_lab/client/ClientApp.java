package se1_prog_lab.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.client.commands.concrete.technical.Login;
import se1_prog_lab.client.commands.concrete.technical.Register;
import se1_prog_lab.client.gui.ClientModel;
import se1_prog_lab.client.gui.ClientView;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.util.AuthData;
import se1_prog_lab.util.AuthStrings;

import javax.annotation.Nonnull;
import javax.swing.*;

import static se1_prog_lab.server.api.ResponseType.AUTH_STATUS;
import static se1_prog_lab.server.api.ResponseType.LABWORK_LIST;
import static se1_prog_lab.util.AuthStrings.INCORRECT_LOGIN_DATA;
import static se1_prog_lab.util.AuthStrings.USERNAME_TAKEN;

/**
 * Класс клиентского приложения.
 * Controller.
 */
@Singleton
public class ClientApp implements ClientController {
    private final ServerIO serverIO;
    private final ClientView view;
    private final ClientModel model;

    @Inject
    public ClientApp(ServerIO serverIO, ClientView view, ClientModel model) {
        this.model = model;
        this.serverIO = serverIO;
        this.view = view;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ClientModule());
        ClientController controller = injector.getInstance(ClientController.class);
        controller.start();
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(view::initLoginWindow);
        serverIO.tryOpen();
    }

    @Override
    public void executeServerCommand(@Nonnull BasicCommand command) {
        Response serverResponse = serverIO.sendAndReceive(command);
        handleResponse(serverResponse);
        if (serverResponse.isRejected() && serverResponse.getResponseType() == AUTH_STATUS) {
            AuthStrings authStatus = (AuthStrings) serverResponse.getMessage();
            if (authStatus == INCORRECT_LOGIN_DATA || authStatus == USERNAME_TAKEN) getBackToLoginWindow();
        }
    }

    private void getBackToLoginWindow() {
        view.disposeMainWindow();
        view.initLoginWindow();
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

    @Override
    public void openConstructingFrame() {
        view.initConstructingFrame();
    }

    private void handleAuthResponse(Response authResponse) {
        if (authResponse.isRejected()) {
            view.simpleAlert(authResponse.getStringMessage());
        } else {
            view.disposeLoginWindow();
            SwingUtilities.invokeLater(() -> view.initMainWindow(serverIO.getUsername()));
        }
    }

    public void handleResponse(Response response) {
        if (response.getResponseType() == LABWORK_LIST) {
            model.setBufferedCollectionPage(response.getCollection());
        } else {
            view.simpleAlert(response.getStringMessage());
        }
    }
}














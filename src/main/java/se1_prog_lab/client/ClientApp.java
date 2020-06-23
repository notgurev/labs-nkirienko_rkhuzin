package se1_prog_lab.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.client.commands.NoJournalEntryCommand;
import se1_prog_lab.client.commands.concrete.technical.GetCollectionPage;
import se1_prog_lab.client.commands.concrete.technical.Login;
import se1_prog_lab.client.commands.concrete.technical.Register;
import se1_prog_lab.client.gui.ClientView;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.AuthStrings;
import se1_prog_lab.shared.api.Response;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.Collection;
import java.util.LinkedList;

import static se1_prog_lab.shared.api.AuthStrings.INCORRECT_LOGIN_DATA;
import static se1_prog_lab.shared.api.AuthStrings.USERNAME_TAKEN;
import static se1_prog_lab.shared.api.ResponseType.AUTH_STATUS;
import static se1_prog_lab.shared.api.ResponseType.LABWORK_LIST;

/**
 * Класс клиентского приложения.
 * Controller.
 */
@Singleton
public class ClientApp implements ClientCore {
    private final ServerIO serverIO;
    private final ClientView view;
    private final static int JOURNAL_SIZE_LIMIT = 13;
    private final LinkedList<String> journal = new LinkedList<>(); // Журнал (история) команд
    private Collection<LabWork> bufferedCollectionPage;
    private int selectedPage;
    private int pageSize = 30;

    @Inject
    public ClientApp(ServerIO serverIO, ClientView view) {
        this.serverIO = serverIO;
        this.view = view;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ClientModule());
        ClientCore controller = injector.getInstance(ClientCore.class);
        controller.start();
    }

    @Override
    public void updateCollectionPage() {
        executeServerCommand(new GetCollectionPage(selectedPage, pageSize));
    }

    @Override
    public Object[][] getCollectionData() {
        return bufferedCollectionPage.stream().map(LabWork::toArray).toArray(Object[][]::new);
    }

    @Override
    public void simpleAlert(String alertText) {
        view.simpleAlert(alertText);
    }

    @Override
    public void start() {
        SwingUtilities.invokeLater(view::initLoginWindow);
        serverIO.tryOpen();
    }

    private void addJournalEntry(String entry) {
        if (entry != null) journal.addFirst(entry);
        while (journal.size() > JOURNAL_SIZE_LIMIT) {
            journal.removeLast();
        }
    }

    @Override
    public void executeServerCommand(@Nonnull BasicCommand command) {
        Response serverResponse = serverIO.sendAndReceive(command);
        handleResponse(serverResponse);
        if (serverResponse.isRejected() && serverResponse.getResponseType() == AUTH_STATUS) {
            AuthStrings authStatus = (AuthStrings) serverResponse.getMessage();
            if (authStatus == INCORRECT_LOGIN_DATA || authStatus == USERNAME_TAKEN) getBackToLoginWindow();
        } else {
            if (!(command instanceof NoJournalEntryCommand)) addJournalEntry(command.getJournalEntry());
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

    @Override
    public void openJournalFrame() {
        view.initJournalFrame(journal);
    }

    private void handleAuthResponse(Response authResponse) {
        if (authResponse.isRejected()) {
            view.simpleAlert(authResponse.getStringMessage());
        } else {
            view.disposeLoginWindow();
            updateCollectionPage();
            view.initMainWindow(serverIO.getUsername());
        }
    }

    public void handleResponse(Response response) {
        if (response.getResponseType() == LABWORK_LIST) {
            bufferedCollectionPage = response.getCollection();
        } else {
            view.simpleAlert(response.getStringMessage());
        }
        if (view.isMainFrameInitialized()) view.update();
    }

    @Override
    public int getSelectedPage() {
        return selectedPage;
    }

    @Override
    public void setSelectedPage(int selectedPage) {
        this.selectedPage = selectedPage;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
//
//    @Override
//    public void startRegularUpdates() {
//        new Thread(() -> {
//            try {
//                wait(60 * 1000);
//            } catch (InterruptedException e) {
//                startRegularUpdates();
//            }
//            updateCollectionPage();
//        }).start();
//    }
}














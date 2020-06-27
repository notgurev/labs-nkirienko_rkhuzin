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
import se1_prog_lab.client.gui.CollectionChangeSubscriber;
import se1_prog_lab.client.gui.LangChangeSubscriber;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.AuthStatus;
import se1_prog_lab.shared.api.Response;
import se1_prog_lab.shared.util.ColorUtils;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

import static se1_prog_lab.shared.api.AuthStatus.INCORRECT_LOGIN_DATA;
import static se1_prog_lab.shared.api.AuthStatus.USERNAME_TAKEN;
import static se1_prog_lab.shared.api.ResponseType.AUTH_STATUS;
import static se1_prog_lab.shared.api.ResponseType.LABWORK_LIST;

/**
 * Класс клиентского приложения.
 * Controller.
 */
@Singleton
public class ClientApp implements ClientCore {
    private static final int UPDATE_TIMER = 60 * 1000; // 60 секунд
    private static final int JOURNAL_SIZE_LIMIT = 13;
    private static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("ru-RU");
    private final ServerIO serverIO;
    private final ClientView view;
    private final LinkedList<String> journal = new LinkedList<>(); // Журнал (история) команд
    private final HashMap<String, Color> ownersColors = new HashMap<>();
    private final List<CollectionChangeSubscriber> collectionChangeSubscribers = new ArrayList<>();
    private final List<LangChangeSubscriber> langChangeSubscribers = new ArrayList<>();
    private Vector<LabWork> bufferedCollectionPage = new Vector<>();
    private int selectedPage;
    private int pageSize = 10;
    private Locale locale = DEFAULT_LOCALE;

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
    public void addLanguageSubscriber(LangChangeSubscriber subscriber) {
        langChangeSubscribers.add(subscriber);
    }

    @Override
    public void removeLanguageSubscriber(LangChangeSubscriber subscriber) {
        langChangeSubscribers.remove(subscriber);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
        langChangeSubscribers.forEach(subscriber -> subscriber.changeLang(locale));
    }

    @Override
    public void updateCollectionPage() {
        submitServerCommand(new GetCollectionPage(selectedPage, pageSize));
    }

    @Override
    public Vector<LabWork> getBufferedCollectionPage() {
        return bufferedCollectionPage;
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
        addLanguageSubscriber(serverIO);
        serverIO.changeLang(locale);
        serverIO.tryOpen();
    }

    private void addJournalEntry(String entry) {
        if (entry != null) journal.addFirst(entry);
        while (journal.size() > JOURNAL_SIZE_LIMIT) {
            journal.removeLast();
        }
    }

    @Override
    public Response submitServerCommand(@Nonnull BasicCommand command) {
        Response serverResponse = serverIO.sendAndReceive(command, locale);
        handleResponse(serverResponse);
        if (serverResponse.isRejected() && serverResponse.getResponseType() == AUTH_STATUS) {
            AuthStatus authStatus = serverResponse.getAuthStatus();
            if (authStatus == INCORRECT_LOGIN_DATA || authStatus == USERNAME_TAKEN) getBackToLoginWindow();
        } else {
            if (!(command instanceof NoJournalEntryCommand)) addJournalEntry(command.getJournalEntry());
        }
        if (command.isCollectionChanging() && !serverResponse.isRejected()) {
            updateCollectionPage();
        }
        return serverResponse;
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
        handleAuthResponse(serverIO.authorize(authCommand, authData, locale));
    }

    @Override
    public void register(String username, String password) {
        AuthCommand authCommand;
        AuthData authData = new AuthData(username, password);
        authCommand = new Register();
        handleAuthResponse(serverIO.authorize(authCommand, authData, locale));
    }

    @Override
    public void openConstructingFrame() {
        view.initConstructingFrame();
    }

    @Override
    public void openConstructingFrame(int index) {
        view.initConstructingFrame(bufferedCollectionPage.get(index));
    }

    @Override
    public void openJournalFrame(Locale locale) {
        view.initJournalFrame(journal, locale);
    }

    private void handleAuthResponse(Response authResponse) {
        if (authResponse.isRejected()) {
            view.simpleAlert(authResponse.getMessage());
        } else {
            view.disposeLoginWindow();
            updateCollectionPage();
            view.initMainWindow(serverIO.getUsername());
            startRegularUpdates();
        }
    }

    private void handleResponse(Response response) {
        if (response.getResponseType() == LABWORK_LIST) {
            Collection<LabWork> collection = response.getCollection();
            if (collection == null || collection.isEmpty()) {
                bufferedCollectionPage = new Vector<>();
            } else {
                bufferedCollectionPage = (Vector<LabWork>) response.getCollection();
            }
            collectionChangeSubscribers.forEach(CollectionChangeSubscriber::updateWithNewData);
        } else {
            view.simpleAlert(response.getMessage());
        }
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

    @Override
    public Color getColorByOwner(String owner) {
        if (ownersColors.containsKey(owner)) {
            return ownersColors.get(owner);
        } else {
            Color randomColor = ColorUtils.generateRandomColor(0.5f);
            ownersColors.put(owner, randomColor);
            return randomColor;
        }
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void startRegularUpdates() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(UPDATE_TIMER);
                    System.out.println("Обновляем коллекцию в фоновом режиме");
                    updateCollectionPage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void addCollectionChangeSubscriber(CollectionChangeSubscriber collectionChangeSubscriber) {
        collectionChangeSubscribers.add(collectionChangeSubscriber);
    }
}














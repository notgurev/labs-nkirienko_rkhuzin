package se1_prog_lab.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.client.commands.NoJournalEntryCommand;
import se1_prog_lab.client.commands.concrete.Add;
import se1_prog_lab.client.commands.concrete.Clear;
import se1_prog_lab.client.commands.concrete.RemoveByID;
import se1_prog_lab.client.commands.concrete.Update;
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
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private static final int UPDATE_TIMER = 10 * 1000; // 10 секунд
    private final ServerIO serverIO;
    private final ClientView view;
    private final static int JOURNAL_SIZE_LIMIT = 13;
    private final LinkedList<String> journal = new LinkedList<>(); // Журнал (история) команд
    private Vector<LabWork> bufferedCollectionPage = new Vector<>();
    private int selectedPage;
    private int pageSize = 15;
    private List<ModelListener> listeners = new ArrayList<>();
    private boolean hasNextPage = true;
    private final HashMap<String, Color> ownersColors = new HashMap<>();

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

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean updateCollectionPage(int change) {
        if (getSelectedPage() != 0 || change >= 0) {
            Response response = executeServerCommand(new GetCollectionPage(selectedPage + change, pageSize));
            if (!response.isRejected() && response.getCollection().size() != 0) {
                setSelectedPage(getSelectedPage() + change);
                bufferedCollectionPage = (Vector<LabWork>) response.getCollection();
                if (view.isMainFrameInitialized()) {
                    view.update();
                }
                return true;
            }
        }
        return false;
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
        serverIO.tryOpen();
    }

    private void addJournalEntry(String entry) {
        if (entry != null) journal.addFirst(entry);
        while (journal.size() > JOURNAL_SIZE_LIMIT) {
            journal.removeLast();
        }
    }

    @Override
    public Response executeServerCommand(@Nonnull BasicCommand command) {
        Response serverResponse = serverIO.sendAndReceive(command);
        handleResponse(serverResponse);
        if (serverResponse.isRejected() && serverResponse.getResponseType() == AUTH_STATUS) {
            AuthStrings authStatus = (AuthStrings) serverResponse.getMessage();
            if (authStatus == INCORRECT_LOGIN_DATA || authStatus == USERNAME_TAKEN) getBackToLoginWindow();
        } else {
            if (!(command instanceof NoJournalEntryCommand)) addJournalEntry(command.getJournalEntry());
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
    public void openConstructingFrame(int index) {
        view.initConstructingFrame(bufferedCollectionPage.get(index));
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
            updateCollectionPage(0);
            view.initMainWindow(serverIO.getUsername());
            startRegularUpdates();
        }
    }

    public void handleResponse(Response response) {
        if (response.getResponseType() == LABWORK_LIST) {
            Collection<LabWork> collection = response.getCollection();
            hasNextPage = collection.size() >= pageSize;

            if (collection.size() != 0) {
                bufferedCollectionPage = (Vector<LabWork>) response.getCollection();
                if (bufferedCollectionPage.size() > pageSize)
                    bufferedCollectionPage.setSize(pageSize);
            }
        } else {
            view.simpleAlert(response.getStringMessage());
        }
    }

    public void addLabWork(LabWork labWork) {
        Response response = executeServerCommand(new Add(labWork));
        if (!response.isRejected()) {
            labWork.setId((Long) response.getPayload());
            if (bufferedCollectionPage.size() < pageSize) {
                bufferedCollectionPage.add(labWork);
                for (ModelListener listener : listeners) {
                    listener.addElement(labWork.toArray());
                }
            }
        }
    }

    public void clear() {
        Response response = executeServerCommand(new Clear());
        if (!response.isRejected()) {
            setSelectedPage(0);
            updateCollectionPage(0);
            if (view.isMainFrameInitialized()) {
                view.clear();
            }
        }
    }

    public void updateLabWork(Long id, LabWork labWork) {
        labWork.setId(id);
        AtomicBoolean isReplaced = new AtomicBoolean(false);
        if (!executeServerCommand(new Update(id, labWork)).isRejected()) {
            bufferedCollectionPage.replaceAll(l -> {
                if (l.getId().equals(id)) {
                    isReplaced.set(true);
                    return labWork;
                }
                return l;
            });
        }
        if (isReplaced.get()) initUpdateEvent(id, labWork);
    }

    public void removeLabWork(Long id) {
        if (!executeServerCommand(new RemoveByID(id)).isRejected()) {
            int size = bufferedCollectionPage.size();
            bufferedCollectionPage.removeIf(l -> {
                if (l.getId().equals(id)) {
                    return true;
                }
                return false;
            });
            if (size != bufferedCollectionPage.size()) initRemoveEvent(id);
        }
    }

    protected void initUpdateEvent(Long id, LabWork labWork) {
        for (ModelListener listener : listeners) {
            listener.updateElement(id, labWork.toArray());
        }
    }

    protected void initRemoveEvent(Long id) {
        for (ModelListener listener : listeners) {
            listener.removeElement(id);
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

    public boolean hasNextPage() {
        return hasNextPage;
    }

    @Override
    public Color getColorByOwner(String owner) {
        if (ownersColors.containsKey(owner)) {
            return ownersColors.get(owner);
        } else {
            Color randomColor = generateRandomColor();
            ownersColors.put(owner, randomColor);
            return randomColor;
        }
    }

    private static Color generateRandomColor() {
        Random r = new Random();
        return new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void startRegularUpdates() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(UPDATE_TIMER);
                    System.out.println("Обновляем коллекцию в фоновом режиме");
                    updateCollectionPage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}














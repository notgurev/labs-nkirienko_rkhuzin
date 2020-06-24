package se1_prog_lab.client;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.shared.api.Response;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Vector;

// todo поменять чтобы лишнего не было
public interface ClientCore {
    boolean updateCollectionPage(int change);

    Vector<LabWork> getBufferedCollectionPage();

    Object[][] getCollectionData();

    void addListener(ModelListener listener);

    void removeListener(ModelListener listener);

    void simpleAlert(String alertText);

    void start();

    Response executeServerCommand(@Nonnull BasicCommand command);

    void addLabWork(LabWork labWork);

    void updateLabWork(Long id, LabWork labWork);

    void removeLabWork(Long id);

    void clear();

    void login(String username, String password);

    void register(String username, String password);

    void openConstructingFrame();

    void openConstructingFrame(int index);

    void openJournalFrame();

    int getSelectedPage();

    void setSelectedPage(int selectedPage);

    int getPageSize();

    void setPageSize(int pageSize);

    boolean hasNextPage();

    Color getColorByOwner(String owner);

//    void startRegularUpdates();
}

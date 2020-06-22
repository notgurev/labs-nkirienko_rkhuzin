package se1_prog_lab.client;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.collection.LabWork;

import javax.annotation.Nonnull;
import java.util.Collection;

// todo поменять чтобы лишнего не было
public interface ClientCore {
    Collection<LabWork> updateCollectionPage();

    Object[][] getCollectionData();

    void simpleAlert(String alertText);

    void start();

    void executeServerCommand(@Nonnull BasicCommand command);

    void login(String username, String password);

    void register(String username, String password);

    void openConstructingFrame();

    void openJournalFrame();

    int getSelectedPage();

    void setSelectedPage(int selectedPage);

    int getPageSize();

    void setPageSize(int pageSize);

//    void startRegularUpdates();
}

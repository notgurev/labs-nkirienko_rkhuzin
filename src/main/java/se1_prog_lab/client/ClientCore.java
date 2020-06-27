package se1_prog_lab.client;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.client.gui.CollectionChangeSubscriber;
import se1_prog_lab.client.gui.LangChangeSubscriber;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.shared.api.Response;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Locale;
import java.util.Vector;

// todo поменять чтобы лишнего не было
public interface ClientCore {
    void updateCollectionPage();

    Vector<LabWork> getBufferedCollectionPage();

    Object[][] getCollectionData();

    void addLanguageSubscriber(LangChangeSubscriber subscriber);

    void removeLanguageSubscriber(LangChangeSubscriber subscriber);

    Locale getLocale();

    void setLocale(Locale locale);

    void simpleAlert(String alertText);

    void start();

    Response submitServerCommand(@Nonnull BasicCommand command);

    void login(String username, String password);

    void register(String username, String password);

    void openConstructingFrame();

    void openConstructingFrame(int index);

    void openJournalFrame(Locale locale);

    int getSelectedPage();

    void setSelectedPage(int selectedPage);

    int getPageSize();

    void setPageSize(int pageSize);

    Color getColorByOwner(String owner);

    void startRegularUpdates();

    void addCollectionChangeSubscriber(CollectionChangeSubscriber collectionChangeSubscriber);
}

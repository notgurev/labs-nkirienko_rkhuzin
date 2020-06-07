package se1_prog_lab.client.gui;

import java.util.LinkedList;

public interface ClientView {
    void initLoginWindow();

    void disposeLoginWindow();

    void initMainWindow(String username);

    void disposeMainWindow();

    void simpleAlert(String alertText);

    void initConstructingFrame();

    void initJournalFrame(LinkedList<String> journal);
}

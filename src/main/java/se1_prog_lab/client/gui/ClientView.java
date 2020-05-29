package se1_prog_lab.client.gui;

public interface ClientView {
    void initLoginWindow();

    void disposeLoginWindow();

    void initMainWindow();

    void disposeMainWindow();

    void simpleAlert(String alertText);
}

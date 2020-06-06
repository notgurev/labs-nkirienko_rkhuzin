package se1_prog_lab.client.gui;

public interface ClientView {
    void initLoginWindow();

    void disposeLoginWindow();

    void initMainWindow(String username);

    void disposeMainWindow();

    void simpleAlert(String alertText);

    void initConstructingFrame();
}

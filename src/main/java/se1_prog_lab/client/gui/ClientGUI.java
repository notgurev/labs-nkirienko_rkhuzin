package se1_prog_lab.client.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.client.ClientController;

import javax.swing.*;

@Singleton
public class ClientGUI implements ClientView {
    private final ClientController controller;
    private LoginFrame loginFrame;
    private MainFrame mainFrame;

    @Inject
    public ClientGUI(ClientController controller) {
        this.controller = controller;
    }

    @Override
    public void initLoginWindow() {
        loginFrame = new LoginFrame(controller);
    }

    @Override
    public void disposeLoginWindow() {
        loginFrame.dispose();
    }

    @Override
    public void initMainWindow(String username) {
        mainFrame = new MainFrame(controller, username);
    }

    @Override
    public void disposeMainWindow() {
        mainFrame.dispose();
    }

    @Override
    public void simpleAlert(String alertText) {
        JOptionPane.showMessageDialog(loginFrame, alertText);
    }
}

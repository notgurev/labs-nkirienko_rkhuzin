package se1_prog_lab.client.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.client.ClientCore;

import javax.swing.*;
import java.util.LinkedList;

@Singleton
public class ClientGUI implements ClientView {
    private final ClientCore controller;
    private LoginFrame loginFrame;
    private MainFrame mainFrame;
    private ConstructingFrame constructingFrame;
    private JournalFrame journalFrame;

    @Inject
    public ClientGUI(ClientCore controller) {
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

    @Override
    public void initConstructingFrame() {
        constructingFrame = new ConstructingFrame(controller);
    }

    @Override
    public void initJournalFrame(LinkedList<String> journal) {
        journalFrame = new JournalFrame(journal);
    }
}

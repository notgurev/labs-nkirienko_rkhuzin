package se1_prog_lab.client.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.client.ClientCore;
import se1_prog_lab.collection.LabWork;

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
        controller.addLanguageSubscriber(loginFrame);
    }

    @Override
    public void disposeLoginWindow() {
        loginFrame.dispose();
        controller.removeLanguageSubscriber(loginFrame);
    }

    @Override
    public void initMainWindow(String username) {
        mainFrame = new MainFrame(controller, username);
        controller.addLanguageSubscriber(mainFrame);
    }

    @Override
    public void disposeMainWindow() {
        mainFrame.dispose();
        controller.removeLanguageSubscriber(mainFrame);
    }

    @Override
    public void simpleAlert(String alertText) {
        JOptionPane.showMessageDialog(null, alertText);
    }

    @Override
    public void initConstructingFrame() {
        constructingFrame = new ConstructingFrame(controller, null);
    }

    @Override
    public void initJournalFrame(LinkedList<String> journal) {
        journalFrame = new JournalFrame(journal);
    }

    @Override
    public void update() {
        mainFrame.update();
    }

    @Override
    public void clear() {
        mainFrame.clear();
    }

    @Override
    public boolean isMainFrameInitialized() {
        return !(mainFrame == null);
    }

    @Override
    public void initConstructingFrame(LabWork labWork) {
        constructingFrame = new ConstructingFrame(controller, labWork);
    }
}

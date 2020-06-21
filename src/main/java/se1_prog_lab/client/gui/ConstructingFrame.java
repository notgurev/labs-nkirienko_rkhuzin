package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;

import javax.swing.*;
import java.awt.*;

public class ConstructingFrame extends JFrame {
    private final ClientCore controller;
    private final JPanel panel;

    public ConstructingFrame(ClientCore controller) throws HeadlessException {
        this.controller = controller;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(400, 100));

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        panel.setLayout(new GridLayout(0, 2));

        // в разработке
        addPropertyField("one-line field");
        addPropertyArea("description");

        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private void addPropertyField(String text) {
        JLabel label = new JLabel(text);
        JTextField field = new JTextField();
        panel.add(label);
        panel.add(field);
    }

    private void addPropertyArea(String text) {
        JLabel label = new JLabel(text);
        JTextArea field = new JTextArea();
        panel.add(label);
        panel.add(field);
    }
}

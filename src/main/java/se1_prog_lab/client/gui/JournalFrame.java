package se1_prog_lab.client.gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class JournalFrame extends JFrame {
    public JournalFrame(LinkedList<String> journal) {
        super("Журнал");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(150, 150));

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        panel.setLayout(new GridLayout(0, 1));

        if (journal.isEmpty()) {
            panel.add(new JLabel("Журнал пуст!", SwingConstants.CENTER));
        } else {
            JList<String> journalList = new JList<>(journal.toArray(new String[0]));
            panel.add(journalList);
        }

        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
}

package se1_prog_lab.client.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class JournalFrame extends JFrame {
    public JournalFrame(LinkedList<String> journal, Locale locale) {
        super();
        ResourceBundle r = ResourceBundle.getBundle("localization/gui", locale);
        setTitle(r.getString("journal.title"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(150, 150));

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        panel.setLayout(new GridLayout(0, 1));

        if (journal.isEmpty()) {
            panel.add(new JLabel(r.getString("journal.empty"), SwingConstants.CENTER));
        } else {
            ArrayList<String> localizedJournal =
                    (ArrayList<String>) journal.stream().map(r::getString).collect(Collectors.toList());
            JList<String> journalList = new JList<>(localizedJournal.toArray(new String[0]));
            panel.add(journalList);
        }

        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
}

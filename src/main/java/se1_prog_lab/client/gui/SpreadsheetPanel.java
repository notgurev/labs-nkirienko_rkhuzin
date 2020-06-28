package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.collection.Color;
import se1_prog_lab.collection.LabWork;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.stream.Collectors;

public class SpreadsheetPanel extends JPanel implements LangChangeSubscriber, CollectionChangeSubscriber {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final ClientCore clientCore;
    private final ResourceBundle r;
    private String[] headers;

    public SpreadsheetPanel(ClientCore clientCore) {
        this.clientCore = clientCore;
        r = ResourceBundle.getBundle("localization/gui", clientCore.getLocale());
        headers = getLocalizedHeaders(clientCore.getLocale());

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        tableModel.setColumnIdentifiers(headers);
        tableModel.setDataVector(getLocalizedData(), headers);
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new GridLayout());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) clientCore.openConstructingFrame(row);
            }
        });

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());

                Vector<LabWork> sorted = clientCore.getBufferedCollectionPage().stream().sorted((o1, o2) -> {
                    switch (column) {
                        case 0:
                            return o1.getId().compareTo(o2.getId());
                        case 1:
                            return o1.getName().compareTo(o2.getName());
                        case 2:
                            return Long.compare(o1.getCoordinates().getX(), o2.getCoordinates().getX());
                        case 3:
                            return Float.compare(o1.getCoordinates().getY(), o2.getCoordinates().getY());
                        case 4:
                            return o1.getCreationDate().compareTo(o2.getCreationDate());
                        case 5:
                            Integer mp1 = o1.getMinimalPoint();
                            Integer mp2 = o2.getMinimalPoint();
                            if (mp1 == null) return -1;
                            else if (mp2 == null) return 1;
                            return mp1.compareTo(mp2);
                        case 6:
                            return o1.getDescription().compareTo(o2.getDescription());
                        case 7:
                            Integer tiw1 = o1.getTunedInWorks();
                            Integer tiw2 = o2.getTunedInWorks();
                            if (tiw1 == null) return -1;
                            else if (tiw2 == null) return 1;
                            return tiw1.compareTo(tiw2);
                        case 8:
                            return o1.getDifficulty().compareTo(o2.getDifficulty());
                        case 9:
                            return o1.getAuthor().getName().compareTo(o2.getAuthor().getName());
                        case 10:
                            Float h1 = o1.getAuthor().getHeight();
                            Float h2 = o2.getAuthor().getHeight();
                            if (h1 == null) return -1;
                            else if (h2 == null) return 1;
                            return h1.compareTo(h2);
                        case 11:
                            return o1.getAuthor().getPassportID().compareTo(o2.getAuthor().getPassportID());
                        case 12:
                            Color c1 = o1.getAuthor().getHairColor();
                            Color c2 = o2.getAuthor().getHairColor();
                            if (c1 == null) return -1;
                            else if (c2 == null) return 1;
                            return c1.compareTo(c2);
                        case 13:
                            return o1.getAuthor().getLocation().getX().compareTo(o2.getAuthor().getLocation().getX());
                        case 14:
                            return Float.compare(o1.getAuthor().getLocation().getY(), o2.getAuthor().getLocation().getY());
                        case 15:
                            return o1.getAuthor().getLocation().getZ().compareTo(o2.getAuthor().getLocation().getZ());
                        case 16:
                            return o1.getOwner().compareTo(o2.getOwner());
                    }
                    return 0;
                }).collect(Collectors.toCollection(Vector::new));
                tableModel.setDataVector(getLocalizedData(sorted), headers);
                tableModel.fireTableDataChanged();
            }
        });

        add(scrollPane);
    }

    @Override
    public void changeLang(Locale locale) {
        headers = getLocalizedHeaders(locale);
        tableModel.setDataVector(getLocalizedData(), headers);
        tableModel.fireTableDataChanged();
    }

    private String[] getLocalizedHeaders(Locale locale) {
        ResourceBundle r = ResourceBundle.getBundle("localization/gui", locale);
        return new String[]{
                r.getString("SpreadsheetPanel.headers.id"),
                r.getString("SpreadsheetPanel.headers.name"),
                r.getString("SpreadsheetPanel.headers.coordinateX"),
                r.getString("SpreadsheetPanel.headers.coordinateY"),
                r.getString("SpreadsheetPanel.headers.creation_date"),
                r.getString("SpreadsheetPanel.headers.minimalPoint"),
                r.getString("SpreadsheetPanel.headers.description"),
                r.getString("SpreadsheetPanel.headers.tunedInWorks"),
                r.getString("SpreadsheetPanel.headers.difficulty"),
                r.getString("SpreadsheetPanel.headers.authorName"),
                r.getString("SpreadsheetPanel.headers.authorHeight"),
                r.getString("SpreadsheetPanel.headers.authorPassportID"),
                r.getString("SpreadsheetPanel.headers.authorHairColor"),
                r.getString("SpreadsheetPanel.headers.authorLocationX"),
                r.getString("SpreadsheetPanel.headers.authorLocationY"),
                r.getString("SpreadsheetPanel.headers.authorLocationZ"),
                r.getString("SpreadsheetPanel.headers.owner")
        };
    }

    @Override
    public void updateWithNewData() {
        tableModel.setDataVector(getLocalizedData(), headers);
        tableModel.fireTableDataChanged();
    }

    private Object[][] getLocalizedData() {
        return clientCore.getBufferedCollectionPage()
                .stream().map(labWork -> labWork.toLocalizedArray(clientCore.getLocale())).toArray(Object[][]::new);
    }

    private Object[][] getLocalizedData(Vector<LabWork> collectionPage) {
        return collectionPage
                .stream().map(labWork -> labWork.toLocalizedArray(clientCore.getLocale())).toArray(Object[][]::new);
    }
}

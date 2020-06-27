package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.collection.Color;
import se1_prog_lab.collection.Difficulty;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.*;
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
        tableModel.setDataVector(clientCore.getCollectionData(), headers);
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
            @SuppressWarnings("rawtypes")
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());

                Comparator<Vector> vectorComparator = (o1, o2) -> {
                    String first = o1.get(column).toString();
                    String second = o2.get(column).toString();
                    switch (column) {
                        case 0:
                        case 2: // я не знаю как лучше
                            return Long.compare(Long.parseLong(first), Long.parseLong(second));
                        case 1:
                        case 6:
                        case 9:
                        case 11:
                        case 16:
                            return (first).compareTo(second);
                        case 3:
                        case 10:
                        case 14:
                            return Float.compare(Float.parseFloat(first), Float.parseFloat(second));
                        case 4:
                            return LocalDateTime.parse(first).compareTo(LocalDateTime.parse(second));
                        case 5:
                        case 7:
                        case 13:
                        case 15:
                            return Integer.compare(Integer.parseInt(first), Integer.parseInt(second));
                        case 8:
                            return Difficulty.valueOf(first).compareTo(Difficulty.valueOf(second));
                        case 12:
                            return Color.valueOf(first).compareTo(Color.valueOf(second));
                    }
                    return 0;
                };

                tableModel.setDataVector((Vector<? extends Vector>)
                        tableModel.getDataVector()
                                .stream()
                                .sorted(vectorComparator)
                                .collect(Collectors.toCollection(Vector::new)), new Vector<>(Arrays.asList(headers))
                );
                tableModel.fireTableDataChanged();
            }
        });

        add(scrollPane);
    }

    @Override
    public void changeLang(Locale locale) {
        headers = getLocalizedHeaders(locale);
        tableModel.setDataVector(clientCore.getCollectionData(), headers);
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
        tableModel.setDataVector(clientCore.getCollectionData(), headers);
        tableModel.fireTableDataChanged();
    }
}

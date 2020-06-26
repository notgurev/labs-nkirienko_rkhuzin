package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.client.ModelListener;
import se1_prog_lab.collection.Color;
import se1_prog_lab.collection.Difficulty;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import java.util.stream.Collectors;

public class SpreadsheetPanel extends JPanel implements ModelListener, LangChangeSubscriber {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final String[] headers = {"ID", "Название", "Координата X", "Координата Y",
            "Дата создания", "Минимальная оценка", "Описание", "Настроенные работы",
            "Сложность", "Имя автора", "Рост", "Паспорт", "Цвет волос", "X", "Y", "Z"};
    private final ClientCore clientCore;

    public SpreadsheetPanel(ClientCore clientCore) {
        this.clientCore = clientCore;
        clientCore.addListener(this);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        tableModel.setColumnIdentifiers(headers);
        tableModel.setDataVector(clientCore.getCollectionData(), headers);
        JScrollPane scrollPane = new JScrollPane(table); // todo что-то с размером не так
        setLayout(new GridLayout());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    clientCore.openConstructingFrame(row);
                }
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
                updateTable();
            }
        });

        add(scrollPane);
    }

    public void update() {
        tableModel.setDataVector(clientCore.getCollectionData(), headers);
        updateTable();
    }

    public void updateTable() {
        tableModel.fireTableDataChanged();
    }

    public void addElement(Object[] fields) {
        if (!clientCore.hasNextPage()) {
            tableModel.addRow(fields);
            updateTable();
        }
    }

    public void updateElement(Long id, Object[] fields) {
        Integer row = findRowById(id);

        if (row != null) {
            for (int i = 0; i < fields.length; i++) {
                tableModel.setValueAt(fields[i], row, i);
            }
            updateTable();
        }
    }

    public void removeElement(Long id) {
        Integer row = findRowById(id);
        if (row != null) {
            tableModel.removeRow(row);
            updateTable();
        }
    }


    @SuppressWarnings("rawtypes")
    protected Integer findRowById(Long id) {
        for (Object v : tableModel.getDataVector()) {
            String currentId = ((Vector) v).elementAt(0).toString();

            if (currentId.equals(id.toString())) {
                return tableModel.getDataVector().indexOf(v);
            }
        }
        return null;
    }

    @Override
    public void changeLang() {
        // todo
    }
}

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
import java.util.Vector;

import static java.util.Comparator.comparing;

public class SpreadsheetPanel extends JPanel implements ModelListener {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final String[] headers = {"ID", "Название", "Координата X", "Координата Y",
            "Дата создания", "Минимальная оценка", "Описание", "Настроенные работы",
            "Сложность", "Имя автора", "Рост", "Паспорт", "Цвет волос", "X", "Y", "Z"};
    private final ClientCore clientCore;
    private final Class[] columnTypes = {Long.class, String.class, Long.class, Float.class,
            LocalDateTime.class, Integer.class, String.class, Integer.class,
            Difficulty.class, String.class, Float.class, String.class, Color.class, Integer.class,
            Float.class, Integer.class}; // я не знаю как удобнее это сделать для сортировки

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

        /*
        TODO у меня не получается сделать нормальную сортировку стримами, при том что я решил делать тупо по toString().
        Если разрешать в телеге так, то надо понять как нормально сделать collect, ибо у меня не получается.
        */
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                Vector<Vector> currentVector = tableModel.getDataVector();
                currentVector.sort(comparing((Vector o) -> o.get(column).toString()));
//                Stream<Vector> stream = tableModel.getDataVector()
//                        .stream().sorted(comparing((Vector o) -> o.get(column).toString()));
//                stream.collect(Vector::new);

//                tableModel.getDataVector().stream()
//                        .sorted(new Comparator<Vector>() {
//                            @Override
//                            public int compare(Vector o1, Vector o2) {
//                                return o1.get(column).toString().compareTo(o2.get(column).toString());
//                            }
//                        })
//                        .collect(Collectors.toCollection(Vector::new));
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


    protected Integer findRowById(Long id) {
        for (Object v : tableModel.getDataVector()) {
            String currentId = ((Vector) v).elementAt(0).toString();

            if (currentId.equals(id.toString())) {
                return tableModel.getDataVector().indexOf(v);
            }
        }
        return null;
    }
}

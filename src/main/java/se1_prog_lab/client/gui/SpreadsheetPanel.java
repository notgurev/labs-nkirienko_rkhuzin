package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.client.ModelListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class SpreadsheetPanel extends JPanel implements ModelListener {
    private JTable table;
    private final DefaultTableModel tableModel;
    private final String[] headers = {"ID", "Название", "Координата X", "Координата Y",
            "Дата создания", "Минимальная оценка", "Описание", "tunedInWorks",
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
        tableModel.addRow(fields);
        updateTable();
    }

    public void updateElement(Long id, Object[] fields) {
        int row = findRowById(id);
        for (int i = 0; i < fields.length; i++) {
            tableModel.setValueAt(fields[i], row, i);
        }
        updateTable();
    }

    public void removeElement(Long id) {
        int row = findRowById(id);
        tableModel.removeRow(row);
        updateTable();
    }

    protected Integer findRowById(Long id) {
        for (Object v: tableModel.getDataVector()) {
            String currentId = ((Vector) v).elementAt(0).toString();

            if (currentId.equals(id.toString())) {
                return tableModel.getDataVector().indexOf(v);
            }
        }
        return null;
    }
}

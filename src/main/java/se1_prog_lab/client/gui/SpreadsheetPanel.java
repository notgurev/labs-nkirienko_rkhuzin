package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SpreadsheetPanel extends JPanel {
    private JTable table;
    private final DefaultTableModel tableModel;
    private final String[] headers = {"ID", "Название", "Координата X", "Координата Y",
            "Дата создания", "Минимальная оценка", "Описание", "tunedInWorks",
            "Сложность", "Имя автора", "Рост", "Паспорт", "Цвет волос", "X", "Y", "Z"};
    private final ClientCore clientCore;

    public SpreadsheetPanel(ClientCore clientCore) {
        this.clientCore = clientCore;
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setColumnIdentifiers(headers);
        tableModel.setDataVector(clientCore.getCollectionData(), headers);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table); // todo что-то с размером не так

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
//                    long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
                    clientCore.openConstructingFrame(row);
                }
            }
        });

        add(scrollPane);
    }

    public void update() {
        // todo это не работает
        tableModel.setDataVector(clientCore.getCollectionData(), headers);
        tableModel.fireTableDataChanged();
        table.revalidate();
        table.repaint();
        revalidate();
        repaint();
    }
}

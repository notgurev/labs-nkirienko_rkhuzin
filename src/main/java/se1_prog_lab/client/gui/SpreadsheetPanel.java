package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
        add(table);
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

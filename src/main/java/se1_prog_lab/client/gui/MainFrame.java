package se1_prog_lab.client.gui;

import se1_prog_lab.client.interfaces.ClientController;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

import static javax.swing.SwingConstants.*;
import static javax.swing.SwingConstants.CENTER;

public class MainFrame extends JFrame {
    private ClientController controller;

    public MainFrame(ClientController controller, String username) {
        this.controller = controller;
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        createMenuBar(username);
        createToolBar();
        setVisible(true);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar(HORIZONTAL);
        toolBar.setFloatable(false);

        JButton add = new JButton("Добавить");
        toolBar.add(add);

        JButton countLessThanDesc = new JButton("Посчитать < описания"); // хз как подписать
        toolBar.add(countLessThanDesc);

        add(toolBar, BorderLayout.PAGE_START);
    }

    private void createMenuBar(String username) {
        // Иконка юзера
        Icon userIcon = new ImageIcon("usericon.gif");

        // Меню
        JMenuBar jMenuBar = new JMenuBar();

        // Отображение юзернейма
        jMenuBar.add(new JLabel(username, userIcon, CENTER));

        // Вид
        JMenu view = new JMenu("Вид");
        ButtonGroup viewGroup = new ButtonGroup();

        JRadioButtonMenuItem spreadsheet = new JRadioButtonMenuItem("Таблица");
        viewGroup.add(spreadsheet);
        view.add(spreadsheet);
        spreadsheet.addActionListener(this::setSpreadsheetMode);

        JRadioButtonMenuItem visualization = new JRadioButtonMenuItem("Визуализация");
        viewGroup.add(visualization);
        view.add(visualization);
        visualization.addActionListener(this::setVisualizationMode);

        jMenuBar.add(view);

        setJMenuBar(jMenuBar);
    }

    public void setSpreadsheetMode(ActionEvent e) {
        // отображение в виде таблица
    }

    public void setVisualizationMode(ActionEvent e) {
        // отображение в виде визуализации
    }
}

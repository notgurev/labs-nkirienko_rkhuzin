package se1_prog_lab.client.gui;

import se1_prog_lab.client.commands.concrete.CountLessThanDescription;
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
        countLessThanDesc.addActionListener(e -> {
            String description = JOptionPane.showInputDialog("Введите описание:");
            controller.executeServerCommand(new CountLessThanDescription(description));
        });

        add(toolBar, BorderLayout.PAGE_START);
    }

    private void createMenuBar(String username) {
        // Иконка юзера
        Icon userIcon = new ImageIcon("usericon.gif");

        // Меню
        JMenuBar jMenuBar = new JMenuBar();

        // Отображение юзернейма (меню)
        jMenuBar.add(new JLabel(username, userIcon, CENTER));

        {
            // Вид (меню) todo функционал
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
        }

        {
            // Язык (меню) todo функционал и дефолты
            JMenu language = new JMenu("Язык");
            jMenuBar.add(language);
            ButtonGroup languageGroup = new ButtonGroup();
            JRadioButtonMenuItem russian = new JRadioButtonMenuItem("Русский");
            language.add(russian);
            languageGroup.add(russian);
            JRadioButtonMenuItem slovenian = new JRadioButtonMenuItem("Slovenščina");
            language.add(slovenian);
            languageGroup.add(slovenian);
            JRadioButtonMenuItem polish = new JRadioButtonMenuItem("Polski");
            language.add(polish);
            languageGroup.add(polish);
            JRadioButtonMenuItem ecuador = new JRadioButtonMenuItem("Español (Ecuador)");
            language.add(ecuador);
            languageGroup.add(ecuador);
        }

        setJMenuBar(jMenuBar);
    }

    public void setSpreadsheetMode(ActionEvent e) {
        // todo отображение в виде таблица
    }

    public void setVisualizationMode(ActionEvent e) {
        // todo отображение в виде визуализации
        // todo паттерн стратегия
    }
}

package se1_prog_lab.client.gui;

import se1_prog_lab.client.commands.concrete.Clear;
import se1_prog_lab.client.commands.concrete.CountLessThanDescription;
import se1_prog_lab.client.commands.concrete.Info;
import se1_prog_lab.client.commands.concrete.PrintUniqueTunedInWorks;
import se1_prog_lab.client.interfaces.ClientController;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingConstants.*;
import static javax.swing.SwingConstants.CENTER;

public class MainFrame extends JFrame {
    private final ClientController controller;
    private JToolBar toolBar;

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
        toolBar = new JToolBar(HORIZONTAL);
        toolBar.setFloatable(false);

        // Add
        addToolBarButton("Добавить", e -> {/*TODO функционал add*/});
        // Count less than description
        addToolBarButton("Посчитать < описания", e -> {
            String description = JOptionPane.showInputDialog("Введите описание:");
            controller.executeServerCommand(new CountLessThanDescription(description));
        });
        // Clear
        addToolBarButton("Очистить", e -> {
            controller.executeServerCommand(new Clear());
        });
        // Info
        addToolBarButton("Информация", e -> {
            controller.executeServerCommand(new Info());
        });
        // Print unique tuned in works
        addToolBarButton("Уникальные tuned in works", e -> {
            controller.executeServerCommand(new PrintUniqueTunedInWorks()); // todo проверить с непустой коллекцией
        });

        add(toolBar, BorderLayout.PAGE_START);
    }

    private void addToolBarButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        toolBar.add(button);
        button.addActionListener(actionListener);
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

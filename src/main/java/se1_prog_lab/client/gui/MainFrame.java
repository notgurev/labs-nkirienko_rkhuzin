package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientController;
import se1_prog_lab.client.commands.concrete.*;
import se1_prog_lab.client.gui.strategies.DrawStrategyOne;
import se1_prog_lab.client.gui.strategies.DrawStrategyTwo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.HORIZONTAL;

public class MainFrame extends JFrame {
    private final ClientController controller;
    private JToolBar toolBar;
    private Mode mode = Mode.SPREADSHEET;
    private DrawStrategy drawStrategy;
    private JMenu strategy;
    private int selectedPage = 1;
    private JLabel selectedPageLabel;

    public MainFrame(ClientController controller, String username) {
        super("Управление и обзор");
        this.controller = controller;
        setMinimumSize(new Dimension(0, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        createMenuBar(username);
        createToolBar();
        pack();
        setVisible(true);
    }

    private void createToolBar() {
        toolBar = new JToolBar(HORIZONTAL);
        toolBar.setFloatable(false);

        {
            // Переключение страниц
            addToolBarButton("◀", e -> changeSelectedPage(-1));

            selectedPageLabel = new JLabel(" " + selectedPage + " ");
            toolBar.add(selectedPageLabel);

            addToolBarButton("▶", e -> changeSelectedPage(+1));
        }

        // Add
        addToolBarButton("Добавить", e -> {/*TODO функционал add*/
            controller.openConstructingFrame();
        });
        // Count less than description
        addToolBarButton("Посчитать < описания", e -> {
            String description = JOptionPane.showInputDialog("Введите описание:");
            controller.executeServerCommand(new CountLessThanDescription(description));
        });
        // Clear
        addToolBarButton("Очистить", e -> controller.executeServerCommand(new Clear()));
        // Info
        addToolBarButton("Информация", e -> controller.executeServerCommand(new Info()));
        // Print unique tuned in works // todo проверить с непустой коллекцией
        addToolBarButton("Уникальные tuned in works", e -> controller.executeServerCommand(new PrintUniqueTunedInWorks()));
        // Sort // todo проверить с непустой коллекцией
        addToolBarButton("Сортировать на сервере", e -> controller.executeServerCommand(new Sort()));

        // Журнал
        addToolBarButton("Журнал", e -> controller.openJournalFrame());

        add(toolBar, BorderLayout.PAGE_START);
    }

    private void changeSelectedPage(int change) {
        selectedPage += change;
        // получить от сервера нужную страницу
        // перерисовать таблицу/визуализацию этой страницы
        selectedPageLabel.setText(" " + selectedPage + " ");
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

            spreadsheet.setSelected(true);
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

        {
            strategy = new JMenu("Форма рисуемых объектов");
            jMenuBar.add(strategy);
            ButtonGroup strategies = new ButtonGroup();
            JRadioButtonMenuItem strategy1 = new JRadioButtonMenuItem("ФОРМА 1"); // todo нормальные названия
            strategy.add(strategy1);
            strategies.add(strategy1);
            JRadioButtonMenuItem strategy2 = new JRadioButtonMenuItem("ФОРМА 2");
            strategy.add(strategy2);
            strategies.add(strategy2);

            strategy.setEnabled(mode != Mode.SPREADSHEET);
        }

        setJMenuBar(jMenuBar);
    }

    public void setSpreadsheetMode(ActionEvent e) {
        mode = Mode.SPREADSHEET;
        drawStrategy = new DrawStrategyOne();
        strategy.setEnabled(false);
        // todo отображение в виде таблица
    }

    public void setVisualizationMode(ActionEvent e) {
        mode = Mode.VISUALIZATION;
        drawStrategy = new DrawStrategyTwo();
        strategy.setEnabled(true);
        // todo отображение в виде визуализации
    }

    enum Mode {
        VISUALIZATION,
        SPREADSHEET
    }
}

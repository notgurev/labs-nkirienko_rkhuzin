package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.client.commands.concrete.CountLessThanDescription;
import se1_prog_lab.client.commands.concrete.Info;
import se1_prog_lab.client.commands.concrete.PrintUniqueTunedInWorks;
import se1_prog_lab.client.commands.concrete.Sort;
import se1_prog_lab.client.gui.strategies.CircleStrategy;
import se1_prog_lab.client.gui.strategies.RectangleStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.HORIZONTAL;

public class MainFrame extends JFrame implements LangChangeSubscriber {
    private final ClientCore clientCore;
    private JToolBar toolBar;
    private Mode mode = Mode.SPREADSHEET;
    private JMenu strategy;
    private JLabel selectedPageLabel;

    private final VisualizationPanel visualizationPanel;
    private final SpreadsheetPanel spreadsheetPanel;

    public MainFrame(ClientCore clientCore, String username) {
        super("Управление и обзор");
        this.clientCore = clientCore;
        setMinimumSize(new Dimension(0, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        createMenuBar(username);
        createToolBar();
        spreadsheetPanel = new SpreadsheetPanel(this.clientCore);
        visualizationPanel = new VisualizationPanel(this.clientCore);
        getContentPane().add(spreadsheetPanel);
        pack();
        setVisible(true);
    }

    private void createToolBar() {
        toolBar = new JToolBar(HORIZONTAL);
        toolBar.setFloatable(false);

        // Переключение страниц
        addToolBarButton("◀", e -> changeSelectedPage(-1));
        selectedPageLabel = new JLabel(" " + clientCore.getSelectedPage() + " ");
        toolBar.add(selectedPageLabel);
        addToolBarButton("▶", e -> changeSelectedPage(+1));

        // Add
        addToolBarButton("Добавить", e -> clientCore.openConstructingFrame());

        // Count less than description
        addToolBarButton("Посчитать < описания", e -> {
            String description = JOptionPane.showInputDialog("Введите описание:");
            clientCore.executeServerCommand(new CountLessThanDescription(description));
        });

        // Clear
        addToolBarButton("Очистить", e -> clientCore.clear());

        // Info
        addToolBarButton("Информация", e -> clientCore.executeServerCommand(new Info()));

        // Print unique tuned in works
        addToolBarButton("Уникальные tuned in works", e -> clientCore.executeServerCommand(new PrintUniqueTunedInWorks()));

        // Sort // todo пофиксить поломку на null
        addToolBarButton("Сортировать на сервере", e -> clientCore.executeServerCommand(new Sort()));

        // Журнал
        addToolBarButton("Журнал", e -> clientCore.openJournalFrame());

        add(toolBar, BorderLayout.PAGE_START);
    }

    private void changeSelectedPage(int change) {
        if (clientCore.updateCollectionPage(change)) {
            selectedPageLabel.setText(" " + clientCore.getSelectedPage() + " ");
        }
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

            JRadioButtonMenuItem circleStrategy = new JRadioButtonMenuItem("Круги");
            strategy.add(circleStrategy);
            strategies.add(circleStrategy);
            circleStrategy.addActionListener(e -> {
                visualizationPanel.setDrawStrategy(new CircleStrategy());
                visualizationPanel.update(); // todo не работает
            });

            JRadioButtonMenuItem rectangleStrategy = new JRadioButtonMenuItem("Квадраты");
            strategy.add(rectangleStrategy);
            strategies.add(rectangleStrategy);
            rectangleStrategy.addActionListener(e -> {
                visualizationPanel.setDrawStrategy(new RectangleStrategy());
                visualizationPanel.update(); // todo не работает
            });
            rectangleStrategy.setSelected(true);

            strategy.setEnabled(mode != Mode.SPREADSHEET);
        }

        setJMenuBar(jMenuBar);
    }

    public void setSpreadsheetMode(ActionEvent e) {
        mode = Mode.SPREADSHEET;
        strategy.setEnabled(false);
        getContentPane().remove(visualizationPanel);
        getContentPane().add(spreadsheetPanel);
        revalidate();
        repaint();
    }

    public void setVisualizationMode(ActionEvent e) {
        mode = Mode.VISUALIZATION;
        strategy.setEnabled(true);
        getContentPane().remove(spreadsheetPanel);
        getContentPane().add(visualizationPanel);
        revalidate();
        repaint();
    }

    public void update() {
        spreadsheetPanel.update();
        visualizationPanel.update();
    }

    public void clear() {
        selectedPageLabel.setText(Integer.toString(clientCore.getSelectedPage()));
    }

    @Override
    public void changeLang() {
        // todo
    }

    enum Mode {
        VISUALIZATION,
        SPREADSHEET
    }
}

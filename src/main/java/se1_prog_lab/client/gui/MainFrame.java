package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.client.commands.concrete.*;
import se1_prog_lab.client.gui.strategies.CircleStrategy;
import se1_prog_lab.client.gui.strategies.RectangleStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.util.Locale.forLanguageTag;
import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.HORIZONTAL;

public class MainFrame extends JFrame implements LangChangeSubscriber {
    private final ClientCore clientCore;
    private final VisualizationPanel visualizationPanel;
    private final SpreadsheetPanel spreadsheetPanel;
    /**
     * JComponent - компонент, String - ключ для локализации
     */
    private final Map<JComponent, String> componentsWithText = new HashMap<>();
    private JToolBar toolBar;
    private Mode mode = Mode.SPREADSHEET;
    private JMenu strategy;
    private JLabel selectedPageLabel;
    private ResourceBundle r;
    private JButton pageDecrement;

    public MainFrame(ClientCore clientCore, String username) {
        super();
        r = ResourceBundle.getBundle("localization/gui", clientCore.getLocale());
        setTitle(r.getString("MainFrame.title"));
        this.clientCore = clientCore;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        createMenuBar(username);
        createToolBar();

        spreadsheetPanel = new SpreadsheetPanel(clientCore);
        clientCore.addLanguageSubscriber(spreadsheetPanel);
        clientCore.addCollectionChangeSubscriber(spreadsheetPanel);

        visualizationPanel = new VisualizationPanel(clientCore);
        clientCore.addCollectionChangeSubscriber(visualizationPanel);

        getContentPane().add(spreadsheetPanel);
        pack();
        setVisible(true);
    }

    private void createToolBar() {
        toolBar = new JToolBar(HORIZONTAL);
        toolBar.setFloatable(false);

        // Переключение страниц
        pageDecrement = new JButton("◀");
        toolBar.add(pageDecrement);
        pageDecrement.addActionListener(e -> changeSelectedPage(-1));
        pageDecrement.setEnabled(false);
        selectedPageLabel = new JLabel(" " + clientCore.getSelectedPage() + " ");
        toolBar.add(selectedPageLabel);
        addUnlocalizedToolBarButton("▶", e -> changeSelectedPage(+1));

        // Add
        addToolBarButton("MainFrame.toolbar.add", e -> clientCore.openConstructingFrame());

        // Count less than description
        addToolBarButton("MainFrame.toolbar.cltd", e -> {
            String description = JOptionPane.showInputDialog(r.getString("MainFrame.alerts.enter_description"));
            if (description != null) clientCore.executeServerCommand(new CountLessThanDescription(description));
        });

        // Clear
        addToolBarButton("MainFrame.toolbar.clear", e -> clientCore.executeServerCommand(new Clear()));

        // Info
        addToolBarButton("MainFrame.toolbar.info", e -> clientCore.executeServerCommand(new Info()));

        // Print unique tuned in works
        addToolBarButton("MainFrame.toolbar.utiw", e -> clientCore.executeServerCommand(new PrintUniqueTunedInWorks()));

        // Sort
        addToolBarButton("MainFrame.toolbar.sort", e -> clientCore.executeServerCommand(new Sort()));

        // Журнал
        addToolBarButton("MainFrame.toolbar.journal", e -> clientCore.openJournalFrame(clientCore.getLocale()));

        add(toolBar, BorderLayout.PAGE_START);
    }

    private void changeSelectedPage(int change) {
        int selectedPage = clientCore.getSelectedPage();
        clientCore.setSelectedPage(selectedPage + change);
        selectedPageLabel.setText(" " + clientCore.getSelectedPage() + " ");
        clientCore.updateCollectionPage(0);
        pageDecrement.setEnabled(selectedPage + change != 0);
    }

    private void addUnlocalizedToolBarButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        toolBar.add(button);
        button.addActionListener(actionListener);
    }

    private void addToolBarButton(String localizationKey, ActionListener actionListener) {
        JButton button = new JButton(r.getString(localizationKey));
        toolBar.add(button);
        componentsWithText.put(button, localizationKey);
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
            JMenu view = new JMenu(r.getString("MainFrame.menubar.view"));
            componentsWithText.put(view, "MainFrame.menubar.view");
            ButtonGroup viewGroup = new ButtonGroup();

            JRadioButtonMenuItem spreadsheet = new JRadioButtonMenuItem(r.getString("MainFrame.menubar.view.table"));
            componentsWithText.put(spreadsheet, "MainFrame.menubar.view.table");
            viewGroup.add(spreadsheet);
            view.add(spreadsheet);
            spreadsheet.addActionListener(this::setSpreadsheetMode);

            JRadioButtonMenuItem visualization = new JRadioButtonMenuItem(r.getString("MainFrame.menubar.view.visualization"));
            componentsWithText.put(visualization, "MainFrame.menubar.view.visualization");
            viewGroup.add(visualization);
            view.add(visualization);
            visualization.addActionListener(this::setVisualizationMode);

            spreadsheet.setSelected(true);
            jMenuBar.add(view);
        }

        {
            // Язык (меню)
            JMenu language = new JMenu(r.getString("MainFrame.menubar.language"));
            componentsWithText.put(language, "MainFrame.menubar.language");
            jMenuBar.add(language);
            ButtonGroup languageGroup = new ButtonGroup();

            JRadioButtonMenuItem russian = new JRadioButtonMenuItem("Русский");
            russian.addActionListener(e -> clientCore.setLocale(forLanguageTag("ru-RU")));
            language.add(russian);
            languageGroup.add(russian);

            JRadioButtonMenuItem slovenian = new JRadioButtonMenuItem("Slovenščina");
            slovenian.addActionListener(e -> clientCore.setLocale(forLanguageTag("sl-SL")));
            language.add(slovenian);
            languageGroup.add(slovenian);

            JRadioButtonMenuItem polish = new JRadioButtonMenuItem("Polski");
            polish.addActionListener(e -> clientCore.setLocale(forLanguageTag("pl-PL")));
            language.add(polish);
            languageGroup.add(polish);

            JRadioButtonMenuItem ecuador = new JRadioButtonMenuItem("Español (Ecuador)");
            ecuador.addActionListener(e -> clientCore.setLocale(forLanguageTag("es-EC")));
            language.add(ecuador);
            languageGroup.add(ecuador);

            switch (clientCore.getLocale().toString()) {
                case "ru_RU":
                    russian.setSelected(true);
                    break;
                case "sl_SL":
                    slovenian.setSelected(true);
                    break;
                case "pl_PL":
                    polish.setSelected(true);
                    break;
                case "es_EC":
                    ecuador.setSelected(true);
                    break;
            }
        }

        {
            strategy = new JMenu(r.getString("MainFrame.menubar.form"));
            componentsWithText.put(strategy, "MainFrame.menubar.form");
            jMenuBar.add(strategy);
            ButtonGroup strategies = new ButtonGroup();

            JRadioButtonMenuItem circleStrategy = new JRadioButtonMenuItem(r.getString("MainFrame.strategies.cirles"));
            componentsWithText.put(circleStrategy, "MainFrame.strategies.cirles");
            strategy.add(circleStrategy);
            strategies.add(circleStrategy);
            circleStrategy.addActionListener(e -> {
                visualizationPanel.setDrawStrategy(new CircleStrategy());
                visualizationPanel.updateWithNewData();
            });

            JRadioButtonMenuItem rectangleStrategy = new JRadioButtonMenuItem(r.getString("MainFrame.strategies.squares"));
            componentsWithText.put(rectangleStrategy, "MainFrame.strategies.squares");
            strategy.add(rectangleStrategy);
            strategies.add(rectangleStrategy);
            rectangleStrategy.addActionListener(e -> {
                visualizationPanel.setDrawStrategy(new RectangleStrategy());
                visualizationPanel.updateWithNewData();
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

    public void clear() {
        selectedPageLabel.setText(Integer.toString(clientCore.getSelectedPage()));
    }

    @Override
    public void changeLang() {
        r = ResourceBundle.getBundle("localization/gui", clientCore.getLocale());
        // Я не знаю, как лучше. У JComponent нет setText().
        for (Map.Entry<JComponent, String> entry : componentsWithText.entrySet()) {
            JComponent jComponent = entry.getKey();
            String s = entry.getValue();
            if (jComponent instanceof JButton) {
                ((JButton) jComponent).setText(r.getString(s));
            } else if (jComponent instanceof JRadioButtonMenuItem) {
                ((JRadioButtonMenuItem) jComponent).setText(r.getString(s));
            } else if (jComponent instanceof JMenu) {
                ((JMenu) jComponent).setText(r.getString(s));
            }
        }
    }

    enum Mode { //todo get rid of this useless thing
        VISUALIZATION,
        SPREADSHEET
    }
}

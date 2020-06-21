package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoginFrame extends JFrame {
    private final ClientCore controller;

    public LoginFrame(ClientCore controller) throws HeadlessException {
        super("Авторизация");
        this.controller = controller;
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        panel.setLayout(new GridLayout(0, 1));
        JLabel title = new JLabel("Войдите или зарегистрируйтесь:");
        panel.add(title);

        JTextField username = new JTextField("");
        panel.add(username);

        JPasswordField password = new JPasswordField("");
        panel.add(password);

        JButton login = new JButton("Войти");
        panel.add(login);
        login.addActionListener(event -> controller.login(username.getText(), Arrays.toString(password.getPassword())));

        JButton register = new JButton("Регистрация");
        panel.add(register);
        register.addActionListener(event -> controller.register(username.getText(), Arrays.toString(password.getPassword())));

        panel.add(new JComboBox<>(new String[]{"Русский", "Slovenščina", "Polski", "Español (Ecuador)"}));
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}

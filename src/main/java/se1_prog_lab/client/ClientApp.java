package se1_prog_lab.client;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.interfaces.Client;
import se1_prog_lab.client.interfaces.CommandRepository;
import se1_prog_lab.client.interfaces.ServerIO;

import java.util.Scanner;

import static se1_prog_lab.util.BetterStrings.coloredYellow;
import static se1_prog_lab.util.ValidatingReader.readString;

@Singleton
public class ClientApp implements Client {
    private final Scanner consoleScanner;
    private final CommandRepository commandRepository;
    private final ServerIO serverIO;
    private String login;
    private String password;

    @Inject
    public ClientApp(Scanner consoleScanner, CommandRepository commandRepository, ServerIO serverIO) {
        this.consoleScanner = consoleScanner;
        this.commandRepository = commandRepository;
        this.serverIO = serverIO;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ClientModule());
        Client clientApp = injector.getInstance(Client.class);
        clientApp.start();
    }

    @Override
    public void start() {
        System.out.println(coloredYellow("Начало работы клиента"));

//        authorize();

        serverIO.open();

        while (true) {
            System.out.print(">> ");

            String[] input = consoleScanner.nextLine().trim().split(" ");
            Command command = commandRepository.parseThenRun(input);

            if (command != null) serverIO.sendAndReceive(command);
        }
    }

    /*
     Пока чисто липовый метод, надо придумать как реально передавать login+password на сервер
     Можно возвращать и отправлять на сервер команду Register/Login или как-то так...
     Плюс еще нужна валидация, то есть нужно знать ответ, то есть возможно что вообще нужно
     это все делать как клиентскую часть команды, я хз...
    */
    private void authorize() {
        System.out.println("Для работы с коллекцией зарегистрироваться/авторизоваться");

        String input;
        do {
            System.out.printf("Введите %s для регистрации или %s для авторизации \n",
                    coloredYellow("register"), coloredYellow("login"));
            input = consoleScanner.nextLine();
        } while (!(input.equals("login") || input.equals("register")));

        switch (input) {
            case "login":
                login = readString(consoleScanner, "Введите ваш логин: ", false, null);
                password = readString(consoleScanner, "Введите ваш пароль: ", false, null);
                break;
            case "register":
                login = readString(consoleScanner, "Придумайте логин: ", false, 1);
                password = readString(consoleScanner, "Придумайте пароль: ", false, 1);
                break;
        }

    }

}

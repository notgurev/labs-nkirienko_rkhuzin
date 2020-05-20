package se1_prog_lab.client.interfaces;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

public interface ClientCommandReceiver {
    Scanner getConsoleScanner();

    Scanner getScriptScanner();

    void setScriptScanner(Scanner scanner);

    Collection<String> getExecutingScripts();

    void help();

    void history();

    void exit();

    void setHelpText(String helpText);

    LinkedList<String> getCommandHistory();

    void addToHistory(String commandKey);
}

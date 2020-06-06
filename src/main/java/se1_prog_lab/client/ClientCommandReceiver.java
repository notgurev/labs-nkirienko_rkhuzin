package se1_prog_lab.client;

import java.util.Collection;
import java.util.Scanner;

public interface ClientCommandReceiver {
    Scanner getConsoleScanner();

    Scanner getScriptScanner();

    Collection<String> getExecutingScripts();
}

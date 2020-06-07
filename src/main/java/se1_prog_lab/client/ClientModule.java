package se1_prog_lab.client;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import se1_prog_lab.client.gui.ClientGUI;
import se1_prog_lab.client.gui.ClientView;
import se1_prog_lab.util.LengthEOTWrapper;
import se1_prog_lab.util.interfaces.EOTWrapper;

import java.util.Scanner;

/**
 * Клиентский модуль для Guice Dependency Injection.
 */
public class ClientModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ClientController.class).to(ClientApp.class);
        bind(ServerIO.class).to(MyServerIO.class);
        bind(EOTWrapper.class).to(LengthEOTWrapper.class);
        bind(ClientView.class).to(ClientGUI.class);
        bind(ClientModel.class).to(ClientData.class);
    }

    @Provides
    Scanner provideConsoleScanner() {
        return new Scanner(System.in);
    }
}

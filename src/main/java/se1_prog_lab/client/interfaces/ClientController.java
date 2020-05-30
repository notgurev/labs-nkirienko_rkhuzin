package se1_prog_lab.client.interfaces;

import se1_prog_lab.client.commands.ClientServerSideCommand;

import javax.annotation.Nonnull;

public interface ClientController {
    void start();

    void executeServerCommand(@Nonnull ClientServerSideCommand command);

    void login(String username, String password);

    void register(String username, String password);
}

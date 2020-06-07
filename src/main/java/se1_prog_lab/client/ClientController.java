package se1_prog_lab.client;

import se1_prog_lab.client.commands.BasicCommand;

import javax.annotation.Nonnull;

public interface ClientController {
    void start();

    void executeServerCommand(@Nonnull BasicCommand command);

    void login(String username, String password);

    void register(String username, String password);

    void openConstructingFrame();

    void openJournalFrame();
}

package se1_prog_lab.util;

import se1_prog_lab.client.commands.ClientServerSideCommand;

import java.io.Serializable;

public class CommandWrapper implements Serializable {
    private final ClientServerSideCommand command;
    private final AuthData authData;

    public CommandWrapper(ClientServerSideCommand command, AuthData authData) {
        this.command = command;
        this.authData = authData;
    }

    public ClientServerSideCommand getCommand() {
        return command;
    }

    public AuthData getAuthData() {
        return authData;
    }
}

package se1_prog_lab.shared.api;

import se1_prog_lab.client.commands.BasicCommand;

import java.io.Serializable;
import java.util.Locale;

public class CommandWrapper implements Serializable {
    private final BasicCommand command;
    private final AuthData authData;
    private final Locale locale;

    public CommandWrapper(BasicCommand command, AuthData authData, Locale locale) {
        this.command = command;
        this.authData = authData;
        this.locale = locale;
    }

    public BasicCommand getCommand() {
        return command;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public Locale getLocale() {
        return locale;
    }
}

package se1_prog_lab.client.commands;

import se1_prog_lab.util.AuthData;

/**
 * Базовый класс команды
 */
public abstract class AbstractCommand implements Command {
    private final transient boolean  serverSide;
    private final transient String helpText;
    private final transient String key;
    private AuthData authData;

    public AbstractCommand(boolean serverSide, String helpText, String key) {
        this.serverSide = serverSide;
        this.helpText = helpText;
        this.key = key;
    }

    @Override
    public String getHelpText() {
        return helpText;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public boolean isServerSide() {
        return serverSide;
    }

    @Override
    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    @Override
    public AuthData getAuthData() {
        return authData;
    }
}

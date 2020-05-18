package se1_prog_lab.client.commands;

import se1_prog_lab.util.AuthData;

/**
 * Обычная клиент-серверная команда.
 * Не создает объект.
 * В clientExecute() должна выполнить валидацию, если это необходимо.
 * В execute() выполняет необходимые действия.
 * Отправляется на сервер.
 */
public abstract class ClientServerSideCommand extends BaseCommand {
    protected AuthData authData;

    public ClientServerSideCommand(String key, String helpText) {
        super(key, helpText);
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public AuthData getAuthData() {
        return authData;
    }
}

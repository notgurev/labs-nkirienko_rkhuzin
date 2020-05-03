package se1_prog_lab.client.commands;

import se1_prog_lab.util.AuthData;

/**
 * Команда авторизации.
 * Не содержится в банке команд, нельзя напрямую вызвать из консоли.
 * Используется для отправки AuthData на сервер.
 */
public abstract class AuthCommand extends NonValidatingRegularCommand {
    public AuthCommand(AuthData authData) {
        super(null,null);
        super.setAuthData(authData);
    }
}

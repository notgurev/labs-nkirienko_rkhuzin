package se1_prog_lab.util;

import java.io.Serializable;

/**
 * Класс данных для авторизации (имя пользователя + пароль).
 */
public class AuthData implements Serializable {
    private final String username;
    private final String password;

    public AuthData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

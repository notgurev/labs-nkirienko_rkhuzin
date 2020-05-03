package se1_prog_lab.util;

/**
 * Класс для передачи ключей авторизации между сервером и клиентом.
 */
public enum AuthStrings {
    LOGIN_SUCCESSFUL("Вы успешно авторизованы!"),
    REGISTRATION_SUCCESSFUL("Вы успешно зарегистрированы!"),
    INCORRECT_LOGIN_DATA("Получены некорректные данные для авторизации!"),
    USERNAME_TAKEN("Это имя пользователя уже занято!"),
    AUTH_FAILED("Команда содержит некорректные данные для авторизации!");

    private final String message;

    AuthStrings(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

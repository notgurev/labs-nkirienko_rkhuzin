package se1_prog_lab.shared.api;

/**
 * Класс для передачи ключей авторизации между сервером и клиентом.
 */
public enum AuthStatus {
    LOGIN_SUCCESSFUL("AuthStrings.LOGIN_SUCCESSFUL"),
    REGISTRATION_SUCCESSFUL("AuthStrings.REGISTRATION_SUCCESSFUL"),
    INCORRECT_LOGIN_DATA("AuthStrings.INCORRECT_LOGIN_DATA"),
    USERNAME_TAKEN("AuthStrings.USERNAME_TAKEN"),
    AUTH_FAILED("AuthStrings.AUTH_FAILED"),
    SERVER_ERROR("AuthStrings.SERVER_ERROR");
    private final String message;

    AuthStatus(String message) {
        this.message = message;
    }

    public String getMessageLocalizationKey() {
        return message;
    }
}

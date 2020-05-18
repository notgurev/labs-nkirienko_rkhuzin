package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.exceptions.DatabaseException;
import se1_prog_lab.exceptions.PasswordHashException;
import se1_prog_lab.server.interfaces.AuthManager;
import se1_prog_lab.server.interfaces.DatabaseManager;
import se1_prog_lab.server.interfaces.SecurePassword;
import se1_prog_lab.util.AuthData;

import java.util.logging.Logger;

/**
 * Класс-менеджер авторизации
 */
@Singleton
public class AuthManagerImpl implements AuthManager {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());
    private final DatabaseManager databaseManager;
    private final SecurePassword securePassword;

    @Inject
    public AuthManagerImpl(DatabaseManager databaseManager, SecurePassword securePassword) {
        this.databaseManager = databaseManager;
        this.securePassword = securePassword;
    }

    /**
     * Проверяет, правильные ли данные авторизации пришли вместе с командой.
     *
     * @param authData данные для авторизации.
     * @return true, если правильные; false, если нет.
     */
    @Override
    public boolean checkAuth(AuthData authData) throws DatabaseException {
        try {
            String hashedPassword = databaseManager.getPassword(authData.getUsername());
            if (hashedPassword == null) {
                return false;
            }

            return securePassword.validatePassword(authData.getPassword(), hashedPassword);
        } catch (PasswordHashException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Проверяет, зарегистрирован ли пользователь.
     *
     * @param username имя пользователя
     * @return true, если зарегистрирован; false, если нет.
     */
    @Override
    public boolean doesUserExist(String username) throws DatabaseException {
        return databaseManager.doesUserExist(username);
    }

    /**
     * Регистрация пользователя.
     *
     * @param authData данные для регистрации.
     */
    @Override
    public void register(AuthData authData) throws DatabaseException {
        try {
            databaseManager.addUser(authData.getUsername(), securePassword.hash(authData.getPassword()));
        } catch (PasswordHashException e) {
            logger.warning("Ошибка хеширования пароля: " + e.getMessage());
            throw new DatabaseException(e.getMessage());
        }
    }
}

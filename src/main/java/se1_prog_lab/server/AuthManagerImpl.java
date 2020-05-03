package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.server.interfaces.AuthManager;
import se1_prog_lab.server.interfaces.DatabaseManager;
import se1_prog_lab.util.AuthData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Класс-менеджер авторизации
 */
@Singleton
public class AuthManagerImpl implements AuthManager {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());
    private final DatabaseManager databaseManager;

    @Inject
    public AuthManagerImpl(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Проверяет, правильные ли данные авторизации пришли вместе с командой.
     *
     * @param authData данные для авторизации.
     * @return true, если правильные; false, если нет.
     */
    @Override
    public boolean checkAuth(AuthData authData) {
        return databaseManager.checkAuth(authData);
    }

    /**
     * Проверяет, зарегистрирован ли пользователь.
     *
     * @param username имя пользователя
     * @return true, если зарегистрирован; false, если нет.
     */
    @Override
    public boolean doesUserExist(String username) {
        return databaseManager.doesUserExist(username);
    }

    /**
     * Регистрация пользователя.
     *
     * @param authData данные для регистрации.
     */
    @Override
    public void register(AuthData authData) {
        // TODO наверное это не нужно.
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            databaseManager.addUser(authData.getUsername(), Arrays.toString(md.digest(authData.getPassword().getBytes())));
        } catch (NoSuchAlgorithmException e) {
            logger.severe("Не удалось получить алгоритм шифрования");
            System.exit(1);
        }
    }
}

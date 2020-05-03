package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.DatabaseManager;
import se1_prog_lab.util.AuthData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Класс для работы с базой данных.
 * Я во многом тут не уверен, наверное придется переделывать сигнатуры и так далее.
 * TODO надо придумать, как передавать сюда права и как прикрутить многопоточность.
 */
@Singleton
public class DatabaseManagerImpl implements DatabaseManager {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());
    private final String URL = "jdbc:postgresql://localhost:5432/se1-prog-lab";
    // Ссылка на коллекцию для загрузки
    private final CollectionWrapper collectionWrapper;
    // Я не уверен что адекватно сделал поля ниже
    private final String ADMIN_USERNAME = "postgres";
    private final String ADMIN_PASSWORD = "admin";
    private final String PASSWORD = ADMIN_PASSWORD;
    private final String USER = ADMIN_USERNAME;


    /**
     * Конструктор. Подключает драйвер.
     */
    @Inject
    public DatabaseManagerImpl(CollectionWrapper collectionWrapper) {
        this.collectionWrapper = collectionWrapper;
        try {
            Class.forName("org.postgresql.Driver");
            logger.info("Драйвер подключён");
        } catch (ClassNotFoundException e) {
            logger.severe("PostgreSQL JDBC Driver не найден.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Добавляет элемент в БД.
     *
     * @param labWork элемент для добавления
     * @return true, если успешно; false, если нет
     */
    @Override
    public boolean addElement(LabWork labWork) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
         */
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addThenLoad(LabWork labWork) {
        return addElement(labWork) && loadCollectionFromDatabase();
    }

    /**
     * Удаляет элемент из БД. Понятия не имею, что он должен принимать в качестве аргумента.
     * Посмотрим по мере выполнения.
     *
     * @return true, если успешно; false, если нет
     */
    @Override
    public boolean removeElement() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
         */
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    /**
     * Для команды Update id. Удаляет элемент с данным id и вставляет вместо него новый.
     *
     * @param labWork новый элемент
     * @param id      id старого элемента
     * @return true, если успешно; false, если нет
     */
    @Override
    public boolean updateById(LabWork labWork, long id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
            UPDATE table_name
            SET column1 = value1, column2 = value2, ...
            WHERE condition;
            или чет такое
         */
            loadCollectionFromDatabase();
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    /**
     * Загружает коллекцию из БД.
     *
     * @return загруженную коллекцию / null, если не удалось загрузить
     */
    @Override
    public boolean loadCollectionFromDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Vector<LabWork> newCollection = new Vector<>();
            /*
                Тут запрос к БД и заполнение newCollection
             */
            collectionWrapper.setVector(newCollection);
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    /**
     * Проверяет, соответствуют ли данные (имя пользователя и пароль) в команде реальному пользователю в базе данных
     *
     * @param authData данные для авторизации
     * @return true, если соответствуют; false, если нет.
     */
    @Override
    public boolean checkAuth(AuthData authData) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            /*
                Тут запрос к БД
             */
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    /**
     * Проверяет, существует (зарегистрирован) ли пользователь
     *
     * @param username имя пользователя
     * @return true, если существует; false, если нет.
     */
    @Override
    public boolean doesUserExist(String username) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
         */
            return true; // или false
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    /**
     * Добавляет нового пользователя в базу данных
     *
     * @param username имя пользователя
     * @param password пароль
     * @return true, если успешно; false, если нет.
     */
    @Override
    public boolean addUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
         */
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean clear() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
         */
            loadCollectionFromDatabase();
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addThenLoad(LabWork labWork, int index) {
        return addElementToIndex(labWork, index) && loadCollectionFromDatabase();
    }

    @Override
    public boolean addElementToIndex(LabWork labWork, int index) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
         */
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sortById() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
         */
            loadCollectionFromDatabase();
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeById(long id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        /*
            Тут запрос к БД
         */
            loadCollectionFromDatabase();
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }
}

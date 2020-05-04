package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.Coordinates;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.collection.Location;
import se1_prog_lab.collection.Person;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.DatabaseManager;
import se1_prog_lab.server.interfaces.SqlConsumer;
import se1_prog_lab.util.AuthData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;
import java.util.function.Consumer;
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
    // Я не уверен что адекватно сделал поля ниже
    private final String ADMIN_USERNAME = "postgres";
    private final String ADMIN_PASSWORD = "admin";
    private final String PASSWORD = ADMIN_PASSWORD;
    private final String USER = ADMIN_USERNAME;
    private Properties dbProps;


    /**
     * Конструктор. Подключает драйвер.
     */
    @Inject
    public DatabaseManagerImpl() {
        try {
            this.dbProps = getDBProperties();
            Class.forName("org.postgresql.Driver");
            logger.info("Драйвер подключён");
        } catch (ClassNotFoundException e) {
            logger.severe("PostgreSQL JDBC Driver не найден.");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            logger.severe("Не удалось загрузить параметры соединения к БД: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Properties getDBProperties() throws IOException {
        Properties dbProps = new Properties();

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("database.properties");
        if (stream != null) {
            dbProps.load(stream);
        } else {
            throw new FileNotFoundException("db property file not found");
        }

        return dbProps;
    }

    /**
     * Добавляет элемент в БД.
     *
     * @param labWork элемент для добавления
     * @return true, если успешно; false, если нет
     */
    @Override
    public boolean addElement(LabWork labWork) {
        return handleQuery((Connection connection) -> {

            Person author = labWork.getAuthor();
            Location authorLocation = author.getLocation();
            String addPersonSql = "INSERT INTO person (person_name, height, passportID, hair_color, locationX, locationY, locationZ)" +
                    "VALUES (?, ?, ?, ?::color, ?, ?, ?)";

            PreparedStatement authorStatement = connection.prepareStatement(addPersonSql, Statement.RETURN_GENERATED_KEYS);
            authorStatement.setString(1, author.getName());
            authorStatement.setFloat(2, author.getHeight());
            authorStatement.setString(3, author.getPassportID());
            authorStatement.setString(4, author.getHairColor().name());
            authorStatement.setInt(5, authorLocation.getX());
            authorStatement.setFloat(6, authorLocation.getY());
            authorStatement.setInt(7, authorLocation.getZ());

            authorStatement.executeUpdate();

            ResultSet rs = authorStatement.getGeneratedKeys();
            if (rs.next()) {
                String addElementSql = "INSERT INTO labwork (labwork_name, coordinateX, coordinateY, creationDate, minimalPoint, description, tunedInWorks, difficulty, person_id)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?::difficulty, ?)";

                Coordinates coordinates = labWork.getCoordinates();

                PreparedStatement elementStatement = connection.prepareStatement(addElementSql);
                elementStatement.setString(1, labWork.getName());
                elementStatement.setLong(2, coordinates.getX());
                elementStatement.setFloat(3, coordinates.getY());
                elementStatement.setTimestamp(4, Timestamp.valueOf(labWork.getCreationDate()));
                elementStatement.setInt(5, labWork.getMinimalPoint());
                elementStatement.setString(6, labWork.getDescription());
                elementStatement.setInt(7, labWork.getTunedInWorks());
                elementStatement.setString(8, labWork.getDifficulty().name());
                elementStatement.setLong(9, rs.getLong(1));

                elementStatement.executeUpdate();

                logger.info("В коллекцию добавлен элемент");
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        });
    }

    public boolean handleQuery(SqlConsumer<Connection> queryBody) {
        try (Connection connection = DriverManager.getConnection(dbProps.getProperty("url"), dbProps)) {
            queryBody.accept(connection);

            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
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
    public boolean loadCollectionFromDatabase(CollectionWrapper collectionWrapper) {
        return handleQuery((connection -> {
            Vector<LabWork> newCollection = new Vector<>();
            /*
                Тут запрос к БД и заполнение newCollection
             */

          collectionWrapper.setVector(newCollection);
        }));
    }

    /**
     * Проверяет, соответствуют ли данные (имя пользователя и пароль) в команде реальному пользователю в базе данных
     *
     * @param authData данные для авторизации
     * @return true, если соответствуют; false, если нет.
     */
    @Override
    public boolean checkAuth(AuthData authData) {
        try (Connection connection = DriverManager.getConnection(dbProps.getProperty("url"), dbProps)) {
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
        try (Connection connection = DriverManager.getConnection(dbProps.getProperty("url"), dbProps)) {
        /*
            Тут запрос к БД
         */
            return false; // или false
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
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
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
            return true;
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return false;
        }
    }
}

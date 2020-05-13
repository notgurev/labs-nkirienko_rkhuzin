package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.*;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.DatabaseManager;
import se1_prog_lab.server.interfaces.SqlConsumer;
import se1_prog_lab.server.interfaces.SqlFunction;
import se1_prog_lab.util.AuthData;
import se1_prog_lab.util.ElementCreator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
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
    public Long addElement(LabWork labWork) {
        return this.<Long>handleQuery((Connection connection) -> {

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

                PreparedStatement elementStatement = connection.prepareStatement(addElementSql, Statement.RETURN_GENERATED_KEYS);
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

                ResultSet result = elementStatement.getGeneratedKeys();

                result.next();


                logger.info("В коллекцию добавлен элемент");
                return result.getLong(1);
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

    public <T> T handleQuery(SqlFunction<Connection, T> queryBody) {
        try (Connection connection = DriverManager.getConnection(dbProps.getProperty("url"), dbProps)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            logger.severe("Не удалось получить доступ к базе данных: " + e.getMessage());
            return null;
        }
    }

    /**
     * Удаляет элемент из БД. Понятия не имею, что он должен принимать в качестве аргумента.
     * Посмотрим по мере выполнения.
     *
     * @return true, если успешно; false, если нет
     */
    @Override
    public boolean removeById(long id) {
        return handleQuery((Connection connection) -> {
            String query = "DELETE from labwork" +
                    " WHERE labwork.labwork_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Элемента с заданным id нет в бд");
            }
        });
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
        return handleQuery((Connection connection) -> {

            //(labwork_name, coordinateX, coordinateY, creationDate, minimalPoint, description, tunedInWorks, difficulty, person_id)
            String query =
                    "BEGIN TRANSACTION;" +
                    "UPDATE labwork" +
                    " SET labwork_name = ?," +
                    "coordinateX = ?," +
                    "coordinateY = ?," +
                    "creationDate = ?," +
                    "minimalPoint = ?," +
                    "description = ?," +
                    "tunedInWorks = ?," +
                    "difficulty = ?::difficulty" +
                    " WHERE labwork.labwork_id = ?;" +
                    "UPDATE person" +
                    " SET person_name = ?," +
                    "height = ?," +
                    "passportID = ?," +
                    "hair_color = ?::color," +
                    "locationX = ?," +
                    "locationY = ?," +
                    "locationZ = ?" +
                    "FROM labwork" +
                    " WHERE labwork.person_id = person.person_id and labwork.labwork_id = ?;" +
                    "COMMIT;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, labWork.getName());
            statement.setLong(2, labWork.getCoordinates().getX());
            statement.setFloat(3, labWork.getCoordinates().getY());
            statement.setTimestamp(4, Timestamp.valueOf(labWork.getCreationDate()));
            statement.setInt(5, labWork.getMinimalPoint());
            statement.setString(6, labWork.getDescription());
            statement.setInt(7, labWork.getTunedInWorks());
            statement.setString(8, labWork.getDifficulty().name());
            statement.setLong(9, id);

            Person author = labWork.getAuthor();
            statement.setString(10, author.getName());
            statement.setFloat(11, author.getHeight());
            statement.setString(12, author.getPassportID());
            statement.setString(13, author.getHairColor().name());
            statement.setInt(14, author.getLocation().getX());
            statement.setFloat(15, author.getLocation().getY());
            statement.setInt(16, author.getLocation().getZ());
            statement.setLong(17, id);

            statement.executeUpdate();
        });
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
            String query = "SELECT * FROM labwork" +
                    " INNER JOIN person ON labwork.person_id = person.person_id";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            LabWorkParams labWorkParams;
            while (rs.next()) {
                labWorkParams = new LabWorkParams();
                labWorkParams.setName(rs.getString("labwork_name"));
                labWorkParams.setId(rs.getLong("labwork_id"));
                labWorkParams.setCoordinateX(rs.getLong("coordinatex"));
                labWorkParams.setCoordinateY(rs.getFloat("coordinatey"));
                labWorkParams.setCreationDate(rs.getTimestamp("creationdate").toLocalDateTime());
                labWorkParams.setMinimalPoint(rs.getInt("minimalpoint"));
                labWorkParams.setDescription(rs.getString("description"));
                labWorkParams.setTunedInWorks(rs.getInt("tunedinworks"));
                labWorkParams.setDifficulty(Difficulty.valueOf(rs.getString("difficulty")));
                labWorkParams.setAuthorName(rs.getString("person_name"));
                labWorkParams.setAuthorHeight(rs.getFloat("height"));
                labWorkParams.setAuthorPassportID(rs.getString("passportid"));
                labWorkParams.setAuthorHairColor(Color.valueOf(rs.getString("hair_color")));
                labWorkParams.setAuthorLocationX(rs.getInt("locationx"));
                labWorkParams.setAuthorLocationY(rs.getFloat("locationy"));
                labWorkParams.setAuthorLocationZ(rs.getInt("locationz"));
                newCollection.add(ElementCreator.createLabWork(labWorkParams));
            }
          collectionWrapper.setVector(newCollection);
          logger.info("База данных загружена");
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
}

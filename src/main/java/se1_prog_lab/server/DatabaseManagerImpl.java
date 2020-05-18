package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.*;
import se1_prog_lab.exceptions.DatabaseException;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.DatabaseManager;
import se1_prog_lab.server.interfaces.SqlConsumer;
import se1_prog_lab.server.interfaces.SqlFunction;
import se1_prog_lab.util.ElementCreator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Класс для работы с базой данных.
 */
@Singleton
public class DatabaseManagerImpl implements DatabaseManager {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());
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

    /**
     * Получает свойства для подключения к базе данных из файла.
     *
     * @return свойства из файла
     * @throws IOException если не удалось получить свойства
     */
    private Properties getDBProperties() throws IOException {
        Properties dbProps = new Properties();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("database.properties");
        if (stream != null) {
            dbProps.load(stream);
            return dbProps;
        } else {
            throw new FileNotFoundException("db property file not found");
        }
    }

    /**
     * Добавляет элемент в базу данных.
     *
     * @param labWork  добавляемый элемент
     * @param username имя пользователя (создателя элемента)
     * @return назначенный базой данных id для этого элемента
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    @Override
    public Long addElement(LabWork labWork, String username) throws DatabaseException {
        return this.<Long>handleQuery((Connection connection) -> {

            Person author = labWork.getAuthor();
            Location authorLocation = author.getLocation();
            String addPersonSql = "INSERT INTO person (person_name, height, passportID, hair_color, " +
                    "locationX, locationY, locationZ)" +
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
                String addElementSql = "INSERT INTO labwork (labwork_name, coordinateX, coordinateY, creationDate, " +
                        "minimalPoint, description, tunedInWorks, difficulty, person_id, user_id)" +
                        " SELECT ?, ?, ?, ?, ?, ?, ?, ?::difficulty, ?, id" +
                        " FROM \"user\"" +
                        " WHERE \"user\".login = ?";

                PreparedStatement elementStatement = connection.prepareStatement(addElementSql, Statement.RETURN_GENERATED_KEYS);
                Coordinates coordinates = labWork.getCoordinates();
                elementStatement.setString(1, labWork.getName());
                elementStatement.setLong(2, coordinates.getX());
                elementStatement.setFloat(3, coordinates.getY());
                elementStatement.setTimestamp(4, Timestamp.valueOf(labWork.getCreationDate()));
                elementStatement.setInt(5, labWork.getMinimalPoint());
                elementStatement.setString(6, labWork.getDescription());
                elementStatement.setInt(7, labWork.getTunedInWorks());
                elementStatement.setString(8, labWork.getDifficulty().name());
                elementStatement.setLong(9, rs.getLong(1));
                elementStatement.setString(10, username);

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

    /**
     * Обработка запроса без возврата значения.
     *
     * @param queryBody тело запроса (Consumer)
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    private void handleQuery(SqlConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbProps.getProperty("url"), dbProps)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    /**
     * Обработка запроса с возвратом значения.
     *
     * @param queryBody тело запроса (Function)
     * @param <T>       тип возвращаемого значения
     * @return запрошенное у базы данных значение
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    private <T> T handleQuery(SqlFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbProps.getProperty("url"), dbProps)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    /**
     * Удаляет элемент из базы данных по id.
     *
     * @param id       id удаляемого элемента
     * @param username пользователь, который пытается удалить элемент
     * @return true, если успешно; false, если нет
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    @Override
    public boolean removeById(long id, String username) throws DatabaseException {
        return handleQuery((Connection connection) -> {
            String query =
                    "DELETE from person" +
                            " USING \"user\", labwork" +
                            " WHERE person.person_id = labwork.person_id AND labwork.labwork_id = ? " +
                            "AND labwork.user_id = \"user\".id AND \"user\".login = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            statement.setString(2, username);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        });
    }

    /**
     * Обновляет элемент базы данных с указанным id.
     *
     * @param labWork  новый элемент
     * @param id       id обновляемого элемента
     * @param username пользователь, который пытается обновить элемент
     * @return true, если успешно; false, если нет
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    @Override
    public boolean updateById(LabWork labWork, long id, String username) throws DatabaseException {
        return handleQuery((Connection connection) -> {
            connection.createStatement().execute("BEGIN TRANSACTION;");
            String query =
                    "UPDATE labwork" +
                            " SET labwork_name = ?," +
                            "coordinateX = ?," +
                            "coordinateY = ?," +
                            "creationDate = ?," +
                            "minimalPoint = ?," +
                            "description = ?," +
                            "tunedInWorks = ?," +
                            "difficulty = ?::difficulty" +
                            " FROM \"user\"" +
                            " WHERE labwork.labwork_id = ? AND labwork.user_id = \"user\".id AND \"user\".login = ?;" +
                            "UPDATE person" +
                            " SET person_name = ?," +
                            "height = ?," +
                            "passportID = ?," +
                            "hair_color = ?::color," +
                            "locationX = ?," +
                            "locationY = ?," +
                            "locationZ = ?" +
                            "FROM labwork, \"user\"" +
                            " WHERE labwork.person_id = person.person_id and labwork.labwork_id = ? AND labwork.user_id = \"user\".id AND \"user\".login = ?";
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
            statement.setString(10, username);

            Person author = labWork.getAuthor();
            statement.setString(11, author.getName());
            statement.setFloat(12, author.getHeight());
            statement.setString(13, author.getPassportID());
            statement.setString(14, author.getHairColor().name());
            statement.setInt(15, author.getLocation().getX());
            statement.setFloat(16, author.getLocation().getY());
            statement.setInt(17, author.getLocation().getZ());

            statement.setLong(18, id);

            statement.setString(19, username);

            int result = statement.executeUpdate();

            connection.createStatement().execute("COMMIT;");

            return result > 0; // Если true, значит результат не пустой и записи обновлены
        });
    }

    /**
     * Загружает коллекцию из базы данных.
     *
     * @param collectionWrapper куда загрузить коллекцию
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    @Override
    public void loadCollectionFromDatabase(CollectionWrapper collectionWrapper) throws DatabaseException {
        handleQuery((connection -> {
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
     * Получает пароль по имени пользователя.
     *
     * @param username имя пользователя
     * @return пароль (хешированный)
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    @Override
    public String getPassword(String username) throws DatabaseException {
        return this.handleQuery((Connection connection) -> {
            String query = "SELECT (\"password\")" +
                    " FROM \"user\"" +
                    " WHERE \"user\".login = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getString("password");
            }
            return null;
        });
    }

    /**
     * Проверяет, существует ли пользователь.
     *
     * @param username имя пользователя
     * @return true, если существует; false, если нет
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    @Override
    public boolean doesUserExist(String username) throws DatabaseException {
        return this.<Boolean>handleQuery((Connection connection) -> {
            String query = "SELECT COUNT(*)" +
                    " FROM \"user\"" +
                    " WHERE \"user\".login = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            result.next();

            return result.getInt("count") > 0;
        });
    }

    /**
     * Добавляет пользователя в базу данных.
     *
     * @param username имя пользователя
     * @param password пароль (хешированный)
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    @Override
    public void addUser(String username, String password) throws DatabaseException {
        handleQuery((Connection connection) -> {
            String query = "INSERT INTO \"user\" (\"login\", \"password\")" +
                    "VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            statement.executeUpdate();
        });
    }

    /**
     * Удаляет все элементы, владельцем которых является пользователь.
     *
     * @param username имя пользователя
     * @return список id элементов, которые были удалены
     * @throws DatabaseException если что-то пошло не так при работе с базой данных
     */
    @Override
    public List<Long> clear(String username) throws DatabaseException {
        return handleQuery((Connection connection) -> {
            String query = "DELETE FROM person" +
                    " USING labwork, \"user\"" +
                    "WHERE person.person_id = labwork.person_id AND labwork.user_id = \"user\".id AND \"user\".login = ?" +
                    " RETURNING labwork.labwork_id;";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            ArrayList<Long> ids = new ArrayList<>();

            while (result.next()) {
                ids.add(result.getLong("labwork_id"));
            }
            return ids;
        });
    }
}

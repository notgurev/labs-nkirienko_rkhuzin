package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.exceptions.DatabaseException;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.api.ResponseType;
import se1_prog_lab.server.interfaces.AuthManager;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.DatabaseManager;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.lang.String.join;
import static se1_prog_lab.util.AuthStrings.*;
import static se1_prog_lab.util.BetterStrings.*;

/**
 * Ресивер для серверных команд (см. паттерн "Команда").
 */
@Singleton
public class ServerCommandReceiverImpl implements ServerCommandReceiver {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());
    private final CollectionWrapper collectionWrapper;
    private final AuthManager authManager;
    private final DatabaseManager databaseManager;

    @Inject
    public ServerCommandReceiverImpl(CollectionWrapper collectionWrapper, AuthManager authManager, DatabaseManager databaseManager) {
        this.collectionWrapper = collectionWrapper;
        this.authManager = authManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public void loadCollectionFromDatabase() {
        try {
            logger.info("Загружаем коллекцию из базы данных");
            databaseManager.loadCollectionFromDatabase(collectionWrapper);
        } catch (DatabaseException e) {
            logger.severe("Произошла ошибка при попытке загрузить коллекцию из базы данных: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public synchronized Response add(LabWork labWork, AuthData authData) {
        try {
            logger.info("Добавляем элемент в коллекцию");
            Long id = databaseManager.addElement(labWork, authData.getUsername());
            collectionWrapper.add(labWork, id);
            return new Response(ResponseType.PLAIN_TEXT, yellow("Элемент успешно добавлен в коллекцию"));
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return new Response(ResponseType.PLAIN_TEXT, SERVER_ERROR.getMessage(), true);
        }
    }

    @Override
    public synchronized Response clear(AuthData authData) {
        try {
            logger.info("Очищаем коллекцию");
            List<Long> ids = databaseManager.clear(authData.getUsername());
            if (ids.size() > 0) {
                collectionWrapper.clear(ids);
                return new Response(ResponseType.PLAIN_TEXT, yellow("Элементы, на которые вы имели права, удалены из коллекции"));
            }
            return new Response(ResponseType.PLAIN_TEXT, yellow("Ваших элементов уже нет в коллекции"));
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return new Response(ResponseType.PLAIN_TEXT, SERVER_ERROR.getMessage(), true);
        }
    }

    @Override
    public synchronized Response countLessThanDescription(String description) {
        logger.info("Добавляем в ответ количество элементов, значение поля description которых меньше " + description);
        String message = yellow("Количество элементов, значение поля description которых меньше заданного: " +
                collectionWrapper.countLessThanDescription(description));
        return new Response(ResponseType.PLAIN_TEXT, message);
    }

    @Override
    public synchronized Response info() {
        logger.info("Добавляем в ответ информацию о коллекции");
        String message = multiline(
                "Тип коллекции: " + collectionWrapper.getCollectionType(),
                "Дата инициализации: " + collectionWrapper.getInitDate(),
                "Количество элементов: " + collectionWrapper.getSize()
        );
        return new Response(ResponseType.PLAIN_TEXT, message);
    }

    @Override
    public synchronized Response sort() {
        logger.info("Сортируем коллекцию");
        if (collectionWrapper.sort()) {
            return new Response(ResponseType.PLAIN_TEXT, yellow("Коллекция была успешно отсортирована в естественном порядке!"));
        } else {
            return new Response(ResponseType.PLAIN_TEXT, yellow("Коллекция пуста!"));
        }
    }

    @Override
    public synchronized Response show() {
        logger.info("Добавляем в ответ содержимое коллекции");
        return new Response(ResponseType.PLAIN_TEXT, collectionWrapper.showAll());
    }

    @Override
    public synchronized Response printUniqueTunedInWorks() {
        logger.info("Добавляем в ответ уникальные значения поля tunedInWorks");
        if (collectionWrapper.isEmpty()) {
            return new Response(ResponseType.PLAIN_TEXT, yellow("Коллекция пуста!"));
        } else {
            String message = "Уникальные значения поля tunedInWorks: " +
                    join(", ", collectionWrapper.getUniqueTunedInWorks().toString());
            return new Response(ResponseType.PLAIN_TEXT, message);
        }
    }

    @Override
    public synchronized Response filterGreaterThanMinimalPoint(int minimalPoint) {
        logger.info("Добавляем в ответ элементы, значение поля minimalPoint которых больше " + minimalPoint);
        if (collectionWrapper.isEmpty()) {
            return new Response(ResponseType.PLAIN_TEXT, yellow("Коллекция пуста!"));
        } else {
            String message = collectionWrapper.filterGreaterThanMinimalPoint(minimalPoint);
            if (message.equals("")) {
                return new Response(ResponseType.PLAIN_TEXT, yellow("Элементов, значение поля minimalPoint которых больше заданного, нет."));
            } else return new Response(ResponseType.PLAIN_TEXT, message);
        }
    }

    @Override
    public synchronized Response removeByID(long id, AuthData authData) {
        try {
            logger.info("Удаляем элемент с id " + id);
            if (databaseManager.removeById(id, authData.getUsername())) {
                collectionWrapper.removeByID(id);
                logger.info(format("Элемент с id = %d успешно удален", id));
                return new Response(ResponseType.PLAIN_TEXT, format("Элемент с id = %d успешно удален", id));
            } else {
                logger.warning("Элемента с данным id не существует в коллекции или у вас нет прав на его удаление");
                return new Response(ResponseType.PLAIN_TEXT,
                        "Элемента с данным id не существует в коллекции или у вас нет прав на его удаление",
                        true);
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return new Response(ResponseType.PLAIN_TEXT, SERVER_ERROR.getMessage(), true);
        }
    }

    @Override
    public synchronized Response insertAt(LabWork labWork, int index, AuthData authData) {
        try {
            logger.info("Вставляем элемент в ячейку с индексом " + index);
            Long id = databaseManager.addElement(labWork, authData.getUsername());
            collectionWrapper.insertAtIndex(labWork, index, id);
            return new Response(ResponseType.PLAIN_TEXT, yellow("Элемент успешно добавлен в коллекцию"));
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return new Response(ResponseType.PLAIN_TEXT, SERVER_ERROR.getMessage(), true);
        }
    }

    @Override
    public synchronized Response update(LabWork labWork, long id, AuthData authData) {
        try {
            logger.info("Обновляем элемент с id " + id);
            if (databaseManager.updateById(labWork, id, authData.getUsername())) {
                collectionWrapper.updateByID(id, labWork);
                logger.info(format("Элемент успешно заменён (id = %d)", id));
                return new Response(ResponseType.PLAIN_TEXT, yellow(format("Элемент успешно заменён (id = %d)", id)));
            } else {
                logger.info("Элемент с таким id отсутствует в коллекции либо у вас нет прав на его изменение!");
                return new Response(ResponseType.PLAIN_TEXT, red("Элемент с таким id отсутствует в коллекции либо у вас нет праав на его изменение!"));
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return new Response(ResponseType.PLAIN_TEXT, SERVER_ERROR.getMessage(), true);
        }
    }

    @Override
    public Response register(AuthData authData) {
        try {
            if (!authManager.doesUserExist(authData.getUsername())) {
                authManager.register(authData);
                logger.info(format("Зарегистрирован пользователь с именем: %s", authData.getUsername()));
                return new Response(ResponseType.AUTH_STATUS, REGISTRATION_SUCCESSFUL);
            } else {
                logger.warning(format("Имя пользователя %s уже занято", authData.getUsername()));
                return new Response(ResponseType.AUTH_STATUS, USERNAME_TAKEN, true);
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return new Response(ResponseType.PLAIN_TEXT, SERVER_ERROR.getMessage(), true);
        }
    }

    @Override
    public Response login(AuthData authData) {
        try {
            if (authManager.checkAuth(authData)) {
                logger.info(format("Авторизовался пользователь с именем: %s", authData.getUsername()));
                return new Response(ResponseType.AUTH_STATUS, LOGIN_SUCCESSFUL);
            } else {
                logger.info(format("Пользователь с именем: %s не смог авторизоваться", authData.getUsername()));
                return new Response(ResponseType.AUTH_STATUS, INCORRECT_LOGIN_DATA, true);
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return new Response(ResponseType.PLAIN_TEXT, SERVER_ERROR.getMessage(), true);
        }
    }
}

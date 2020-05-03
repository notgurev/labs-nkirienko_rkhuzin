package se1_prog_lab.server;

import com.google.inject.Inject;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.AuthManager;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

import java.util.logging.Logger;

import static java.lang.String.format;
import static java.lang.String.join;
import static se1_prog_lab.util.AuthStrings.*;
import static se1_prog_lab.util.BetterStrings.*;

/**
 * Ресивер для серверных команд (см. паттерн "Команда").
 */
public class ServerCommandReceiverImpl implements ServerCommandReceiver {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());
    private final CollectionWrapper collectionWrapper;
    private final AuthManager authManager;

    @Inject
    public ServerCommandReceiverImpl(CollectionWrapper collectionWrapper, AuthManager authManager) {
        this.collectionWrapper = collectionWrapper;
        this.authManager = authManager;
    }

    @Override
    public synchronized String add(LabWork labWork) {
        logger.info("Добавляем элемент в коллекцию");
        long id = collectionWrapper.add(labWork);
        return coloredYellow(format("Элемент успешно добавлен в коллекцию (id = %d).", id));
    }

    @Override
    public synchronized String clear() {
        logger.info("Очищаем коллекцию");
        collectionWrapper.clear();
        return coloredYellow("Элементы, на которые вы имели права, удалены из коллекции");
    }

    @Override
    public synchronized String countLessThanDescription(String description) {
        logger.info("Добавляем в ответ количество элементов, значение поля description которых меньше " + description);
        return coloredYellow("Количество элементов, значение поля description которых меньше заданного: " +
                collectionWrapper.countLessThanDescription(description));
    }

    @Override
    public synchronized String info() {
        logger.info("Добавляем в ответ информацию о коллекции");
        return multiline(
                "Тип коллекции: " + collectionWrapper.getCollectionType(),
                "Дата инициализации: " + collectionWrapper.getInitDate(),
                "Количество элементов: " + collectionWrapper.getSize()
        );
    }

    @Override
    public synchronized String sort() {
        logger.info("Сортируем коллекцию");
        if (collectionWrapper.sort()) {
            return coloredYellow("Коллекция была успешно отсортирована в естественном порядке!");
        } else {
            return coloredYellow("Коллекция пуста!");
        }
    }

    @Override
    public synchronized String show() {
        logger.info("Добавляем в ответ содержимое коллекции");
        return collectionWrapper.showAll();
    }

    @Override
    public synchronized String printUniqueTunedInWorks() {
        logger.info("Добавляем в ответ уникальные значения поля tunedInWorks");
        if (collectionWrapper.isEmpty()) {
            return coloredYellow("Коллекция пуста!");
        } else {
            return "Уникальные значения поля tunedInWorks: " +
                    join(", ", collectionWrapper.getUniqueTunedInWorks().toString());
        }
    }

    @Override
    public synchronized String filterGreaterThanMinimalPoint(int minimalPoint) {
        logger.info("Добавляем в ответ элементы, значение поля minimalPoint которых больше " + minimalPoint);
        if (collectionWrapper.isEmpty()) {
            return coloredYellow("Коллекция пуста!");
        } else {
            String response = collectionWrapper.filterGreaterThanMinimalPoint(minimalPoint);
            if (response.equals("")) {
                return coloredYellow("Элементов, значение поля minimalPoint которых больше заданного, нет.");
            } else return response;
        }
    }

    @Override
    public synchronized String removeByID(long id) {
        logger.info("Удаляем элемент с id " + id);
        if (collectionWrapper.removeByID(id)) {
            logger.info(format("Элемент с id = %d успешно удален", id));
            return format("Элемент с id = %d успешно удален", id);
        } else {
            logger.warning("Элемента с данным id не существует в коллекции.");
            return "Элемента с данным id не существует в коллекции.";
        }
    }

    @Override
    public synchronized String insertAt(LabWork labWork, int index) {
        logger.info("Вставляем элемент в ячейку с индексом " + index);
        long id = collectionWrapper.addToPosition(labWork, index);
        return coloredYellow(format("Элемент успешно добавлен в коллекцию (id = %d, index = %d).", id, index));
    }

    @Override
    public synchronized String update(LabWork labWork, long id) {
        logger.info("Обновляем элемент с id " + id);
        if (collectionWrapper.replaceByID(id, labWork)) {
            logger.info(format("Элемент успешно заменён (id = %d)", id));
            return coloredYellow(format("Элемент успешно заменён (id = %d)", id));
        } else {
            logger.info("Элемент с таким id отсутствует в коллекции!");
            return coloredRed("Элемент с таким id отсутствует в коллекции!");
        }
    }

    @Override
    public String register(AuthData authData) {
        if (!authManager.doesUserExist(authData.getUsername())) {
            authManager.register(authData);
            logger.info(format("Зарегистрирован пользователь с именем: %s", authData.getUsername()));
            return REGISTRATION_SUCCESSFUL.getMessage();
        } else {
            logger.warning(format("Имя пользователя %s уже занято", authData.getUsername()));
            return USERNAME_TAKEN.getMessage();
        }
    }

    @Override
    public String login(AuthData authData) {
        if (authManager.checkAuth(authData)) {
            logger.info(format("Авторизовался пользователь с именем: %s", authData.getUsername()));
            return LOGIN_SUCCESSFUL.getMessage();
        } else {
            logger.info(format("Пользователь с именем: %s не смог авторизоваться", authData.getUsername()));
            return INCORRECT_LOGIN_DATA.getMessage();
        }
    }
}

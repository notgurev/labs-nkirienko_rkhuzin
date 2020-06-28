package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.exceptions.DatabaseException;
import se1_prog_lab.server.interfaces.AuthManager;
import se1_prog_lab.server.interfaces.CollectionWrapper;
import se1_prog_lab.server.interfaces.DatabaseManager;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.lang.String.join;
import static se1_prog_lab.shared.api.AuthStatus.*;
import static se1_prog_lab.shared.api.Response.plainText;
import static se1_prog_lab.shared.api.Response.serverError;
import static se1_prog_lab.shared.api.ResponseType.*;
import static se1_prog_lab.shared.util.StringUtils.multiline;

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
    public synchronized Response add(LabWork labWork, AuthData authData, ResourceBundle r) {
        try {
            logger.info("Добавляем элемент в коллекцию");
            Long id = databaseManager.addElement(labWork, authData.getUsername());
            labWork.setOwner(authData.getUsername());
            collectionWrapper.add(labWork, id);
            return plainText(r.getString("ServerCommandReceiverImpl.add.successfully_added"));
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return serverError(r);
        }
    }

    @Override
    public synchronized Response clear(AuthData authData, ResourceBundle r) {
        try {
            logger.info("Очищаем коллекцию");
            List<Long> ids = databaseManager.clear(authData.getUsername());
            if (ids.size() > 0) {
                collectionWrapper.clear(ids);
                return plainText(r.getString("ServerCommandReceiverImpl.clear.successfully_removed"));
            }
            return plainText(r.getString("ServerCommandReceiverImpl.clear.has_no_owned_by_user"));
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return serverError(r);
        }
    }

    @Override
    public synchronized Response countLessThanDescription(String description, ResourceBundle r) {
        logger.info("Добавляем в ответ количество элементов, значение поля description которых меньше " + description);
        String message = r.getString("ServerCommandReceiverImpl.cltd") +
                collectionWrapper.countLessThanDescription(description);
        return plainText(message);
    }

    @Override
    public synchronized Response info(ResourceBundle r) {
        logger.info("Добавляем в ответ информацию о коллекции");
        String message = multiline(
                r.getString("ServerCommandReceiverImpl.info.collection_type") + collectionWrapper.getCollectionType(),
                r.getString("ServerCommandReceiverImpl.info.init_date") + collectionWrapper.getInitDate(),
                r.getString("ServerCommandReceiverImpl.info.size") + collectionWrapper.getSize()
        );
        return plainText(message);
    }

    @Override
    public synchronized Response sort(ResourceBundle r) {
        logger.info("Сортируем коллекцию");
        if (collectionWrapper.sort()) {
            return plainText(r.getString("ServerCommandReceiverImpl.sort.successful"));
        } else {
            return plainText(r.getString("ServerCommandReceiverImpl.collection_empty"));
        }
    }

    @Override
    public synchronized Response printUniqueTunedInWorks(ResourceBundle r) {
        logger.info("Добавляем в ответ уникальные значения поля tunedInWorks");
        if (collectionWrapper.isEmpty()) {
            return Response.plainText(r.getString("ServerCommandReceiverImpl.sort.empty"));
        } else {
            String message = r.getString("ServerCommandReceiverImpl.putiw") +
                    join(", ", collectionWrapper.getUniqueTunedInWorks().toString());
            return plainText(message);
        }
    }

    @Override
    public synchronized Response removeByID(long id, AuthData authData, ResourceBundle r) {
        try {
            logger.info("Удаляем элемент с id " + id);
            if (databaseManager.removeById(id, authData.getUsername())) {
                collectionWrapper.removeByID(id);
                logger.info(format("Элемент с id = %d успешно удален", id));
                return plainText(r.getString("ServerCommandReceiverImpl.remove.successful") + id);
            } else {
                logger.warning("Элемента с данным id не существует в коллекции или у вас нет прав на его удаление");
                return new Response(PLAIN_TEXT,
                        r.getString("ServerCommandReceiverImpl.remove.no_rights_or_such_element"),
                        true);
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return serverError(r);
        }
    }

    @Override
    public synchronized Response update(LabWork labWork, long id, AuthData authData, ResourceBundle r) {
        try {
            logger.info("Обновляем элемент с id " + id);
            if (databaseManager.updateById(labWork, id, authData.getUsername())) {
                labWork.setOwner(authData.getUsername());
                collectionWrapper.updateByID(id, labWork);
                logger.info(format("Элемент успешно заменён (id = %d)", id));
                return plainText(r.getString("ServerCommandReceiverImpl.update.successful") + id);
            } else {
                logger.info("Элемент с таким id отсутствует в коллекции либо у вас нет прав на его изменение!");
                return plainText(r.getString("ServerCommandReceiverImpl.update.no_rights_or_such_element"));
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return serverError(r);
        }
    }

    @Override
    public synchronized Response register(AuthData authData, ResourceBundle r) {
        try {
            if (!authManager.doesUserExist(authData.getUsername())) {
                authManager.register(authData);
                logger.info(format("Зарегистрирован пользователь с именем: %s", authData.getUsername()));
                return new Response(AUTH_STATUS, REGISTRATION_SUCCESSFUL,
                        r.getString(REGISTRATION_SUCCESSFUL.getMessageLocalizationKey()), false);
            } else {
                logger.warning(format("Имя пользователя %s уже занято", authData.getUsername()));
                return new Response(AUTH_STATUS, USERNAME_TAKEN,
                        r.getString(USERNAME_TAKEN.getMessageLocalizationKey()), true);
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return serverError(r);
        }
    }

    @Override
    public synchronized Response login(AuthData authData, ResourceBundle r) {
        try {
            if (authManager.checkAuth(authData)) {
                logger.info(format("Авторизовался пользователь с именем: %s", authData.getUsername()));
                return new Response(AUTH_STATUS, LOGIN_SUCCESSFUL,
                        r.getString(LOGIN_SUCCESSFUL.getMessageLocalizationKey()), false);
            } else {
                logger.info(format("Пользователь с именем: %s не смог авторизоваться", authData.getUsername()));
                return new Response(AUTH_STATUS, INCORRECT_LOGIN_DATA,
                        r.getString(INCORRECT_LOGIN_DATA.getMessageLocalizationKey()), true);
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return serverError(r);
        }
    }

    @Override
    public synchronized Response getCollectionPage(int firstIndex, int size) {
        logger.info(format("Добавляем в ответ содержимое коллекции с индекса %d, кол-во элементов: %d", firstIndex, size));
        return new Response(LABWORK_LIST, collectionWrapper.getCollectionSlice(firstIndex, size));
    }

    @Override
    public synchronized Response insertBefore(LabWork labWork, Long id, AuthData authData, ResourceBundle r) {
        try {
            logger.info("Вставляем элемент на место элемента с id = " + id + ", остальные сдвигаем");
            Long newID = databaseManager.addElement(labWork, authData.getUsername());
            labWork.setOwner(authData.getUsername());
            collectionWrapper.insertBefore(labWork, id, newID);
            return plainText(r.getString("ServerCommandReceiverImpl.add.successfully_added"));
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            return serverError(r);
        }
    }
}

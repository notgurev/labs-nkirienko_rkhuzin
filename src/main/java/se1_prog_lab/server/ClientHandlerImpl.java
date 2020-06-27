package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.exceptions.DatabaseException;
import se1_prog_lab.server.interfaces.AuthManager;
import se1_prog_lab.server.interfaces.ClientHandler;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.CommandWrapper;
import se1_prog_lab.shared.api.EOTWrapper;
import se1_prog_lab.shared.api.Response;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import static java.lang.String.format;
import static se1_prog_lab.shared.api.ResponseType.PLAIN_TEXT;

/**
 * Класс для работы с каждым клиентом в отдельном потоке.
 */
public class ClientHandlerImpl implements ClientHandler {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());
    private final Socket clientSocket;
    private final ServerCommandReceiver serverCommandReceiver;
    private final Validator validator;
    private final AuthManager authManager;
    private final ExecutorService executorService;
    private final EOTWrapper eotWrapper;
    private ResourceBundle r;

    @Inject
    public ClientHandlerImpl(@Assisted Socket clientSocket, ServerCommandReceiver serverCommandReceiver,
                             AuthManager authManager, @Assisted ExecutorService executorService, EOTWrapper eotWrapper) {
        this.clientSocket = clientSocket;
        this.serverCommandReceiver = serverCommandReceiver;
        this.authManager = authManager;
        this.executorService = executorService;
        this.eotWrapper = eotWrapper;
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /**
     * Этот метод запускается при создании потока. Тут основная работа.
     */
    @Override
    public void run() {
        logger.info("Начата обработка запросов");
        try (OutputStream clientWriter = clientSocket.getOutputStream()) {
            logger.info("Создан clientWriter, попытка получить InputStream с clientSocket");
            InputStream clientInputStream = clientSocket.getInputStream();
            ObjectInputStream objectInput;

            while (true) {
                objectInput = new ObjectInputStream(clientInputStream);
                logger.info("Принят объект " + objectInput.getClass().getSimpleName());
                CommandWrapper commandWrapper = (CommandWrapper) objectInput.readObject();

                BasicCommand command = commandWrapper.getCommand();
                Locale locale = commandWrapper.getLocale();
                r = ResourceBundle.getBundle("localization/responses", locale);
                command.setResourceBundle(r);

                new Thread(() -> {
                    String commandName = command.getClass().getSimpleName();
                    logger.info("Создан поток для команды " + commandName);
                    Response response;
                    try {
                        if (!(command instanceof AuthCommand) && !authManager.checkAuth(commandWrapper.getAuthData())) {
                            // Если это не AuthCommand и проверка данных авторизации провалена
                            logger.info("Команда содержит некорректные данные для авторизации!");
                            response = Response.authFailed(r);
                        } else {
                            // Обычные команды
                            if (command instanceof ConstructingCommand && !validateCarriedObject(command)) {
                                // Команда с объектом
                                String message = r.getString("ClientHandlerImpl.validation_unsuccessful");
                                response = new Response(PLAIN_TEXT, message, true);
                                logger.warning("Объект не прошел валидацию, команда не будет выполнена");
                            } else {
                                // Остальные команды
                                logger.info("Начинается выполнение команды " + commandName);
                                response = command.serverExecute(serverCommandReceiver, commandWrapper.getAuthData());
                                logger.info(format("Команда %s выполнена, отправляем ответ клиенту", commandName));
                            }
                        }
                    } catch (DatabaseException e) {
                        response = Response.serverError(r);
                    }
                    Response newResponse = response;
                    executorService.submit(() -> sendToClient(clientWriter, newResponse));
                }).start();
            }
        } catch (IOException e) {
            logger.severe("Не удалось обработать запрос: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.severe("Не удалось десериализировать объект: {}" + e.getMessage());
        }
    }

    /**
     * Отправляет ответ клиенту.
     *
     * @param clientWriter writer клиенту.
     * @param message      ответ клиенту.
     */
    private void sendToClient(OutputStream clientWriter, Response message) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream)) {
            logger.info("Отправка ответа клиенту");
            objectOutputStream.writeObject(message);
            clientWriter.write(eotWrapper.wrap(byteStream.toByteArray()));
            clientWriter.flush();
        } catch (IOException e) {
            logger.severe(format("Не удалось отправить ответ клиенту: %s", e.getMessage()));
        }
    }

    /**
     * Проводит валидацию объекта, который приносят команды-наследники ConstructingCommand.
     *
     * @param command команда.
     * @return true, если валидация пройдена успешно; false, если нет.
     */
    private boolean validateCarriedObject(Command command) {
        LabWork carriedObject = ((ConstructingCommand) command).getCarriedObject();
        Set<ConstraintViolation<LabWork>> violations = validator.validate(carriedObject);
        if (violations.isEmpty()) {
            logger.fine("Валидация объекта пройдена успешно");
            return true;
        } else {
            logger.warning("Объект не прошел валидацию, команда не будет выполнена.");
            logger.warning("Ошибки: " + violations.toString());
            return false;
        }
    }
}

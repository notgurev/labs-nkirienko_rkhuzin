package se1_prog_lab.server;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.AuthManager;
import se1_prog_lab.server.interfaces.ClientHandler;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.interfaces.EOTWrapper;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import static java.lang.String.format;
import static se1_prog_lab.util.AuthStrings.AUTH_FAILED;

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

    @Inject
    private EOTWrapper eotWrapper;

    @Inject
    public ClientHandlerImpl(@Assisted Socket clientSocket, ServerCommandReceiver serverCommandReceiver,
                             AuthManager authManager, @Assisted ExecutorService executorService) {
        this.clientSocket = clientSocket;
        this.serverCommandReceiver = serverCommandReceiver;
        this.authManager = authManager;
        this.executorService = executorService;
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /**
     * Этот метод запускается при создании потока. Тут основная работа.
     */
    @Override
    public void run() {
        logger.info("Начата обработка запросов");
        try (BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8))) {
            logger.info("Создан clientWriter, попытка получить InputStream с clientSocket");
            InputStream clientInputStream = clientSocket.getInputStream();
            ObjectInputStream objectInput;
            Command command;

            while (true) {
                objectInput = new ObjectInputStream(clientInputStream);
                logger.info("Принят объект " + objectInput.getClass().getSimpleName());
                command = (Command) objectInput.readObject();

                Command finalCommand = command;
                new Thread(() -> {
                    logger.info("Создан поток для команды " + finalCommand.getClass().getSimpleName());
                    String response;
                    if (!(finalCommand instanceof AuthCommand || authManager.checkAuth(finalCommand.getAuthData()))) {
                        logger.info("Команда содержит некорректные данные для авторизации!");
                        response = AUTH_FAILED.getMessage();
                    } else {
                        if (finalCommand instanceof ConstructingCommand && !validateCarriedObject(finalCommand)) {
                            response = "Объект не прошел валидацию, команда не будет выполнена.";
                            logger.warning("Объект не прошел валидацию, команда не будет выполнена.");
                        } else {
                            logger.info("Начинается выполнение команды " + finalCommand.getClass().getSimpleName());
                            response = finalCommand.serverExecute(serverCommandReceiver);
                            logger.info(format("Команда %s выполнена, отправляем ответ клиенту", finalCommand.getClass().getSimpleName()));
                        }
                    }
                    executorService.submit(() -> sendToClient(clientWriter, response));
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
    private void sendToClient(BufferedWriter clientWriter, String message) {
        try {
            logger.info("Отправка ответа клиенту");
            clientWriter.write(eotWrapper.wrap(message));
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

package se1_prog_lab.server;

import com.google.inject.*;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Singleton
public class ServerApp implements Server {
    private final ResponseBuilder responseBuilder;
    private final ServerCommandReceiver serverCommandReceiver;
    private ServerSocket serverSocket;
    @Inject
    private EOTWrapper eotWrapper;
    private static final Logger logger;
    private final int PORT = 6006;
    private final Validator validator;

    @Inject
    public ServerApp(ResponseBuilder responseBuilder, ServerCommandReceiver serverCommandReceiver) {
        this.serverCommandReceiver = serverCommandReceiver;
        this.responseBuilder = responseBuilder;

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    static {
        try {
            LogManager.getLogManager()
                    .readConfiguration(ServerApp.class.getClassLoader().getResourceAsStream("logger.properties"));
        } catch (NullPointerException | IOException e) {
            System.out.println("Не удалось загрузить конфиг логгера. Запуск в дефолтном режиме");
        }
        logger = Logger.getLogger(ServerApp.class.getName());
    }

    public static void main(String[] args) {
        logger.info("Инициализация сервера");
        Injector injector = Guice.createInjector(new ServerModule());
        Server serverApp = injector.getInstance(Server.class);
        try {
            serverApp.start();
        } catch (IOException e) {
            logger.info("С сервером что-то пошло не так:\n" + e.getMessage());
        }
    }

    @Override
    public void start() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        handleRequests();
    }

    private void sendResponseToClient(BufferedWriter clientWriter) throws IOException {
        logger.info("Отправка ответа клиенту");
        clientWriter.write(eotWrapper.wrap(responseBuilder.getResponse()));
        clientWriter.flush();
        responseBuilder.clearResponse();
    }

    private void handleRequests() {
        logger.info("Начата обработка запросов");
        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8))) {
                logger.info("Принят clientSocket, создан clientWriter");

                InputStream clientInputStream = clientSocket.getInputStream();
                ObjectInputStream objectInput;
                Command command;

                while (true) {
                    objectInput = new ObjectInputStream(clientInputStream);
                    logger.info("Принят объект " + objectInput.getClass().getSimpleName());
                    command = (Command) objectInput.readObject();

                    if (command instanceof ConstructingCommand && !validateCarriedObject(command)) {
                        responseBuilder.addLineToResponse("Объект не прошел валидацию, команда не будет выполнена.");
                        sendResponseToClient(clientWriter);
                    } else {
                        logger.info("Начинается выполнение команды " + command.getClass().getSimpleName());
                        command.serverExecute(serverCommandReceiver);
                        logger.info("Команда выполнена");
                        sendResponseToClient(clientWriter);
                        logger.info("Отправлен ответ на команду " + command.getClass().getSimpleName());
                    }
                }
            } catch (IOException e) {
                logger.severe("Не удалось обработать запрос: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                logger.severe("Не удалось десериализировать объект: {}" + e.getMessage());
            }
        }
    }

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

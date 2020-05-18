package se1_prog_lab.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import se1_prog_lab.server.interfaces.ClientHandlerFactory;
import se1_prog_lab.server.interfaces.Server;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.lang.String.format;

@Singleton
public class ServerApp implements Server {
    private static final Logger logger;
    private static final int THREADS_IN_POOL = 8;

    static {
        try {
            LogManager.getLogManager()
                    .readConfiguration(ServerApp.class.getClassLoader().getResourceAsStream("logger.properties"));
        } catch (NullPointerException | IOException e) {
            System.out.println("Не удалось загрузить конфиг логгера. Запуск в дефолтном режиме");
        }
        logger = Logger.getLogger(ServerApp.class.getName());
    }

    private final int PORT = 6006;
    private final ClientHandlerFactory clientHandlerFactory;
    private final ServerCommandReceiver serverCommandReceiver;

    @Inject
    public ServerApp(ClientHandlerFactory clientHandlerFactory, ServerCommandReceiver serverCommandReceiver) {
        this.clientHandlerFactory = clientHandlerFactory;
        this.serverCommandReceiver = serverCommandReceiver;
    }

    public static void main(String[] args) {
        logger.info("Инициализация сервера");
        Injector injector = Guice.createInjector(new ServerModule());
        Server serverApp = injector.getInstance(Server.class);
        serverApp.start();
    }

    @Override
    public void start() {
        try {
            logger.info("Создание сокета с портом " + PORT);
            ServerSocket serverSocket = new ServerSocket(PORT);

            logger.info("Создаем пул потоков для обработки ответов");
            ExecutorService executorService = Executors.newFixedThreadPool(THREADS_IN_POOL);

            serverCommandReceiver.loadCollectionFromDatabase();

            Socket clientSocket;
            while (true) {
                try {
                    logger.info("Попытка установить соединение");
                    clientSocket = serverSocket.accept();
                    logger.info("Соединение установлено, создаем новый поток ClientHandler");
                    new Thread(clientHandlerFactory.create(clientSocket, executorService)).start();
                } catch (IOException e) {
                    logger.warning("Не удалось установить соединение: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.severe(format("Не удалось создать сокет, порт %d, ошибка: %s", PORT, e.getMessage()));
        }
    }
}

package se1_prog_lab.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.interfaces.ServerIO;
import se1_prog_lab.util.AuthData;
import se1_prog_lab.util.ByteArrays;
import se1_prog_lab.util.interfaces.EOTWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static se1_prog_lab.util.AuthStrings.LOGIN_SUCCESSFUL;
import static se1_prog_lab.util.AuthStrings.REGISTRATION_SUCCESSFUL;
import static se1_prog_lab.util.BetterStrings.red;
import static se1_prog_lab.util.BetterStrings.yellow;

/**
 * Класс для взаимодействия с сервером.
 */
@Singleton
public class MyServerIO implements ServerIO {
    /**
     * Максимальное количество попыток переподключения к серверу.
     */
    private static final int MAX_TRIES = 3;
    /**
     * Размер буфера по умолчанию.
     */
    private final static int DEFAULT_BUFFER_CAPACITY = 1024;
    private final static int PORT = 6006;
    private final static String HOST = "localhost";
    private final Scanner consoleScanner;
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
    private final EOTWrapper eotWrapper;
    private AuthData authData;

    @Inject
    public MyServerIO(Scanner consoleScanner, EOTWrapper eotWrapper) {
        this.consoleScanner = consoleScanner;
        this.eotWrapper = eotWrapper;
    }

    /**
     * Метод, пытающийся установить соединение с сервером.
     * Количество попыток: MAX_TRIES.
     *
     * @return true, если удалось; false, если не удалось.
     */
    public boolean tryOpen() {
        System.out.println("Попытка соединиться с сервером...");
        String errorMessage = null;
        for (int i = 1; i <= MAX_TRIES; i++) {
            try {
                socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress(HOST, PORT));
                socketChannel.configureBlocking(false);
                System.out.println(yellow("Соединение с сервером успешно установлено"));
                return true;
            } catch (IOException e) {
                errorMessage = red("Не получилось открыть соединение: " + e.getMessage());
            }
        }
        System.out.println(errorMessage);
        return false;
    }

    private boolean isOpen() {
        return socketChannel.isOpen();
    }

    private ByteBuffer getByteBuffer() {
        if (byteBuffer == null) {
            byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
        }
        return byteBuffer;
    }

    private ByteBuffer getByteBuffer(int capacity) {
        if (byteBuffer == null || byteBuffer.capacity() != capacity) {
            byteBuffer = ByteBuffer.allocate(capacity);
        }

        return byteBuffer;
    }

    /**
     * Отправляет команду на сервер.
     *
     * @param command команда для отправки.
     * @throws IOException если что-то пошло не так.
     */
    private void sendToServer(Command command) throws IOException {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);

        System.out.printf("Команда %s готовится к отправке... ", command.getClass().getSimpleName());
        objectStream.writeObject(command);
        byte[] byteArray = byteArrayStream.toByteArray();
        ByteBuffer buffer = getByteBuffer(byteArray.length);
        buffer.clear();

        buffer.put(byteArray);
        buffer.flip();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
        System.out.print("Команда отправлена. \n");
    }

    /**
     * Принимает ответ сервера.
     *
     * @return полученная строка.
     */
    private String receiveFromServer() {
        StringBuilder stringBuilder;
        try {
            ByteBuffer buffer = getByteBuffer();
            buffer.clear();
            stringBuilder = new StringBuilder();
            ArrayList<Byte> stringBytes = new ArrayList<>();
            String stringSlice = "";
            byte[] readBytes;
            while (!eotWrapper.hasEOTSymbol(stringSlice)) {
                if (socketChannel.read(buffer) > 0) {
                    buffer.flip();
                    readBytes = new byte[buffer.limit()];
                    buffer.get(readBytes);
                    stringBytes.addAll(ByteArrays.toList(readBytes));
                    stringSlice = new String(readBytes);
                    buffer.clear();
                }
            }
            stringBuilder.append(new String(ByteArrays.toByteArray(stringBytes)));
        } catch (IOException e) {
            return red("При получении ответа возникла ошибка: " + e.getMessage());
        }
        return eotWrapper.unwrap(stringBuilder.toString());
    }

    /**
     * Отправляет команду на сервер и получает ответ, а также содержит контроль переподключений.
     * Добавляет команде актуальные данные для авторизации.
     *
     * @param command команда для отправки.
     * @return полученная строка.
     */
    @Override
    public String sendAndReceive(Command command) {
        if (command.isServerSide()) {
            if (!Objects.equals(command.getAuthData(), authData)) command.setAuthData(authData);
            while (true) {
                try {
                    if (!isOpen() && !tryOpen()) {
                        return "Команда не будет отправлена, так как не удалось открыть соединение";
                    }
                    sendToServer(command);
                    return receiveFromServer();
                } catch (IOException e) {
                    System.out.println(red("Не получилось отправить команду: " + e.getMessage()));
                    closeSocketChannel();
                    if (!tryOpen()) return "Не удалось установить соединение";
                    System.out.println("Повторная отправка команды " + command.getClass().getSimpleName());
                }
            }
        }
        return "";
    }

    /**
     * Пытается закрыть сокет.
     */
    private void closeSocketChannel() {
        try {
            socketChannel.close();
        } catch (IOException ex) {
            System.out.println("Проблемы с закрытием сокета: " + ex.getMessage());
        }
    }

    /**
     * Получает от пользователя данные для авторизации и отправляет их на сервер.
     * @return true, если авторизация успешная
     * @param authCommand команда для авторизации
     */
    @Override
    public String authorize(AuthCommand authCommand) {
        String response = sendAndReceive(authCommand);
        if (response.equals(REGISTRATION_SUCCESSFUL.getMessage()) || response.equals(LOGIN_SUCCESSFUL.getMessage())) {
            authData = authCommand.getAuthData();
        }
        return response;
    }
}

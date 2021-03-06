package se1_prog_lab.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.exceptions.EOTException;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.CommandWrapper;
import se1_prog_lab.shared.api.EOTWrapper;
import se1_prog_lab.shared.api.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Locale;
import java.util.ResourceBundle;

import static se1_prog_lab.shared.api.ResponseType.PLAIN_TEXT;

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
    private final EOTWrapper eotWrapper;
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
    private AuthData authData;
    private ResourceBundle r;

    @Inject
    public MyServerIO(EOTWrapper eotWrapper) {
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
                System.out.println("Соединение с сервером успешно установлено");
                return true;
            } catch (IOException e) {
                errorMessage = "Не получилось открыть соединение: " + e.getMessage();
            }
        }
        System.out.println(errorMessage);
        return false;
    }

    private boolean isOpen() {
        return (socketChannel != null) && socketChannel.isOpen();
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
     * @param commandWrapper обертка команды для отправки
     * @throws IOException если что-то пошло не так.
     */
    private void sendToServer(CommandWrapper commandWrapper) throws IOException {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);

        System.out.printf("Команда %s готовится к отправке... ", commandWrapper.getCommand().getClass().getSimpleName());
        objectStream.writeObject(commandWrapper);
        byte[] byteArray = byteArrayStream.toByteArray();
        ByteBuffer buffer = getByteBuffer(byteArray.length);
        buffer.clear();

        buffer.put(byteArray);
        buffer.flip();
        while (buffer.hasRemaining()) socketChannel.write(buffer);
        System.out.print("Команда отправлена. \n");
    }

    /**
     * Принимает ответ сервера.
     *
     * @return полученная строка.
     */
    private Response receiveFromServer() {
        byte[] result;
        try (ByteArrayOutputStream totalBytes = new ByteArrayOutputStream()) {
            ByteBuffer buffer = getByteBuffer();
            buffer.clear();
            byte[] readBytes;
            while (!eotWrapper.hasEOTSymbol(totalBytes.toByteArray())) {
                if (socketChannel.read(buffer) > 0) {
                    buffer.flip();
                    readBytes = new byte[buffer.limit()];
                    buffer.get(readBytes);
                    totalBytes.write(readBytes);
                    buffer.clear();
                }
            }
            result = eotWrapper.unwrap(totalBytes.toByteArray());
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(result));
            return (Response) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Response(PLAIN_TEXT,
                    r.getString("MyServerIO.error_getting_response") + e.getMessage(), true);
        } catch (EOTException e) {
            return new Response(PLAIN_TEXT, r.getString("MyServerIO.broken_message"), true);
        }
    }

    /**
     * Отправляет команду на сервер и получает ответ, а также содержит контроль переподключений.
     * Добавляет команде актуальные данные для авторизации.
     *
     * @param command команда для отправки.
     * @param locale локаль
     * @return полученная строка.
     */
    @Override
    public Response sendAndReceive(BasicCommand command, Locale locale) {
        CommandWrapper commandWrapper = new CommandWrapper(command, authData, locale);
        while (true) {
            try {
                if (!isOpen() && !tryOpen()) {
                    return new Response(PLAIN_TEXT,
                            r.getString("MyServerIO.wont_be_sent_because_cannot_connect"), true);
                }
                sendToServer(commandWrapper);
                return receiveFromServer();
            } catch (IOException e) {
                System.out.println("Не получилось отправить команду: " + e.getMessage());
                closeSocketChannel();
                if (!tryOpen()) return new Response(PLAIN_TEXT,
                        r.getString("MyServerIO.cannot_connect"), true);
                System.out.println("Повторная отправка команды " + commandWrapper.getCommand().getClass().getSimpleName());
            }
        }
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
     *
     * @param authCommand команда для авторизации
     * @param authData    данные для авторизации
     * @param locale локаль
     * @return true, если авторизация успешная
     */
    @Override
    public Response authorize(AuthCommand authCommand, AuthData authData, Locale locale) {
        this.authData = authData;
        return sendAndReceive(authCommand, locale);
    }

    @Override
    public String getUsername() {
        return authData.getUsername();
    }

    @Override
    public void changeLang(Locale locale) {
        r = ResourceBundle.getBundle("localization/responses", locale);
    }
}

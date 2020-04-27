package se1_prog_lab.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.interfaces.ServerIO;
import se1_prog_lab.server.interfaces.EOTWrapper;
import se1_prog_lab.util.ByteArrays;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static se1_prog_lab.util.BetterStrings.coloredRed;
import static se1_prog_lab.util.BetterStrings.coloredYellow;

@Singleton
public class MyServerIO implements ServerIO {
    private static final int MAX_TRIES = 3;
    private final int DEFAULT_BUFFER_CAPACITY = 1024;
    private final int PORT = 6006;
    private final String HOST = "localhost";
    private SocketChannel socketChannel;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
    @Inject
    private EOTWrapper eotWrapper;

    @Inject
    public MyServerIO() {
    }

    public boolean open() {
        System.out.println("Попытка соединиться с сервером...");
        String errorMessage = null;
        for (int i = 1; i <= MAX_TRIES; i++) {
            System.out.println("ПОПЫТКА НОМЕР "+i);
            try {
                socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress(HOST, PORT));
                socketChannel.configureBlocking(false);
                System.out.println(coloredYellow("Соединение с сервером успешно установлено"));
                return true;
            } catch (IOException e) {
                errorMessage = coloredRed("Не получилось открыть соединение: " + e.getMessage());
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

    private void sendToServer(Command command) throws IOException {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);

        System.out.println("Команда готовится к отправке: " + command.getClass().getSimpleName());
        objectStream.writeObject(command);
        byte[] byteArray = byteArrayStream.toByteArray();
        ByteBuffer buffer = getByteBuffer(byteArray.length);
        buffer.clear();

        buffer.put(byteArray);
        buffer.flip();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
        System.out.println("Команда отправлена");
    }

    private void receiveFromServer() {
        StringBuilder stringBuilder = null;
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
            System.out.println(coloredRed("При получении ответа возникла ошибка: " + e.getMessage()));
        }
        System.out.println("Получен результат команды:\n" + eotWrapper.unwrap(stringBuilder.toString()));
    }

    @Override
    public void sendAndReceive(Command command) {
        if (command.isServerSide()) {
            while (true) {
                try {
                    if (!isOpen() && !open()) {
                        System.out.println("Команда не будет отправлена, так как не удалось открыть соединение");
                        break;
                    }
                    sendToServer(command);
                    receiveFromServer();
                    break;
                } catch (IOException e) {
                    System.out.println(coloredRed("Не получилось отправить команду: " + e.getMessage()));
                    closeSocketChannel();
                    if (!open()) break;
                    System.out.println("Повторная отправка команды " + command.getClass().getSimpleName());
                }
            }
        }
    }

    private void closeSocketChannel() {
        try {
            socketChannel.close();
        } catch (IOException ex) {
            System.out.println("Проблемы с закрытием сокета: " + ex.getMessage());
        }
    }
}

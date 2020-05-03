package se1_prog_lab.server.interfaces;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

public interface ClientHandlerFactory {
    ClientHandler create(Socket clientSocket, ExecutorService executorService);
}

package se1_prog_lab.client.commands;

import se1_prog_lab.client.interfaces.ClientCommandReceiver;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

import java.io.Serializable;

public interface Command extends Serializable {
    /**
     * Метод, выполняемый на сервере
     *
     * @param serverReceiver ресивер команд, который передается сервером как аргумент
     * @param authData       данные для авторизации
     * @return ответ клиенту.
     */
    Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData);

    /**
     * Метод, выполняемый на клиенте
     *
     * @param args           аргументы команды
     * @param clientReceiver серверный ресивер команд
     * @return true, если выполнилась нормально; false, если нет
     */
    boolean clientExecute(String[] args, ClientCommandReceiver clientReceiver);

    String getHelpText();

    String getKey();
}

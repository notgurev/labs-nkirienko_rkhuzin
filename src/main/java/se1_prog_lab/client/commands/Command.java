package se1_prog_lab.client.commands;

import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

import java.io.Serializable;
import java.util.ResourceBundle;

public interface Command extends Serializable {
    /**
     * Метод, выполняемый на сервере
     *
     * @param serverReceiver ресивер команд, который передается сервером как аргумент
     * @param authData       данные для авторизации
     * @return ответ клиенту.
     */
    Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData);

    String getJournalEntry();

    boolean isCollectionChanging();

    void setResourceBundle(ResourceBundle resourceBundle);
}

package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

// todo не очень понимаю как делать, видимо встраивать в таблицу/визуализацию
public class FilterGreaterThanMinimalPoint extends BasicCommand {
    int minimalPoint;

    public FilterGreaterThanMinimalPoint(int minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.filterGreaterThanMinimalPoint(minimalPoint);
    }

    @Override
    public String getJournalEntry() {
        return "Получить элементы, значение minimalPoint которых больше заданного";
    }
}

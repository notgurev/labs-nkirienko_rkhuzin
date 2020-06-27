package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

// todo не очень понимаю как делать, видимо встраивать в таблицу/визуализацию
@Deprecated
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
        return "journal.entries.fgtmp";
    }
}

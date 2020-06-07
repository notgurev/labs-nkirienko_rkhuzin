package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

// todo норм, но используется часто
public class RemoveByID extends BasicCommand {
    long id;

    public RemoveByID(long id) {
        this.id = id;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.removeByID(id, authData);
    }

    @Override
    public String getJournalEntry() {
        return "Удалить элемент с id=" + id;
    }
}

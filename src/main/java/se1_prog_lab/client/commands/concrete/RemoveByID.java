package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

// todo норм, но используется часто
public class RemoveByID extends BasicCommand {
    long id;

    public RemoveByID(long id) {
        this.id = id;
        collectionChanging = true;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.removeByID(id, authData);
    }

    @Override
    public String getJournalEntry() {
        return "journal.entries.remove";
    }
}

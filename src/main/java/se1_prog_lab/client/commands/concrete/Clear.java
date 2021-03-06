package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

public class Clear extends BasicCommand {
    public Clear() {
        collectionChanging = true;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.clear(authData, resourceBundle);
    }

    @Override
    public String getJournalEntry() {
        return "journal.entries.clear";
    }
}

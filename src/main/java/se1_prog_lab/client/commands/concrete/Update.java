package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

public class Update extends ConstructingCommand {
    long id;

    public Update(long id, LabWork labWork) {
        this.id = id;
        carriedObject = labWork;
        collectionChanging = true;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.update(carriedObject, id, authData);
    }

    @Override
    public String getJournalEntry() {
        return "journal.entries.update";
    }
}

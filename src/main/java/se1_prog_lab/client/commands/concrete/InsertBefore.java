package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

public class InsertBefore extends ConstructingCommand {
    Long id;

    public InsertBefore(LabWork labWork, Long id) {
        carriedObject = labWork;
        this.id = id;
        collectionChanging = true;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.insertBefore(carriedObject, id, authData, resourceBundle);
    }

    @Override
    public String getJournalEntry() {
        return "journal.entries.insert_before";
    }
}

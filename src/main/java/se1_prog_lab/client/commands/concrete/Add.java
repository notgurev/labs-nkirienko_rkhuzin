package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.shared.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;

public class Add extends ConstructingCommand {
    public Add(LabWork labWork) {
        carriedObject = labWork;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.add(carriedObject, authData);
    }

    @Override
    public String getJournalEntry() {
        return "Добавить элемент";
    }
}

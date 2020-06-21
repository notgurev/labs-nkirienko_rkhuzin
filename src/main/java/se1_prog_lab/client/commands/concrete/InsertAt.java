package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.shared.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;

// todo повнимательнее
public class InsertAt extends ConstructingCommand {
    int index;

    public InsertAt(int index) {
        this.index = index;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.insertAt(carriedObject, index, authData);
    }

    @Override
    public String getJournalEntry() {
        return "Добавить элемент в позицию " + index;
    }
}

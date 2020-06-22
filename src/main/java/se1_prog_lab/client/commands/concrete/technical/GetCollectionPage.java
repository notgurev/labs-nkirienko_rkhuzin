package se1_prog_lab.client.commands.concrete.technical;

import se1_prog_lab.client.commands.NoJournalEntryCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

public class GetCollectionPage extends NoJournalEntryCommand {
    int firstIndex, size;

    public GetCollectionPage(int page, int pageSize) {
        super();
        firstIndex = page * pageSize;
        size = pageSize;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.getCollectionPage(firstIndex, size);
    }
}

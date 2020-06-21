package se1_prog_lab.client.commands.concrete.technical;

import se1_prog_lab.client.commands.NoJournalEntryCommand;
import se1_prog_lab.shared.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;

public class GetCollectionPage extends NoJournalEntryCommand {
    int firstIndex, lastIndex;

    public GetCollectionPage(int page, int pageSize) {
        super();
        firstIndex = page - 1;
        lastIndex = firstIndex + pageSize - 1;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.getCollectionPage(firstIndex, lastIndex);
    }
}

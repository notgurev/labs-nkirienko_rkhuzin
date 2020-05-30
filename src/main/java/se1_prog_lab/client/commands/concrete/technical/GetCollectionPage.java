package se1_prog_lab.client.commands.concrete.technical;

import se1_prog_lab.client.commands.ServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

public class GetCollectionPage extends ServerSideCommand {
    int firstIndex, lastIndex;

    public GetCollectionPage(int page, int pageSize) {
        super(null, null);
        firstIndex = page - 1;
        lastIndex = firstIndex + pageSize - 1;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.getCollectionPage(firstIndex, lastIndex);
    }
}

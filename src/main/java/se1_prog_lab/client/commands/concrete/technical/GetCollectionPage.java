package se1_prog_lab.client.commands.concrete.technical;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

public class GetCollectionPage extends BasicCommand {
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

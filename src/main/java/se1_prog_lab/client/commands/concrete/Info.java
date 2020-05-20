package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.api.ResponseType;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

public class Info extends ServerSideCommand {
    public Info() {
        super("info", " - вывести информацию о коллекции");
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.info();
    }
}

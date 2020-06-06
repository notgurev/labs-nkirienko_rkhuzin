package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;
// todo в окно
public class Info extends ServerSideCommand {
    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.info();
    }
}

package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.ClientCommandReceiver;
import se1_prog_lab.client.commands.ClientServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

public class CountLessThanDescription extends ClientServerSideCommand {
    String description;

    public CountLessThanDescription(String description) {
        this.description = description;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.countLessThanDescription(description);
    }

    @Override
    public boolean clientExecute(String[] args, ClientCommandReceiver clientReceiver) {
        description = String.join(" ", args);
        return true;
    }
}

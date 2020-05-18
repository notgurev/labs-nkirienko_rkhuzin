package se1_prog_lab.client.commands.concrete.technical;

import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

public class Register extends AuthCommand {
    public Register() {
        super();
    }

    @Override
    public String serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.register(authData);
    }
}

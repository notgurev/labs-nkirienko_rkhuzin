package se1_prog_lab.client.commands.concrete.technical;

import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

public class Login extends AuthCommand {
    public Login(AuthData authData) {
        super(authData);
    }

    @Override
    public String serverExecute(ServerCommandReceiver serverReceiver) {
        return serverReceiver.login(getAuthData());
    }
}

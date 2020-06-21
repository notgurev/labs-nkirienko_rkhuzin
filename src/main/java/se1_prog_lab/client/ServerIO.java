package se1_prog_lab.client;

import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.shared.api.Response;
import se1_prog_lab.shared.api.AuthData;

public interface ServerIO {
    boolean tryOpen();

    Response sendAndReceive(BasicCommand command);

    Response authorize(AuthCommand authCommand, AuthData authData);

    String getUsername();
}

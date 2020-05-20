package se1_prog_lab.client.interfaces;

import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.ClientServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.util.AuthData;

public interface ServerIO {
    boolean tryOpen();

    Response sendAndReceive(ClientServerSideCommand command);

    Response authorize(AuthCommand authCommand, AuthData authData);
}

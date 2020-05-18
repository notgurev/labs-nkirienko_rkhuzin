package se1_prog_lab.client.interfaces;

import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.ClientServerSideCommand;
import se1_prog_lab.util.AuthData;

public interface ServerIO {
    boolean tryOpen();

    String sendAndReceive(ClientServerSideCommand command);

    String authorize(AuthCommand authCommand, AuthData authData);
}

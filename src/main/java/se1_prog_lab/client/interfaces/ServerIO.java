package se1_prog_lab.client.interfaces;

import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.Command;

public interface ServerIO {
    boolean tryOpen();

    String sendAndReceive(Command command);

    String authorize(AuthCommand authCommand);
}

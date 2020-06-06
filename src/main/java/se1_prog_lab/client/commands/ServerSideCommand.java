package se1_prog_lab.client.commands;

import se1_prog_lab.client.ClientCommandReceiver;

public abstract class ServerSideCommand extends ClientServerSideCommand {
    @Override
    public final boolean clientExecute(String[] args, ClientCommandReceiver clientReceiver) {
        return true;
    }
}

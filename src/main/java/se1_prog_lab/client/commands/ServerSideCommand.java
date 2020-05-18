package se1_prog_lab.client.commands;

import se1_prog_lab.client.interfaces.ClientCommandReceiver;

public abstract class ServerSideCommand extends ClientServerSideCommand {
    public ServerSideCommand(String key, String helpText) {
        super(key, helpText);
    }

    @Override
    public final boolean clientExecute(String[] args, ClientCommandReceiver clientReceiver) {
        return true;
    }
}

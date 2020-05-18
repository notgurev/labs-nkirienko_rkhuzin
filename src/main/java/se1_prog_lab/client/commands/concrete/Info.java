package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ServerSideCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;

public class Info extends ServerSideCommand {
    public Info() {
        super("info", " - вывести информацию о коллекции");
    }

    @Override
    public String serverExecute(ServerCommandReceiver serverReceiver) {
        return serverReceiver.info();
    }
}

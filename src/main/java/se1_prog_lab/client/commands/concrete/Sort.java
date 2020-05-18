package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ServerSideCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;

public class Sort extends ServerSideCommand {
    public Sort() {
        super("sort", " - отсортировать коллекцию в естественном порядке");
    }

    @Override
    public String serverExecute(ServerCommandReceiver serverReceiver) {
        return serverReceiver.sort();
    }
}

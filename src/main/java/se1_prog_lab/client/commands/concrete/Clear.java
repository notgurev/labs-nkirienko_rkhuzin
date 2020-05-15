package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.NonValidatingRegularCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;

public class Clear extends NonValidatingRegularCommand {
    public Clear() {
        super("clear", " - очистить коллекцию");
    }

    @Override
    public String serverExecute(ServerCommandReceiver serverReceiver) {
        return serverReceiver.clear(getAuthData());
    }
}

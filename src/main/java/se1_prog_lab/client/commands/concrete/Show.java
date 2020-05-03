package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.NonValidatingRegularCommand;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;

public class Show extends NonValidatingRegularCommand {
    public Show() {
        super("show", " - вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public String serverExecute(ServerCommandReceiver serverReceiver) {
        return serverReceiver.show();
    }
}

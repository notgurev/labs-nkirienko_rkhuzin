package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;
// todo переделать, теперь это техническая команда
public class Show extends ServerSideCommand {
    public Show() {
        super("show", " - вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.show();
    }
}

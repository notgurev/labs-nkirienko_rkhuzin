package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ClientServerSideCommand;
import se1_prog_lab.client.interfaces.ClientCommandReceiver;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.api.ResponseType;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

public class CountLessThanDescription extends ClientServerSideCommand {
    String description;

    public CountLessThanDescription() {
        super("count_less_than_description", " description - вывести количество элементов, значение поля description которых меньше заданного");
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.countLessThanDescription(description);
    }

    @Override
    public boolean clientExecute(String[] args, ClientCommandReceiver clientReceiver) {
        description = String.join(" ", args);
        return true;
    }
}

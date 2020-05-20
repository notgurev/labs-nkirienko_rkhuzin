package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.client.interfaces.ClientCommandReceiver;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.api.ResponseType;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;
import se1_prog_lab.util.ElementCreator;

public class Add extends ConstructingCommand {
    public Add() {
        super("add", " - добавить новый элемент в коллекцию");
    }


    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.add(carriedObject, authData);
    }

    @Override
    public boolean clientExecute(String[] args, ClientCommandReceiver clientCommandReceiver) {
        carriedObject = ElementCreator.buildLabWork(clientCommandReceiver);
        return true;
    }
}

package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ConstructingCommand;
import se1_prog_lab.client.interfaces.ClientCommandReceiver;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.api.ResponseType;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;
import se1_prog_lab.util.ElementCreator;

public class InsertAt extends ConstructingCommand {
    int index;

    public InsertAt() {
        super("insert_at", " index - добавить новый элемент в заданную позицию");
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.insertAt(carriedObject, index, authData);
    }

    @Override
    public boolean clientExecute(String[] args, ClientCommandReceiver clientReceiver) {
        try {
            index = Integer.parseInt(args[0]);
            if (index < 0) throw new NumberFormatException();
            carriedObject = ElementCreator.buildLabWork(clientReceiver);
            return true;
        } catch (NumberFormatException e) {
            if (clientReceiver.getExecutingScripts().isEmpty())
                System.out.println("Введите корректный положительный целочисленный индекс!");
            else throw new NumberFormatException();
            return false;
        }
    }
}

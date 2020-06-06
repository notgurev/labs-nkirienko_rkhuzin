package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.ClientCommandReceiver;
import se1_prog_lab.client.commands.ClientServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;
// todo норм, но используется часто
public class RemoveByID extends ClientServerSideCommand {
    long id;

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.removeByID(id, authData);
    }

    @Override
    public boolean clientExecute(String[] args, ClientCommandReceiver clientReceiver) {
        try {
            id = Long.parseLong(args[0]);
            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (clientReceiver.getExecutingScripts().isEmpty())
                System.out.println("Пожалуйста, введите корректный числовой id в качестве аргумента.");
            else throw new NumberFormatException();
            return false;
        }
    }
}

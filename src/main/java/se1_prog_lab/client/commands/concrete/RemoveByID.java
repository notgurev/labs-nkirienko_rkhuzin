package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ClientServerSideCommand;
import se1_prog_lab.client.interfaces.ClientCommandReceiver;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;

public class RemoveByID extends ClientServerSideCommand {
    long id;

    public RemoveByID() {
        super("remove_by_id", " id - удалить элемент из коллекции по его id");
    }

    @Override
    public String serverExecute(ServerCommandReceiver serverReceiver) {
        return serverReceiver.removeByID(id, getAuthData());
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

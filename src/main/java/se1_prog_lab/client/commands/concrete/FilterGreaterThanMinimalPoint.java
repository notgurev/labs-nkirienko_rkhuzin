package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.ClientCommandReceiver;
import se1_prog_lab.client.commands.ClientServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

// todo не очень понимаю как делать, видимо встраивать в таблицу/визуализацию
public class FilterGreaterThanMinimalPoint extends ClientServerSideCommand {
    int minimalPoint;

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.filterGreaterThanMinimalPoint(minimalPoint);
    }

    @Override
    public boolean clientExecute(String[] args, ClientCommandReceiver clientReceiver) {
        try {
            minimalPoint = Integer.parseInt(args[0]);
            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (clientReceiver.getExecutingScripts().isEmpty())
                System.out.println("Пожалуйста, введите корректное число в качестве аргумента.");
            else throw new NumberFormatException();
            return false;
        }
    }
}

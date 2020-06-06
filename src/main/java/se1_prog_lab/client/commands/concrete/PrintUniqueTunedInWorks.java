package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.ServerSideCommand;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

public class PrintUniqueTunedInWorks extends ServerSideCommand {
    public PrintUniqueTunedInWorks() {
        super("print_unique_tuned_in_works", " - вывести уникальные значения поля tunedInWorks");
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.printUniqueTunedInWorks();
    }
}

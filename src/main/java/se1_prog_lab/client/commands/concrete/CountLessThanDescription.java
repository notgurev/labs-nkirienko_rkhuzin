package se1_prog_lab.client.commands.concrete;

import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.shared.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.shared.api.AuthData;

public class CountLessThanDescription extends BasicCommand {
    String description;

    public CountLessThanDescription(String description) {
        this.description = description;
    }

    @Override
    public Response serverExecute(ServerCommandReceiver serverReceiver, AuthData authData) {
        return serverReceiver.countLessThanDescription(description);
    }

    @Override
    public String getJournalEntry() {
        return "Подсчет элементов с полем description < заданного";
    }
}

package se1_prog_lab.client;

import se1_prog_lab.client.commands.AuthCommand;
import se1_prog_lab.client.commands.BasicCommand;
import se1_prog_lab.client.gui.LangChangeSubscriber;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

import java.util.Locale;

public interface ServerIO extends LangChangeSubscriber {
    boolean tryOpen();

    Response sendAndReceive(BasicCommand command, Locale locale);

    Response authorize(AuthCommand authCommand, AuthData authData, Locale locale);

    String getUsername();
}

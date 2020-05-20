package se1_prog_lab.client.commands;

import se1_prog_lab.server.api.Response;
import se1_prog_lab.server.interfaces.ServerCommandReceiver;
import se1_prog_lab.util.AuthData;

/**
 * Клиентская команда.
 * Не создает объект.
 * В clientExecute() должна выполнить необходимые действия.
 * Не отправляется на сервер.
 */
public abstract class ClientSideCommand extends BaseCommand {
    public ClientSideCommand(String key, String helpText) {
        super(key, helpText);
    }

    @Override
    public final Response serverExecute(ServerCommandReceiver clientCommandReceiver, AuthData authData) {
        return null;
    }
}

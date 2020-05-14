package se1_prog_lab.client.commands;

/**
 * Скриптовая клиентская команда.
 * В clientExecute() должна выполнить необходимые действия.
 * Не отправляется на сервер.
 */
public abstract class ScriptCommand extends ClientSideCommand {
    public ScriptCommand(String key, String helpText) {
        super(key, helpText);
    }
}

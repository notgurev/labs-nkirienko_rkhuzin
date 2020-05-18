package se1_prog_lab.client.commands;

/**
 * Обычная клиент-серверная команда.
 * Не создает объект.
 * В clientExecute() должна выполнить валидацию, если это необходимо.
 * В execute() выполняет необходимые действия.
 * Отправляется на сервер.
 */
public abstract class ClientServerSideCommand extends BaseCommand {
    public ClientServerSideCommand(String key, String helpText) {
        super(key, helpText);
    }
}

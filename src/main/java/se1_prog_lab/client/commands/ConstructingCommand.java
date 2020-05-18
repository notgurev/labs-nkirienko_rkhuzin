package se1_prog_lab.client.commands;

import se1_prog_lab.collection.LabWork;

/**
 * Клиент-серверная команда, создающая объект.
 * Создает объект.
 * В clientExecute() должна создать объект.
 * В execute() выполняет необходимые действия.
 * Отправляется на сервер.
 */
public abstract class ConstructingCommand extends ClientServerSideCommand {
    protected LabWork carriedObject;

    public ConstructingCommand(String key, String helpText) {
        super(key, helpText);
    }

    public LabWork getCarriedObject() {
        return carriedObject;
    }
}

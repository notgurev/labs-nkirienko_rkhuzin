package se1_prog_lab.client.commands;

import se1_prog_lab.collection.LabWork;

public abstract class ConstructingCommand extends BasicCommand {
    protected LabWork carriedObject;

    public LabWork getCarriedObject() {
        return carriedObject;
    }
}

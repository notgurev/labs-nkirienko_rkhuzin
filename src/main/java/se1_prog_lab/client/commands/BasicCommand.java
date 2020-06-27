package se1_prog_lab.client.commands;

public abstract class BasicCommand implements Command {
    protected boolean collectionChanging = false;

    @Override
    public boolean isCollectionChanging() {
        return collectionChanging;
    }
}

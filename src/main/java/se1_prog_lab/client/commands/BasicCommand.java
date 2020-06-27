package se1_prog_lab.client.commands;

import java.util.ResourceBundle;

public abstract class BasicCommand implements Command {
    protected boolean collectionChanging = false;
    protected ResourceBundle resourceBundle;

    @Override
    public boolean isCollectionChanging() {
        return collectionChanging;
    }

    @Override
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}

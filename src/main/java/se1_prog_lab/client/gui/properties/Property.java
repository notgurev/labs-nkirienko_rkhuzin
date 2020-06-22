package se1_prog_lab.client.gui.properties;

import javax.validation.Validator;

public abstract class Property {
    protected final String propertyName;
    protected final String text;

    public Property(String propertyName, String text) {
        this.propertyName = propertyName;
        this.text = text;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getLabelText() {
        return text;
    }

    public abstract boolean validateValue(Validator validator);
}

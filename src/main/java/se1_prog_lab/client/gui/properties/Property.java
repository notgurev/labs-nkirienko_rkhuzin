package se1_prog_lab.client.gui.properties;

import se1_prog_lab.collection.LabWorkParams;

import javax.validation.Validator;
import java.lang.reflect.Field;

public abstract class Property {
    protected Field labWorkField;
    protected final String propertyName;
    protected final String text;
    protected LabWorkParams labWorkParams;

    public Property(String propertyName, String text) {
        this.propertyName = propertyName;
        this.text = text;
        try {
            labWorkField = LabWorkParams.class.getDeclaredField(propertyName);
            labWorkField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void setLabWorkParams(LabWorkParams labWorkParams) {
        this.labWorkParams = labWorkParams;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getLabelText() {
        return text;
    }

    public abstract boolean validateValue(Validator validator);

    public abstract void fillInputFromParams();

    public abstract void clear();
}

package se1_prog_lab.client.gui.properties;

import javax.swing.*;

public abstract class PropertyField extends Property {
    protected final JTextField field = new JTextField();

    public PropertyField(String propertyName, String text) {
        super(propertyName, text);
    }

    public JTextField getField() {
        return field;
    }

    @Override
    public void fillInputFromParams() {
        try {
            field.setText(labWorkField.get(labWorkParams).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

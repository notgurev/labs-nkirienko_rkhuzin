package se1_prog_lab.client.gui.properties;

import javax.swing.*;

public abstract class EnumPropertyBox extends Property {
    protected JComboBox<String> comboBox;

    public EnumPropertyBox(String propertyName, String text) {
        super(propertyName, text);
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }
}

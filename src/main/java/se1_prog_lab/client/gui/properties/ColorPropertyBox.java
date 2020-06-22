package se1_prog_lab.client.gui.properties;

import se1_prog_lab.collection.Color;
import se1_prog_lab.collection.LabWorkParams;
import se1_prog_lab.shared.util.EnumUtils;

import javax.swing.*;
import javax.validation.Validator;
import java.util.Objects;

public class ColorPropertyBox extends EnumPropertyBox {
    public ColorPropertyBox(String propertyName, String text) {
        super(propertyName, text);
        comboBox = new JComboBox<>(EnumUtils.getNames(Color.class));
        comboBox.setEditable(false);
    }

    @Override
    public boolean validateValue(Validator validator) {
        try {
            Color input = Color.valueOf(Objects.requireNonNull(comboBox.getSelectedItem()).toString());
            validator.validateValue(LabWorkParams.class, propertyName, input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

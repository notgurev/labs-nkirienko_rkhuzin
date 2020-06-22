package se1_prog_lab.client.gui.properties;

import se1_prog_lab.collection.Difficulty;
import se1_prog_lab.collection.LabWorkParams;
import se1_prog_lab.shared.util.EnumUtils;

import javax.swing.*;
import javax.validation.Validator;
import java.util.Objects;

public class DifficultyPropertyBox extends EnumPropertyBox {
    public DifficultyPropertyBox(String propertyName, String text) {
        super(propertyName, text);
        comboBox = new JComboBox<>(EnumUtils.getNames(Difficulty.class));
        comboBox.addItem("-");
        comboBox.setEditable(false);
    }

    @Override
    public boolean validateValue(Validator validator) {
        try {
            String item = Objects.requireNonNull(comboBox.getSelectedItem()).toString();
            Difficulty input = item.equals("-") ? null :
                    Difficulty.valueOf(Objects.requireNonNull(comboBox.getSelectedItem()).toString());
            validator.validateValue(LabWorkParams.class, propertyName, input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
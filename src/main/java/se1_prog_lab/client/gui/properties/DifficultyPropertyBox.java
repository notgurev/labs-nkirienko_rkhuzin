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
        comboBox.setEditable(false);
    }

    @Override
    public boolean validateValue(Validator validator) {
        try {
            Difficulty input = Difficulty.valueOf(Objects.requireNonNull(comboBox.getSelectedItem()).toString());
            if (validator.validateValue(LabWorkParams.class, propertyName, input).isEmpty()) {
                labWorkParams.setDifficulty(input);
                return true;
            } else return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void fillInputFromParams() {
        try {
            comboBox.setSelectedIndex(((Difficulty) labWorkField.get(labWorkParams)).ordinal());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
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
            if (labWorkField.get(labWorkParams) == null) comboBox.setSelectedIndex(4);
            comboBox.setSelectedIndex(((Difficulty) labWorkField.get(labWorkParams)).ordinal());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
package se1_prog_lab.client.gui.properties;

import se1_prog_lab.collection.LabWorkParams;

import javax.validation.Validator;

public class IntegerPropertyField extends PropertyField {
    public IntegerPropertyField(String propertyName, String text) {
        super(propertyName, text);
    }

    @Override
    public boolean validateValue(Validator validator) {
        try {
            Integer input = field.getText().equals("") ? null : Integer.parseInt(field.getText());
            if (validator.validateValue(LabWorkParams.class, propertyName, input).isEmpty()) {
                labWorkField.set(labWorkParams, input);
                return true;
            } else return false;
        } catch (NumberFormatException | IllegalAccessException e) {
            return false;
        }
    }
}

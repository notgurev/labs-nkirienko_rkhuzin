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
            return validator.validateValue(LabWorkParams.class, propertyName, input).isEmpty();
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

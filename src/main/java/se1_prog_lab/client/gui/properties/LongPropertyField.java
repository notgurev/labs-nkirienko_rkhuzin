package se1_prog_lab.client.gui.properties;

import se1_prog_lab.collection.LabWorkParams;

import javax.validation.Validator;

public class LongPropertyField extends PropertyField {
    public LongPropertyField(String propertyName, String text) {
        super(propertyName, text);
    }

    @Override
    public boolean validateValue(Validator validator) {
        try {
            Long input = field.getText().equals("") ? null : Long.parseLong(field.getText());
            return validator.validateValue(LabWorkParams.class, propertyName, input).isEmpty();
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

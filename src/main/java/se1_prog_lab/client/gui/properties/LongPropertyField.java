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
            if (validator.validateValue(LabWorkParams.class, propertyName, input).isEmpty()) {
                labWorkField.set(labWorkParams, input);
                return true;
            } else return false;
        } catch (NumberFormatException | IllegalAccessException e) {
            return false;
        }
    }
}

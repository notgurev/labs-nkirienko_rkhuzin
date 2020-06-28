package se1_prog_lab.client.gui.properties;

import se1_prog_lab.collection.LabWorkParams;

import javax.validation.Validator;

public class FloatPropertyField extends PropertyField {
    public FloatPropertyField(String propertyName, String text) {
        super(propertyName, text);
    }

    @Override
    public boolean validateValue(Validator validator) {
        try {
            Float input = field.getText().equals("") ? null : Float.parseFloat(field.getText());
            if (validator.validateValue(LabWorkParams.class, propertyName, input).isEmpty()) {
                labWorkField.set(labWorkParams, input);
                return true;
            } else return false;
        } catch (NumberFormatException | IllegalAccessException e) {
            return false;
        }
    }
}

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
            return validator.validateValue(LabWorkParams.class, propertyName, input).isEmpty();
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

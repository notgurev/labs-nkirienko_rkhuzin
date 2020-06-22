package se1_prog_lab.client.gui.properties;

import se1_prog_lab.collection.LabWorkParams;

import javax.validation.Validator;

public class StringPropertyField extends PropertyField {
    public StringPropertyField(String propertyName, String text) {
        super(propertyName, text);
    }

    @Override
    public boolean validateValue(Validator validator) {
        String input = field.getText();
        if (input.equals("")) input = null;
        return validator.validateValue(LabWorkParams.class, propertyName, input).isEmpty();
    }
}

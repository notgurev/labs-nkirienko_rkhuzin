package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.client.commands.concrete.Add;
import se1_prog_lab.client.commands.concrete.InsertBefore;
import se1_prog_lab.client.commands.concrete.RemoveByID;
import se1_prog_lab.client.commands.concrete.Update;
import se1_prog_lab.client.gui.properties.*;
import se1_prog_lab.collection.LabWork;
import se1_prog_lab.collection.LabWorkParams;
import se1_prog_lab.shared.util.ElementCreator;

import javax.swing.*;
import javax.validation.Validation;
import javax.validation.Validator;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.String.format;

public class ConstructingFrame extends JFrame {
    private final ClientCore controller;
    private final JPanel panel;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final Map<String, Property> properties = new LinkedHashMap<>();
    private final LabWorkParams labWorkParams;
    private final ResourceBundle r;
    private boolean editingMode = false;

    public ConstructingFrame(ClientCore controller, LabWork labWork) throws HeadlessException {
        this.controller = controller;
        r = ResourceBundle.getBundle("localization/gui", controller.getLocale());
        if (labWork == null) {
            labWorkParams = new LabWorkParams();
        } else {
            labWorkParams = labWork.toParams();
            editingMode = true;
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(400, 100));

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        panel.setLayout(new GridLayout(0, 2));

        addProperties(
                new StringPropertyField("name", r.getString("ConstructingFrame.property.name")),
                new LongPropertyField("coordinateX", r.getString("ConstructingFrame.property.coordinateX")),
                new FloatPropertyField("coordinateY", r.getString("ConstructingFrame.property.coordinateY")),
                new IntegerPropertyField("minimalPoint", r.getString("ConstructingFrame.property.minimalPoint")),
                new StringPropertyField("description", r.getString("ConstructingFrame.property.description")),
                new IntegerPropertyField("tunedInWorks", r.getString("ConstructingFrame.property.tunedInWorks")),
                new DifficultyPropertyBox("difficulty", r.getString("ConstructingFrame.property.difficulty")),
                new StringPropertyField("authorName", r.getString("ConstructingFrame.property.authorName")),
                new FloatPropertyField("authorHeight", r.getString("ConstructingFrame.property.authorHeight")),
                new StringPropertyField("authorPassportID", r.getString("ConstructingFrame.property.authorPassportID")),
                new ColorPropertyBox("authorHairColor", r.getString("ConstructingFrame.property.authorHairColor")),
                new IntegerPropertyField("authorLocationX", r.getString("ConstructingFrame.property.authorLocationX")),
                new FloatPropertyField("authorLocationY", r.getString("ConstructingFrame.property.authorLocationY")),
                new IntegerPropertyField("authorLocationZ", r.getString("ConstructingFrame.property.authorLocationZ"))
        );

        // Кнопка проверки полей
        JButton checkButton = new JButton(r.getString("ConstructingFrame.buttons.check"));
        checkButton.addActionListener(e -> {
            if (checkProperties()) controller.simpleAlert(r.getString("ConstructingFrame.alerts.correct_input"));
        });
        panel.add(checkButton);

        // Кнопка создания экземпляра
        JButton createButton = new JButton(r.getString("ConstructingFrame.buttons.create"));
        createButton.addActionListener(e -> {
            if (checkProperties()) {
                controller.executeServerCommand(new Add(createLabWork()));
            }
        });
        panel.add(createButton);

        if (editingMode) {
            // Кнопка изменения
            JButton updateButton = new JButton(r.getString("ConstructingFrame.buttons.change_aka_update"));
            updateButton.addActionListener(e -> {
                if (checkProperties()) {
                    assert labWork != null;
                    controller.executeServerCommand(new Update(labWork.getId(), createLabWork()));
                    dispose();
                }
            });
            panel.add(updateButton);

            // Кнопка удаления
            JButton removeButton = new JButton(r.getString("ConstructingFrame.buttons.remove"));
            removeButton.addActionListener(e -> {
                assert labWork != null;
                controller.executeServerCommand(new RemoveByID(labWork.getId()));
                dispose();
            });
            panel.add(removeButton);

            // Кнопка вставки на место
            JButton insertButton = new JButton(r.getString("ConstructingFrame.buttons.insert"));
            insertButton.addActionListener(e -> {
                assert labWork != null;
                controller.executeServerCommand(new InsertBefore(createLabWork(), labWork.getId()));
                dispose();
            });
            panel.add(insertButton);
        }

        // Кнопка очистки полей
        JButton clearButton = new JButton(r.getString("ConstructingFrame.buttons.clear_fields"));
        clearButton.addActionListener(e -> properties.values().forEach(Property::clear));
        panel.add(clearButton);

        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private LabWork createLabWork() {
        return ElementCreator.createLabWork(labWorkParams);
    }

    private boolean checkProperties() {
        for (Property property : properties.values()) {
            if (!property.validateValue(validator)) {
                controller.simpleAlert(format(r.getString("ConstructingFrame.alerts.incorrect_field"),
                        property.getLabelText()));
                return false;
            }
        }
        return true;
    }

    private void addPropertyField(PropertyField propertyField) {
        properties.put(propertyField.getPropertyName(), propertyField);
        panel.add(new JLabel(propertyField.getLabelText()));
        panel.add(propertyField.getField());
    }

    private void addPropertyBox(EnumPropertyBox propertyBox) {
        properties.put(propertyBox.getPropertyName(), propertyBox);
        panel.add(new JLabel(propertyBox.getLabelText()));
        panel.add(propertyBox.getComboBox());
    }

    private void addProperties(Property... properties) {
        for (Property property : properties) {
            property.setLabWorkParams(labWorkParams);
            if (property instanceof PropertyField) addPropertyField((PropertyField) property);
            else addPropertyBox((EnumPropertyBox) property);
            if (editingMode) property.fillInputFromParams();
        }
    }
}

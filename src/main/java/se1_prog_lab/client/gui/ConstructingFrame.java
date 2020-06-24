package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.client.commands.concrete.Add;
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

public class ConstructingFrame extends JFrame {
    private final ClientCore controller;
    private final JPanel panel;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final Map<String, Property> properties = new LinkedHashMap<>();
    private final LabWorkParams labWorkParams;
    private boolean editingMode = false;

    public ConstructingFrame(ClientCore controller, LabWork labWork) throws HeadlessException {
        this.controller = controller;
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
                new StringPropertyField("name", "Название (не пустое)"),
                new LongPropertyField("coordinateX", "Координата X (<=625)"),
                new FloatPropertyField("coordinateY", "Координата Y (не пустая)"),
                new IntegerPropertyField("minimalPoint", "Минимальная оценка (положительная)"),
                new StringPropertyField("description", "Описание (не пустое)"),
                new IntegerPropertyField("tunedInWorks", "Настроенные работы"),
                new DifficultyPropertyBox("difficulty", "Сложность"),
                new StringPropertyField("authorName", "Имя автора (не пустое)"),
                new FloatPropertyField("authorHeight", "Рост автора (положительный)"),
                new StringPropertyField("authorPassportID", "Паспорт автора (не пустой, длина >= 9)"),
                new ColorPropertyBox("authorHairColor", "Цвет волос автора"),
                new IntegerPropertyField("authorLocationX", "Местоположение автора по X (не пустое)"),
                new FloatPropertyField("authorLocationY", "Местоположение автора по Y (не пустое)"),
                new IntegerPropertyField("authorLocationZ", "Местоположение автора по Z (не пустое)")
        );

        // Кнопка проверки полей
        JButton checkButton = new JButton("Проверить");
        checkButton.addActionListener(e -> {
            if (checkProperties()) controller.simpleAlert("Данные введены верно!");
        });
        panel.add(checkButton);

        // Кнопка создания экземпляра
        JButton createButton = new JButton("Создать");
        createButton.addActionListener(e -> {
            if (checkProperties()) {
                controller.executeServerCommand(new Add(createLabWork()));
            }
        });
        panel.add(createButton);

        if (editingMode) {
            // Кнопка изменения
            JButton updateButton = new JButton("Изменить");
            updateButton.addActionListener(e -> {
                if (checkProperties()) {
                    assert labWork != null;
                    controller.executeServerCommand(new Update(labWork.getId(), createLabWork()));
                    dispose();
                }
            });
            panel.add(updateButton);

            // Кнопка удаления
            JButton removeButton = new JButton("Удалить");
            removeButton.addActionListener(e -> {
                assert labWork != null;
                controller.executeServerCommand(new RemoveByID(labWork.getId()));
                dispose();
            });
            panel.add(removeButton);
        }

        // Кнопка очистки полей
        JButton clearButton = new JButton("Очистить поля");
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
                controller.simpleAlert("Неправильно введено поле \"" + property.getLabelText() + "'");
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

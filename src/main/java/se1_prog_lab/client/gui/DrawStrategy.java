package se1_prog_lab.client.gui;

import se1_prog_lab.collection.LabWork;

import java.awt.*;

public interface DrawStrategy {
    void draw(Graphics g, LabWork labWork); // тут не уверен, как именно рисуется, мб нужно будет поменять аргументы
}

package se1_prog_lab.client.gui;

import se1_prog_lab.collection.LabWork;

import javax.swing.*;
import java.awt.*;

public class LabWorkComponent extends JComponent {
    private final LabWork labWork;
    private final Color color;

    public LabWorkComponent(LabWork labWork, Color color) {
        super();
        this.labWork = labWork;
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect((int) labWork.getCoordinates().getX(),
                labWork.getCoordinates().getY().intValue(),
                labWork.getMinimalPoint() / 4,
                labWork.getMinimalPoint() / 4);
    }
}

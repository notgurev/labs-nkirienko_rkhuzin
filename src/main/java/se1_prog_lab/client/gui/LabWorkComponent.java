package se1_prog_lab.client.gui;

import se1_prog_lab.collection.LabWork;

import javax.swing.*;
import java.awt.*;

public class LabWorkComponent extends JComponent {
    private final LabWork labWork;
    private final Color color;
    private final DrawStrategy drawStrategy;

    public LabWorkComponent(LabWork labWork, Color color, DrawStrategy drawStrategy) {
        super();
        this.labWork = labWork;
        this.color = color;
        this.drawStrategy = drawStrategy;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        drawStrategy.draw(g, labWork);
    }
}

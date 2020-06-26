package se1_prog_lab.client.gui.strategies;

import se1_prog_lab.client.gui.DrawStrategy;
import se1_prog_lab.client.gui.VisualizationPanel;
import se1_prog_lab.collection.LabWork;

import java.awt.*;

public class RectangleStrategy implements DrawStrategy {
    @Override
    public void draw(Graphics g, LabWork labWork) {
        int size = labWork.getMinimalPoint() == null ? VisualizationPanel.SIZE_IF_NULL : labWork.getMinimalPoint();
        g.fillRect(0,0, size, size);
    }
}

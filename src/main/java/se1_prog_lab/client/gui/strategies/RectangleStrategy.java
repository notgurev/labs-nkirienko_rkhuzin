package se1_prog_lab.client.gui.strategies;

import se1_prog_lab.client.gui.DrawStrategy;
import se1_prog_lab.collection.LabWork;

import java.awt.*;

public class RectangleStrategy implements DrawStrategy {
    @Override
    public void draw(Graphics g, LabWork labWork) {
        g.fillRect((int) labWork.getCoordinates().getX(),
                labWork.getCoordinates().getY().intValue(),
                labWork.getMinimalPoint(),
                labWork.getMinimalPoint());
    }
}
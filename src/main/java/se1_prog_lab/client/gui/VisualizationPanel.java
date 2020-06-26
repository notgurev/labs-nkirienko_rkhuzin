package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.client.ModelListener;
import se1_prog_lab.client.gui.strategies.RectangleStrategy;
import se1_prog_lab.collection.LabWork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VisualizationPanel extends JPanel implements ModelListener {
    public static final int SIZE_IF_NULL = 30;
    private final ClientCore clientCore;
    private DrawStrategy drawStrategy = new RectangleStrategy();

    public VisualizationPanel(ClientCore clientCore) {
        clientCore.addListener(this);
        this.clientCore = clientCore;
        this.setLayout(null);
        drawLabWorks();
    }

    private void drawLabWorks() {
        for (LabWork labWork : clientCore.getBufferedCollectionPage()) {
            LabWorkComponent labWorkComponent = new LabWorkComponent(labWork, clientCore.getColorByOwner(labWork.getOwner()), drawStrategy);
            Component component = add(labWorkComponent);
            component.setLocation((int) labWork.getCoordinates().getX(), labWork.getCoordinates().getY().intValue());
            int size = labWork.getMinimalPoint() == null ? SIZE_IF_NULL : labWork.getMinimalPoint();
            component.setSize(size, size);
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    clientCore.openConstructingFrame(clientCore.getBufferedCollectionPage().indexOf(labWork));
                }
            });
        }
    }

    public void setDrawStrategy(DrawStrategy drawStrategy) {
        this.drawStrategy = drawStrategy;
    }

    public void update() {
        removeAll();
        drawLabWorks();
        revalidate();
        repaint();
    }

    public void addElement(Object[] fields) {
        update();
    }

    public void updateElement(Long id, Object[] fields) {
        update();
    }

    public void removeElement(Long id) {
        update();
    }
}

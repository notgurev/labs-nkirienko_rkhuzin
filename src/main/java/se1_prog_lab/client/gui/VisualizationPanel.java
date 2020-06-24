package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.client.gui.strategies.RectangleStrategy;
import se1_prog_lab.collection.LabWork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VisualizationPanel extends JPanel {
    private final ClientCore clientCore;
    private DrawStrategy drawStrategy = new RectangleStrategy();

    public VisualizationPanel(ClientCore clientCore) {
        this.clientCore = clientCore;
        this.setLayout(new BorderLayout());
        drawLabWorks();
    }

    private void drawLabWorks() {
        for (LabWork labWork : clientCore.getBufferedCollectionPage()) {
            Component component = add(new LabWorkComponent(labWork,
                    clientCore.getColorByOwner(labWork.getOwner()), drawStrategy));
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
        // todo
        revalidate();
        repaint();
    }
}

package se1_prog_lab.client.gui;

import se1_prog_lab.client.ClientCore;
import se1_prog_lab.collection.LabWork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VisualizationPanel extends JPanel {
    private final ClientCore clientCore;

    public VisualizationPanel(ClientCore clientCore) {
        this.clientCore = clientCore;
        this.setLayout(new BorderLayout());
        for (LabWork labWork : clientCore.getBufferedCollectionPage()) {
            Component component = add(new LabWorkComponent(labWork, clientCore.getColorByOwner(labWork.getOwner())));
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    clientCore.openConstructingFrame(clientCore.getBufferedCollectionPage().indexOf(labWork));
                }
            });
        }
    }

    public void update() {
        // todo
        revalidate();
        repaint();
    }
}

package view;

import controller.CanvasController;
import model.MainModel;
import model.osm.OSMWayType;

import javax.swing.*;
import java.awt.*;

public class CanvasView extends JComponent {
    MainModel model;
    CanvasController controller;

    public CanvasView(MainModel m, CanvasController c) {
        model = m;
        controller = c;
    }

    @Override
    public void paint(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;

        // Start with a beige-colored screen
        g.setPaint(new Color(255, 239, 210));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Setup default stroke
        g.setStroke(new BasicStroke(Float.MIN_VALUE));

        // Apply transforms
        g.transform(controller.getTransform());

        if (controller.shouldAntiAlias()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // Draw Buildings
        g.setPaint(new Color(200, 183, 166));
        for (Shape building : model.get(OSMWayType.BUILDING)) {
            g.fill(building);
        }

        // Draw water
        g.setPaint(new Color(60, 149, 255));
        for (Shape water : model.get(OSMWayType.WATER)) {
            g.fill(water);
        }

        // Draw Unknown
        g.setPaint(Color.black);
        for (Shape line : model.get(OSMWayType.UNKNOWN)) {
            g.draw(line);
        }


        // Draw Roads
        g.setStroke(new BasicStroke(0.00005f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g.setPaint(new Color(255, 255, 255));
        for (Shape road : model.get(OSMWayType.ROAD)) {
            g.draw(road);
        }

        // Draw highways
        g.setStroke(new BasicStroke(0.00005f));
        g.setPaint(new Color(255, 177, 90));
        for (Shape highway : model.get(OSMWayType.HIGHWAY)) {
            g.draw(highway);
        }
    }
}
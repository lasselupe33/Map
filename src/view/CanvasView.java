package view;

import controller.CanvasController;
import helpers.ColorMap;
import model.MainModel;
import model.MapElement;
import model.osm.OSMWayType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;

/**
 * This view draws the map.
 */
public class CanvasView extends JComponent {
    private MainModel model;
    private CanvasController controller;

    public CanvasView(MainModel m, CanvasController c) {
        model = m;
        controller = c;
        setFocusable(true);
    }

    /**
     * Draw map.
     * @param _g Graphics
     */
    @Override
    public void paint(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        int zoom = CanvasController.getInstance().getZoomLevel();
        g.setStroke(new BasicStroke(Float.MIN_VALUE));

        Rectangle2D viewRect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());

        g.setPaint(new Color(60, 149, 255));
        g.fill(viewRect);
        g.transform(controller.getTransform());

        if (controller.shouldAntiAlias()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        g.setPaint(Color.yellow);
        for (MapElement m : model.getTreeData()){
            switch (m.getType()){
                case COASTLINE:
                    g.setPaint(ColorMap.getColor(m.getType()));
                    g.fill(m.getShape());
                    break;
                case WATER:
                    g.setPaint(ColorMap.getColor(m.getType()));
                    g.fill(m.getShape());
                    break;
                case UNKNOWN:
                    g.setPaint(ColorMap.getColor(m.getType()));
                    g.draw(m.getShape());
                    break;
                case ROAD:
                    g.setStroke(new BasicStroke(0.00001f));
                    g.setPaint(ColorMap.getColor(m.getType()));
                    g.draw(m.getShape());
                    break;
                case HIGHWAY:
                    g.setStroke(new BasicStroke(0.00005f));
                    g.setPaint(ColorMap.getColor(m.getType()));
                    g.draw(m.getShape());
                    break;
                case BUILDING:
                    g.setPaint(ColorMap.getColor(m.getType()));
                    g.fill(m.getShape());
                    break;
                default:
                    break;
            }

        }

        /*


        g.setPaint(new Color(237, 237, 237));
        for (MapElement coastline: model.get(OSMWayType.COASTLINE)) {
            g.fill(coastline.getShape());
        }

        g.setPaint(new Color(60, 149, 255));
        for (MapElement water: model.get(OSMWayType.WATER)) {
            g.fill(water.getShape());
        }

        if (zoom > 5) {
            g.setPaint(Color.black);
            for (MapElement line : model.get(OSMWayType.UNKNOWN)) {
                g.draw(line.getShape());
            }
        }

        if (zoom > 15) {
            g.setStroke(new BasicStroke(0.00001f));
            g.setPaint(new Color(230, 139, 213));
            for (MapElement road : model.get(OSMWayType.ROAD)) {
                g.draw(road.getShape());
            }
        }

        g.setStroke(new BasicStroke(0.00005f));
        g.setPaint(new Color(255, 114, 109));
        for (MapElement highway: model.get(OSMWayType.HIGHWAY)) {
            g.draw(highway.getShape());
        }

        if (zoom > 10) {
            g.setPaint(new Color(172, 169, 151));
            for (MapElement building : model.get(OSMWayType.BUILDING)) {
                g.fill(building.getShape());
            }
        }*/
        g.setTransform(new AffineTransform());
    }
}
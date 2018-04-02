package view;

import controller.CanvasController;
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
    private double fps = 0.0;

    public CanvasView(MainModel m, CanvasController c) {
        model = m;
        controller = c;
    }

    /**
     * Draw map.
     * @param _g Graphics
     */
    @Override
    public void paint(Graphics _g) {

        Graphics2D g = (Graphics2D) _g;
        g.setStroke(new BasicStroke(Float.MIN_VALUE));

        Rectangle2D viewRect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());

        g.setPaint(new Color(60, 149, 255));
        g.fill(viewRect);
        g.transform(controller.getTransform());


        for(MapElement s : model.getTreeData()){
            switch (s.getTypeZoomLevel()){
                case UNKNOWN:
                    g.setPaint(new Color(5, 172, 21));
                    g.draw(s.getShape());
                case COASTLINE:
                    g.setPaint(new Color(237, 237, 237));
                    g.fill(s.getShape());
                    break;
                case WATER:
                    g.setPaint(new Color(60, 149, 255));
                    g.fill(s.getShape());
                    break;
                case ROAD:
                    g.setStroke(new BasicStroke(0.00001f));
                    g.setPaint(new Color(230, 219, 34));
                    g.fill(s.getShape());
                case HIGHWAY:
                    g.setStroke(new BasicStroke(0.00005f));
                    g.setPaint(new Color(248, 157, 255));
                    g.draw(s.getShape());
                case BUILDING:
                    g.setPaint(new Color(172, 169, 151));
                    g.fill(s.getShape());
                default:
                    g.setStroke(new BasicStroke(Float.MIN_VALUE));
                    g.setPaint(Color.black);
                    g.draw(s.getShape());
                    break;
            }

        }


        /*
        if (controller.shouldAntiAlias()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setPaint(new Color(237, 237, 237));
        for (Shape coastline: model.get(OSMWayType.COASTLINE)) {
            g.fill(coastline);
        }
        g.setPaint(new Color(60, 149, 255));
        for (Shape water: model.get(OSMWayType.WATER)) {
            if (water.intersects(viewRect)) {
                g.fill(water);
            }
        }
        g.setPaint(Color.black);
        for (Shape line: model.get(OSMWayType.UNKNOWN)) {
            if (line.intersects(viewRect)) {
                g.draw(line);
            }
        }
        g.setStroke(new BasicStroke(0.00001f));
        g.setPaint(new Color(230, 139, 213));
        for (Shape road : model.get(OSMWayType.ROAD)) {
            if (road.intersects(viewRect)) {
                g.draw(road);
            }
        }
        g.setStroke(new BasicStroke(0.00005f));
        g.setPaint(new Color(255, 114, 109));
        for (Shape highway: model.get(OSMWayType.HIGHWAY)) {
            if (highway.intersects(viewRect)) {
                g.draw(highway);
            }
        }
        g.setPaint(new Color(172, 169, 151));
        for (Shape building: model.get(OSMWayType.BUILDING)) {
            if (building.intersects(viewRect)) {
                g.fill(building);
            }
        }*/

    }
}
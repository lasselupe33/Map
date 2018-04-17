package view;

import controller.CanvasController;
import helpers.ColorMap;
import helpers.StrokeMap;
import model.MainModel;
import model.MapElements.MapElement;
import model.osm.OSMWayType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;

/**
 * This view draws the map.
 */
public class CanvasView extends JComponent {
    private CanvasController controller;


    public CanvasView(CanvasController c) {
        controller = c;
        setFocusable(true);
    }



    /**
     * Draw map.
     *
     * @param _g Graphics
     */
    @Override
    public void paint(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;

        Rectangle2D viewRect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());

        g.setStroke(new BasicStroke(Float.MIN_VALUE));
        g.setPaint(ColorMap.getColor(OSMWayType.WATER));
        g.fill(viewRect);
        g.transform(controller.getTransform());

        if (controller.shouldAntiAlias()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        viewRect = controller.getModelViewRect();

        for (MapElement m : controller.getMapData()) {
            if (m.getShape().intersects(viewRect)) {
                g.setPaint(ColorMap.getColor(m.getType()));
                if (m.shouldFill()) {
                    g.setStroke(new BasicStroke(Float.MIN_VALUE));
                    g.fill(m.getShape());
                } else {
                    g.setStroke(StrokeMap.getStroke(m.getType()));
                    g.draw(m.getShape());
                }
            }
        }

    }
}
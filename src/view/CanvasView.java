package view;

import controller.MapController;
import helpers.ColorMap;
import helpers.GetDistance;
import helpers.StrokeMap;
import model.Address;
import model.Coordinates;
import model.MapElement;
import model.WayType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This view draws the map.
 */
public class CanvasView extends JComponent {
    private MapController controller;

    public CanvasView(MapController c) {
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
        g.setPaint(ColorMap.getColor(WayType.WATER));
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

        paintLocationIcon(g);

    }


    private void paintLocationIcon(Graphics2D g) {
        if (controller.getListOfLocations().isEmpty()) return;


        double scale = 0.003 * GetDistance.PxToKm(100);

        for (Coordinates coord : controller.getListOfLocations()) {
            Double[] xValue = new Double[] {coord.getX()-scale/2, coord.getX(), coord.getX()+scale/2, coord.getX()-scale/2};
            Double[] yValue = new Double[] {coord.getY()-scale, coord.getY(), coord.getY()-scale, coord.getY()-scale};

            Path2D path = new Path2D.Double();
            path.moveTo(xValue[0], yValue[0]);

            for(int i = 1; i < xValue.length-1; i++) {
                path.lineTo(xValue[i], yValue[i]);
            }
            path.quadTo(xValue[xValue.length-1]+scale/2, yValue[yValue.length-1]-scale/2, xValue[xValue.length-1], yValue[yValue.length-1]);

            Ellipse2D circle = new Ellipse2D.Double(coord.getX()-scale/6, coord.getY()-scale, scale/3, scale/3);

            g.setPaint(Color.red);
            g.fill(path);
            g.setPaint(new Color(124, 17, 19));
            g.fill(circle);
        }
    }

}
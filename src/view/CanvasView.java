package view;

import controller.MapController;
import helpers.ColorMap;
import helpers.StrokeMap;
import model.Address;
import model.MapElement;
import model.WayType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This view draws the map.
 */
public class CanvasView extends JComponent {
    private MapController controller;
    private List<Ellipse2D> ellipseList = new ArrayList<>();


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
        g.setPaint(Color.red);

        ellipseList.clear();
        double size = 0.1 / controller.getZoomLevel();
        //System.out.println("Size: " + 1000*size);
        for (Address a : controller.getListOfLocations()) {
            ellipseList.add(new Ellipse2D.Double(a.getCoordinates().getX() - size / 2,
                    a.getCoordinates().getY() - size / 2, size, size));
        }

        for (Ellipse2D e : ellipseList) {
            g.fill(e);
        }
    }
}
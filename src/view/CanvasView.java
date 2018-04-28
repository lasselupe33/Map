package view;

import controller.MapController;
import helpers.ColorMap;
import helpers.StrokeMap;
import model.MapElement;
import model.MapModel;
import model.WayType;
import model.graph.Graph;
import model.graph.VehicleType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * This view draws the map.
 */
public class CanvasView extends JComponent {
    private MapController controller;
    private Graph graph;
    private Path2D route = null;
    private ColorMap colorMap;


    public CanvasView(MapController c, Graph g, ColorMap cm) {
        controller = c;
        colorMap = cm;
        graph = g;

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
        g.setPaint(colorMap.getColor(WayType.WATER));
        g.fill(viewRect);
        g.transform(controller.getTransform());

        if (controller.shouldAntiAlias()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        viewRect = controller.getModelViewRect();

        for (MapElement m : controller.getMapData()) {
            if (m.getShape().intersects(viewRect)) {
                g.setPaint(colorMap.getColor(m.getType()));
                if (m.shouldFill()) {
                    g.setStroke(new BasicStroke(Float.MIN_VALUE));
                    g.fill(m.getShape());
                } else {
                    g.setStroke(StrokeMap.getStroke(m.getType()));
                    g.draw(m.getShape());
                }
            }
        }

        if (graph.getShortestPath() != null) {
            switch (graph.getType()) {
                case CAR:
                    g.setStroke(new BasicStroke(0.00007f));
                    break;
                case BICYCLE:
                    g.setStroke(new BasicStroke(0.00005f));
                    break;
                case PEDESTRIAN:
                    g.setStroke(new BasicStroke(0.00004f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {0.00003f, 0.00002f}, 0));
                    break;
                default:
                    // There shouldn't be other possibilities
                    break;
            }
            g.setColor(new Color(66, 133, 244));
            route = graph.getShortestPath();
            g.draw(route);
            repaint();
        }

    }

    public void removeRoute() {
        if (route != null) {
            route.reset();
            repaint();
        }
    }
}
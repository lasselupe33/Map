package view;

import controller.MapController;
import controller.NavigationController;
import helpers.ColorMap;
import helpers.UnitConverter;
import helpers.StrokeMap;
import model.*;
import model.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * This view draws the map.
 */
public class MapView extends JComponent {
    private MapController controller;
    private NavigationController navigationController;
    private Graph graph;
    private ColorMap colorMap;
    private HashMap<Shape, Coordinates> favoriteIcons = new HashMap<>();

    public MapView(MapController c, Graph g, ColorMap cm, NavigationController nc) {
        controller = c;
        colorMap = cm;
        graph = g;
        navigationController = nc;

        setFocusable(true);
    }

    /**
     * Draw map.
     * @param _g Graphics
     */
    @Override
    public void paint(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;

        // Setup initial values
        Rectangle2D viewRect = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        g.setStroke(new BasicStroke(Float.MIN_VALUE));
        g.setPaint(colorMap.getColor(WayType.WATER));
        g.fill(viewRect);
        g.transform(controller.getTransform());

        // Determine antialiasing
        if (controller.shouldAntiAlias()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        viewRect = controller.getModelViewRect();

        // Draw all mapData required for current screen
        for (MapElement m : controller.getMapData()) {
            if (m.getShape().intersects(viewRect)) {
                g.setPaint(colorMap.getColor(m.getType()));
                if (m.shouldFill()) {
                    g.setStroke(new BasicStroke(Float.MIN_VALUE));
                    g.fill(m.getShape());
                } else {
                    // Get the current zoomlevel
                    float zoomLevel = MapController.getInstance().getZoomLevel();

                    g.setStroke(StrokeMap.getStroke(m.getType(), zoomLevel));
                    g.draw(m.getShape());
                }
            }
        }

        // Draw navigation path if any
        if (graph.getRoutePath() != null) {
            drawRoute(g);
        }

        // Draw icons
        drawFavoritesIcons(g);
        drawLocationIcon(g);
    }

    /**
     * Draw navigation route
     * @param g graphics
     */
    private void drawRoute(Graphics2D g) {
        // Get zoom level and calculate factor for determining width of stroke
        float zoomLevel = MapController.getInstance().getZoomLevel();
        float factor = (511 - zoomLevel);

        // Set stroke based on vehicle type
        switch (graph.getVehicleType()) {
            case CAR:
                g.setStroke(new BasicStroke(0.00005f*factor));
                break;
            case BICYCLE:
                g.setStroke(new BasicStroke(0.00004f*factor));
                break;
            case PEDESTRIAN:
                g.setStroke(new BasicStroke(0.00004f*factor, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                        0, new float[] {0.00003f*factor, 0.00002f*factor}, 0));
                break;
            default:
                // There shouldn't be other possibilities
                break;
        }

        // Set color
        // Grey if grayscale mode; else blue (suited for color blind mode)
        if (colorMap.isGrayscale()) {
            g.setColor(new Color(30,30,30));
        } else {
            g.setColor(new Color(66, 133, 244));
        }

        g.draw(graph.getRoutePath());

        // Draw dotted path from start address to beginning of route
        // and from end address to end of route
        g.setStroke(new BasicStroke(0.00004f*factor, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {0.00003f*factor, 0.00002f*factor}, 0));
        g.setColor(Color.GRAY);
        g.draw(navigationController.getStartAddressPath());
        g.draw(navigationController.getEndAddressPath());

        // Draw icon indicating beginning of route
        paintStartNavigationIcon(g);
    }

    /**
     * Draw favorites icons
     * @param g Graphics
     */
    public void drawFavoritesIcons(Graphics2D g) {
        // Set colors for favorites icons
        Color colorMain = new Color(66, 133, 244);
        Color colorDetail = new Color(0, 4, 161);
        // If color mode is gray scale change colors to gray scale
        if (colorMap.isGrayscale()) {
            colorMain = new Color(80,80,80);
            colorDetail = new Color(148,148,148);
        }

        // Draw favorites icons if any favorites
        if (!controller.getFavorites().isEmpty()) {
            for (Favorite f : controller.getFavorites()) {
                paintLocationIcon(g, f.getAddress().getCoordinates(), colorMain, colorDetail, 0.0025);
            }
        }
    }

    /**
     * Draw location icon
     * @param g Graphics
     */
    public void drawLocationIcon(Graphics2D g) {
        // Set colors for location icon
        Color colorMain = Color.red;
        Color colorDetail = new Color(124, 17, 19);
        // If color mode is gray scale change colors to gray scala
        if (colorMap.isGrayscale()) {
            colorMain = new Color(30,30,30);
            colorDetail = new Color(148,148,148);
        }

        // Draw location icon if needed
        if (controller.getLocationCoordinates() != null) paintLocationIcon(g, controller.getLocationCoordinates(), colorMain, colorDetail, 0.003);

    }

    /**
     * Draw location icon at address or route search
     * @param g graphics
     * @param coord coordinates for icon
     * @param iconColor main color
     * @param circleColor color for details
     * @param scaling size of icon
     */
    private void paintLocationIcon(Graphics2D g, Coordinates coord, Color iconColor, Color circleColor, double scaling) {
        float scale = (float) (scaling *  UnitConverter.PxToKm(100));

        float[] xValue = new float[] {coord.getX()-scale/2, coord.getX(), coord.getX()+scale/2, coord.getX()-scale/2};
        float[] yValue = new float[] {coord.getY()-scale, coord.getY(), coord.getY()-scale, coord.getY()-scale};

        Path2D path = new Path2D.Double();
        path.moveTo(xValue[0], yValue[0]);

        for(int i = 1; i < xValue.length-1; i++) {
            path.lineTo(xValue[i], yValue[i]);
        }
        path.quadTo(xValue[xValue.length-1]+scale/2, yValue[yValue.length-1]-scale/2, xValue[xValue.length-1], yValue[yValue.length-1]);
        favoriteIcons.put(path, coord);

        Ellipse2D circle = new Ellipse2D.Double(coord.getX()-scale/6, coord.getY()-scale, scale/3, scale/3);

        g.setPaint(iconColor);
        g.fill(path);
        g.setPaint(circleColor);
        g.fill(circle);

    }

    /**
     * Draw icon for starting point of navigation
     * @param g Graphics
     */
    private void paintStartNavigationIcon(Graphics2D g) {
        if (controller.getStartCoordinates() == null) return;

        Coordinates coord = controller.getStartCoordinates();
        float scale = (float) (0.0015 *  UnitConverter.PxToKm(100));
        float scale2 = (float) (0.0022 * UnitConverter.PxToKm(100));

        Ellipse2D innerCircle = new Ellipse2D.Float(coord.getX()-scale/2, coord.getY()-scale/2, scale, scale);
        Ellipse2D outerCircle = new Ellipse2D.Float(coord.getX()-scale2/2, coord.getY()-scale2/2, scale2, scale2);

        if (colorMap.isGrayscale()) {
            g.setPaint(new Color(30,30,30));
        } else {
            g.setPaint(new Color(66, 133, 244));
        }
        g.fill(outerCircle);
        g.setPaint(Color.white);
        g.fill(innerCircle);
    }

    public Coordinates containsCoordinate(Point2D p) {
        for (Shape s : favoriteIcons.keySet()) {
            if (s.contains(p)) return favoriteIcons.get(s);
        }
        return null;
    }

}
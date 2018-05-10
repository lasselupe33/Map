package controller;

import helpers.UnitConverter;
import model.MetaModel;
import model.MapElement;
import model.MapModel;
import model.WayType;
import model.graph.Graph;
import view.MapView;
import model.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This singleton controller handles all input related to the canvas that draws the map.
 */
public class MapController {
    /** Create the instance of the controller */
    private static MapController instance = new MapController();
    private MapView canvas;
    private boolean useAntiAliasing = true;
    private Timer timer = new Timer();
    private long lastAction = 0;

    /** Contains references to the required models */
    private MetaModel metaModel;
    private MapModel mapModel;
    private FavoritesModel favoritesModel;
    private Graph graph;
    private AffineTransform transform = new AffineTransform();

    private Coordinates locationIconCoordinates;
    private Coordinates startIconCoordinates;

    /**
     * @return the transform to be used in the canvasView
     */
    public AffineTransform getTransform() {
        return transform;
    }

    public static MapController getInstance() {
        return instance;
    }

    /** Add needed dependencies */
    public void addDependencies(MapView mv, MapModel mm, MetaModel m, Graph g, FavoritesModel fm) {
        canvas = mv;
        mapModel = mm;
        metaModel = m;
        favoritesModel = fm;
        graph = g;
    }

    /** Return whether or not the view should utilise antialias */
    public boolean shouldAntiAlias() {
        return useAntiAliasing;
    }

    public void toggleAntiAliasing() {
        useAntiAliasing = !useAntiAliasing;
        canvas.repaint();
    }

    /** Helper that pans to a specific location on the map */
    public void pan(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
        updateMap();
    }

    /**
     * Zoom to center with given factor
     * @param factor to zoom by
     */
    public void zoomToCenter(double factor) {
        zoom(factor, -canvas.getWidth() / 2, -canvas.getHeight() / 2);
    }

    public void zoom(double factor, double x, double y) {
        // Cap the zoom so that the user isn't able to zoom either too far or too far in.
        if (UnitConverter.PxToKm(100) > 50 && factor < 1.01) factor = 1.0;
        if (UnitConverter.PxToKm(100) < 0.01 && factor > 1.0) factor = 1.0;

        // Zoom by the passed factor to the coordinate given
        pan(x, y);
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(-x, -y);
    }

    /** Helper that returns the current data required for rendering the map */
    public List<MapElement> getMapData() { return mapModel.getMapData(); }

    /** Helper that updates the list of mapElements to be rendered, based on the current transform. */
    public void updateMap(){
        Point2D p0 = toModelCoords(new Point2D.Double(0,0));
        Point2D p1 = toModelCoords(new Point2D.Double(canvas.getWidth(), canvas.getHeight()));

        mapModel.updateCurrentMapElement(p0, p1);

        repaintMap(false);
    }

    /**
     * Helper method that reset the canvas when called
     */
    public void reset() {
        // Reset transform
        transform = new AffineTransform();

        // put screen to correct place on canvas
        int height = canvas.getHeight();
        int offsetX = (canvas.getWidth() - height) / 2;

        // Pan to map
        pan(-metaModel.getMinLon(), -metaModel.getMaxLat());
        zoom(height / (metaModel.getMaxLon() - metaModel.getMinLon()), 0, 0);

        // Ensure that the initial canvas is properly centered, even on screens that are wider than they are tall.
        pan(offsetX, 0);

        // Update map elements
        updateMap();
    }

    /** Helper that moves the screen to a specific set of coordinates */
    public void moveScreen(Coordinates coordinates) {
        transform = new AffineTransform();

        panToMap(coordinates.getX(), coordinates.getY());

        // Calculates scale the map should zoom
        // (508 is the BUILDING WayTypes zoom priority)
        double zoomscale = Math.abs(100.0 * (508 - getZoomLevel()) / 510.0);
        zoomToCenter(zoomscale);

        updateLocationCoordinates(coordinates);

        // Update map elements
        updateMap();
    }

    /** Helper that moves the screen so that the whole navigation is visible between two points */
    public void moveScreenNavigation(Rectangle2D rect){
        transform = new AffineTransform();

        // Panning to the center of the rectangle, and the slightly to the left because
        // of the navigation panel
        panToMap((rect.getCenterX()-rect.getWidth() / 8), rect.getCenterY());

        // Calculates scale the map should zoom
        double zoomscale = (getRectDistance(getModelViewRect()) / 3) / getRectDistance(rect);
        zoomToCenter(zoomscale);

        updateMap();
    }

    /**
     * Internal helper that return the diagonal distance in KM in a rectangle
     */
    private double getRectDistance(Rectangle2D rectangle2D) {
        return UnitConverter.DistInKM(rectangle2D.getMinX(), rectangle2D.getMinY(), rectangle2D.getMaxX(), rectangle2D.getMaxY());
    }

    /**
     * Internal helper that pans map to the proper location
     */
    private void panToMap(double x, double y) {
        // Pan to map
        pan(-x, -y);
        zoom(canvas.getHeight() / (metaModel.getMaxLon() - metaModel.getMinLon()), 0, 0);
        // Ensure that the initial canvas is properly centered, even on screens that are wider than they are tall.
        pan(canvas.getWidth()/2, canvas.getHeight()/2);
    }

    /** Methods to handle list of locations where Icons should be drawn */
    /* The location icon */
    public void updateLocationCoordinates(Coordinates coordinates){ locationIconCoordinates = coordinates; }

    public void deleteLocationCoordinates() { locationIconCoordinates = null; }

    public Coordinates getLocationCoordinates() { return locationIconCoordinates; }

    /* The start location icon when navigation is active */
    public void updateStartCoordinates(Coordinates coordinates){ startIconCoordinates = coordinates; }

    public void deleteStartCoordinates() { startIconCoordinates = null; }

    public Coordinates getStartCoordinates() { return startIconCoordinates; }

    public ArrayList<Favorite> getFavorites() { return favoritesModel.getFavorites(); }

    /** Helper that returns the viewRect that surrounds the current viewPort in model coordinates */
    public Rectangle2D getModelViewRect() {
        try {
            return transform.createInverse().createTransformedShape(new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight())).getBounds2D();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Public helper that converts a point from the screen to the coordinate that matches the current position on the
     * model, based on the current transform.
     */
    public Point2D toModelCoords(Point2D p) {
        try {
            return transform.inverseTransform(p, null);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Internal helper that parses the current zoom level.
     * This level will be between 1 and 510, see WayTypes for further clarification of this scale.
     */
    public int getZoomLevel() {
        double currDist = UnitConverter.PxToKm(100) * 10;
        int maxDist = 510;

        int zoomLevel = Math.max((int) ((maxDist - currDist)) + 1, 1);
        return zoomLevel;
    }

    /** Helper that repaints map. If antialias isn't forced, then it will be disabled and debounced until 150ms has
     * passed after the last call of this function.
     * This is done in order to increase performance when, for example, the user is panning and repainting a lot.
     */
    public void repaintMap(boolean forceAntialias) {
        canvas.repaint();

        if (forceAntialias) {
            useAntiAliasing = true;
        } else {
            useAntiAliasing = false;
            lastAction = System.currentTimeMillis();

            // Only use antialiasing when idle in order to preserve performance
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                        if (System.currentTimeMillis() - lastAction >= 150) {
                            useAntiAliasing = true;
                            canvas.repaint();
                        }
                        }
                    },
                    150
            );
        }
    }

    /** Remove navigation route */
    public void removeRoute() {
        graph.resetRoute();
        repaintMap(true);
    }
}

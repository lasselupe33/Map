package controller;

import helpers.GetDistance;
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
 * This controller handles all logic and input related to the canvas that draws the map.
 */
public class MapController {
    private static MapController instance = new MapController();
    private static MapView canvas;
    private static boolean useAntiAliasing = true;
    private static Timer timer = new Timer();
    private static long lastAction = 0;

    private MetaModel metaModel;
    private MapModel mapModel;
    private AffineTransform transform = new AffineTransform();

    private Graph graph;

    private static Coordinates locationIconCoordinates;
    private static Coordinates startIconCoordinates;
    private static List<Coordinates> listOfFavorites = new ArrayList<>();

    /**
     * @return the transform to be used in the canvasView
     */
    public AffineTransform getTransform() {
        return transform;
    }

    public static MapController getInstance() {
        return instance;
    }

    public void addDependencies(MapView c, MapModel mm, MetaModel m, Graph g) {
        canvas = c;
        mapModel = mm;
        metaModel = m;
        graph = g;
    }

    /**
     * @return whether or not the view should utilise antialias
     */
    public boolean shouldAntiAlias() {
        return useAntiAliasing;
    }

    public void toggleAntiAliasing() {
        useAntiAliasing = !useAntiAliasing;
        canvas.repaint();
    }

    /**
     *
     * @param dx
     * @param dy
     */
    public void pan(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
        MouseController.thread();
    }

    /**
     * Zoom to center with given factor
     * @param factor to zoom by
     */
    public void zoomToCenter(double factor) {
        zoom(factor, -canvas.getWidth() / 2, -canvas.getHeight() / 2);
    }

    public void zoom(double factor, double x, double y) {
        pan(x, y);
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(-x, -y);
        updateMap();
    }

    /** Helper that returns the current data required for rendering the map */
    public List<MapElement> getMapData() { return mapModel.getMapData(); }

    /**
     * Helper that updates the list of mapElements to be rendered, based on the current transform.
     */
    public void updateMap(){
        Point2D p0 = toModelCoords(new Point2D.Double(0,0));
        Point2D p1 = toModelCoords(new Point2D.Double(canvas.getWidth(), canvas.getHeight()));

        int i = 0;
        List<MapElement> tmpList = new ArrayList<>();

        for (WayType type : WayType.values()) {
            if (type.getPriority() <= getZoomLevel()) {
                tmpList.addAll(mapModel.getTree(i).searchTree(p0, p1));
            }
            i++;
        }

        mapModel.setMapData(tmpList);
        repaintMap(false);
    }

    /** Helper method that reset the canvas when called */
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

    public void moveScreen(Coordinates coordinates, WayType type) {
        transform = new AffineTransform();
        // Pan to map
        pan(-coordinates.getX(), -coordinates.getY());
        zoom(canvas.getHeight() / (metaModel.getMaxLon() - metaModel.getMinLon()), 0, 0);
        // Ensure that the initial canvas is properly centered, even on screens that are wider than they are tall.
        pan(canvas.getWidth()/2, canvas.getHeight()/2);

        double zoomscale = 100.0*(type.getPriority()-getZoomLevel())/510.0;


        zoomToCenter(zoomscale);

        updateLocationCoordinates(coordinates);

        // Update map elements
        updateMap();
    }



    //Methods to handle list of locations where Icons should be drawn
    public static void updateLocationCoordinates(Coordinates coordinates){ locationIconCoordinates = coordinates; }

    public static void deleteLocationCoordinates() { locationIconCoordinates = null; }

    public Coordinates getLocationCoordinates() { return locationIconCoordinates; }

    public static void updateStartCoordinates(Coordinates coordinates){ startIconCoordinates = coordinates; }

    public static void deleteStartCoordinates() { startIconCoordinates = null; }

    public Coordinates getStartCoordinates() { return startIconCoordinates; }

    public static void updateListOfFavorites(Coordinates coordinates) { listOfFavorites.add(coordinates); }

    public static void deleteFavoritesFromList(Coordinates coordinates) { listOfFavorites.remove(coordinates); }

    public List<Coordinates> getListOfFavorites() { return listOfFavorites; }




    public Rectangle2D getModelViewRect() {
        try {
            return transform.createInverse().createTransformedShape(new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight())).getBounds2D();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        return null;
    }


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
     * This level will be between 1 and 500.
     */
    public int getZoomLevel() {
        double currDist = GetDistance.PxToKm(100) * 10;
        int maxDist = 510;

        int zoomLevel = Math.max((int) ((maxDist - currDist)) + 1, 1);
        return zoomLevel;
    }

    public static void repaintMap(boolean forceAntialias) {
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
                            if (System.currentTimeMillis() - lastAction >= 300) {
                                useAntiAliasing = true;
                                canvas.repaint();
                            }
                        }
                    },
                    300
            );
        }
    }

    public void removeRoute() {
        graph.resetRoute();
        repaintMap(true);
    }
}

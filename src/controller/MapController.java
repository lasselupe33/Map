package controller;

import helpers.GetDistance;
import model.*;
import view.CanvasView;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller handles all logic and input related to the canvas that draws the map.
 */
public class MapController {
    private static MapController instance = new MapController();

    private MetaModel metaModel;
    private MapModel mapModel;
    private static CanvasView canvas;
    private AffineTransform transform = new AffineTransform();
    private boolean useAntiAliasing = false;

    private List<Address> listOfLocations = new ArrayList<>();


    /**
     * @return the transform to be used in the canvasView
     */
    public AffineTransform getTransform() {
        return transform;
    }

    public static MapController getInstance() {
        return instance;
    }

    public void addDependencies(CanvasView c, MapModel mm, MetaModel m) { canvas = c; mapModel = mm; metaModel = m; }

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

        canvas.repaint();
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

        System.out.println("x = " + coordinates.getX() + " y = " + coordinates.getY());
        // Pan to map
        pan(-coordinates.getX(), -coordinates.getY());
        zoom(canvas.getHeight() / (metaModel.getMaxLon() - metaModel.getMinLon()), 0, 0);
        // Ensure that the initial canvas is properly centered, even on screens that are wider than they are tall.
        pan(canvas.getWidth()/2, canvas.getHeight()/2);

        double zoomscale = 100*(type.getPriority())/510;
        System.out.println(zoomscale);

        zoomToCenter(zoomscale);


        // Update map elements
        updateMap();
    }



    //Methods to handle list of locations
    public void addToListOfLocations(Address address){ listOfLocations.add(address); }

    public void removeFromListOfLocations(Address address) { listOfLocations.remove(address); }

    public void clearListOfLocations() { listOfLocations.clear(); }

    public List<Address> getListOfLocations() { return listOfLocations; }



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

    public static void repaintMap() {
        canvas.repaint();
    }
}

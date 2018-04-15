package controller;

import model.MainModel;
import model.MapElements.MapElement;
import model.MapModel;
import view.CanvasView;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * This controller handles all logic and input related to the canvas that draws the map.
 */
public class CanvasController {
    private static CanvasController instance = new CanvasController();

    private MainModel mainModel;
    private MapModel mapModel;
    private CanvasView canvas;
    private AffineTransform transform = new AffineTransform();
    private boolean useAntiAliasing = false;
    private int zoomLevel = 0;

    /**
     * @return the transform to be used in the canvasView
     */
    public AffineTransform getTransform() {
        return transform;
    }

    public static CanvasController getInstance() {
        return instance;
    }

    public void addDependencies(CanvasView c, MapModel mm, MainModel m) { canvas = c; mapModel = mm; mainModel = m; }

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
        updateMap();
        canvas.repaint();
    }

    /**
     * Zoom to center with given factor
     * @param factor to zoom by
     */
    public void zoomToCenter(double factor) {
        zoom(factor, -canvas.getWidth() / 2, -canvas.getHeight() / 2);
    }

    public void zoom(double factor, double x, double y) {

        if (factor < 1.0) zoomLevel--;
        else if (factor > 1.0) zoomLevel++;

        //System.out.println(zoomLevel);

        pan(x, y);
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(-x, -y);
        updateMap();

        canvas.repaint();
    }

    /** Helper that returns the current data required for rendering the map */
    public List<MapElement> getMapData() {
        return mapModel.getMapData();
    }

    /**
     * Helper that returns the corresponding model coordinates of a screen coordinate, based on the current transform.
     */
    public void updateMap(){
        Point2D p0 = new Point2D.Double(0,0);
        Point2D p1 = new Point2D.Double(canvas.getWidth(), canvas.getHeight());
        mapModel.updateMap(toModelCoords(p0),toModelCoords(p1));
    }

    /** Helper method that reset the canvas when called */
    public void reset() {
        // Reset transform
        transform = new AffineTransform();

        // put screen to correct place on canvas
        int height = canvas.getHeight();
        int offsetX = (canvas.getWidth() - height) / 2;

        // Pan to map
        pan(-mainModel.getMinLon(), -mainModel.getMaxLat());
        zoom(height / (mainModel.getMaxLon() - mainModel.getMinLon()), 0, 0);

        // Ensure that the initial canvas is properly centered, even on screens that are wider than they are tall.
        pan(offsetX, 0);

        // Update map elements
        updateMap();
    }

    public Rectangle2D getModelViewRect() {
        try {
            return transform.createInverse().createTransformedShape(new Rectangle2D.Double(0, 0, canvas.getWidth(), canvas.getHeight())).getBounds2D();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public Point2D toModelCoords(Point2D p) {
        try {
            return transform.inverseTransform(p, null);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return null;
    }
}

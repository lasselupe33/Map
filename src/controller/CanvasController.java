package controller;

import model.Coordinates;
import model.MapElement;
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

    private MapModel mapModel;
    private static CanvasView canvas;
    private AffineTransform transform = new AffineTransform();
    private boolean useAntiAliasing = false;


    /**
     * @return the transform to be used in the canvasView
     */
    public AffineTransform getTransform() {
        return transform;
    }

    public static CanvasController getInstance() {
        return instance;
    }

    public void addDependencies(CanvasView c, MapModel mm) { canvas = c; mapModel = mm; }

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
     * Helper that returns the corresponding model coordinates of a screen coordinate, based on the current transform.
     */
    public void updateMap(){
        Point2D p0 = new Point2D.Double(0,0);
        Point2D p1 = new Point2D.Double(canvas.getWidth(), canvas.getHeight());
        mapModel.updateMap(toModelCoords(p0),toModelCoords(p1));
        canvas.repaint();
    }

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

    public String nearestNeighbour(double px, double py) {
        return mapModel.nearestNeighbour(px,py);
    }

    public static void repaintMap() {
        canvas.repaint();
    }
}

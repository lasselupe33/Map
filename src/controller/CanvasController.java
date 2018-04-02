package controller;

import model.MainModel;
import view.CanvasView;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * This controller handles all logic and input related to the canvas that draws the map.
 */
public class CanvasController {
    private static CanvasController instance = new CanvasController();

    private CanvasView canvas;
    private AffineTransform transform = new AffineTransform();
    private boolean useAntiAliasing = false;
    private int zoomLevel = 0;

    private CanvasController() {}

    /**
     * @return the transform to be used in the canvasView
     */
    public AffineTransform getTransform() {
        return transform;
    }

    public static CanvasController getInstance() {
        return instance;
    }

    public void addCanvas(CanvasView c) { canvas = c; }

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

    public void updateMap(){
        Point2D p0 = new Point2D.Double(0,0);
        Point2D p1 = new Point2D.Double(canvas.getWidth(), canvas.getHeight());
        MainModel.updateMap(toModelCoords(p0),toModelCoords(p1));
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

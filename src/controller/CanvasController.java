package controller;

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

    public void addCanvas(CanvasView c) {
        canvas = c;
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
        pan(x, y);
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        pan(-x, -y);
        canvas.repaint();
    }
    
    /**
     * Helper that returns the corresponding model coordinates of a screen coordinate, based on the current transform.
     */
    public Point2D toModelCoords(Point2D p) {
        try {
            return transform.inverseTransform(p, null);
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        return null;
    }
}

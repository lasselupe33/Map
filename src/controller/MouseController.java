package controller;

import view.CanvasView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import static java.lang.Math.pow;
/**
 * This controller handles mouse events.
 */
public class MouseController extends MouseAdapter {
    private Model model;
    private CanvasView canvas;
    private CanvasController canvasController;
    private Point2D lastMousePosition;

    public MouseController(CanvasView c, Model m, CanvasController cc) {
        canvas = c;
        model = m;
        canvasController = cc;

        canvas.addMouseListener(this);
        canvas.addMouseWheelListener(this);
        canvas.addMouseMotionListener(this);
    }

    /**
     * Pan by dragging the mouse
     * @param e mouse dragged
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D currentMousePosition = e.getPoint();
        double dx = currentMousePosition.getX() - lastMousePosition.getX();
        double dy = currentMousePosition.getY() - lastMousePosition.getY();
        canvas.pan(dx, dy);
        lastMousePosition = currentMousePosition;
    }

    /**
     * Get position of mouse when pressed
     * @param e mouse pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePosition = e.getPoint();
    }

    public void mouseMoved(MouseEvent e) {
        Point2D modelCoords = canvasController.toModelCoords(e.getPoint());
        System.out.println("Screen: [" + e.getX() + ", " + e.getY() + "], " +
                "Model: [" + modelCoords.getX() + ", " + modelCoords.getY() + "]");
    }

    /**
     * Zoom with mouse wheel using wheel rotations
     * @param e mouse wheel rotated
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double factor = pow(1.1, e.getWheelRotation());
        canvas.zoom(factor, -e.getX(), -e.getY());

    }
}

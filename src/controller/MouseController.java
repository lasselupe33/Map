package controller;

import model.MainModel;
import view.CanvasView;
import view.DistanceCalculation;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.Timer;

import static java.lang.Math.pow;
/**
 * This controller handles mouse events.
 */
public class MouseController extends MouseAdapter {
    private CanvasView canvas;
    private CanvasController canvasController;
    private Point2D lastMousePosition;
    private static Thread t;

    public MouseController(CanvasView c, CanvasController cc) {
        canvas = c;
        canvasController = cc;
        canvas.addMouseListener(this);
        canvas.addMouseWheelListener(this);
        canvas.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Ensure keyboard events are used when the canvas has been pressed
        canvas.requestFocus();
    }

    /**
     * Pan by dragging the mouse
     * @param e mouse dragged
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        canvas.requestFocus();
        Point2D currentMousePosition = e.getPoint();
        double dx = currentMousePosition.getX() - lastMousePosition.getX();
        double dy = currentMousePosition.getY() - lastMousePosition.getY();
        canvasController.pan(dx, dy);
        lastMousePosition = currentMousePosition;
    }

    /**
     * Get position of mouse when pressed
     * @param e mouse pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        canvas.requestFocus();

        if (e.getClickCount() == 2) {
            canvasController.zoom(1.4, -e.getX(), -e.getY());
        }
        lastMousePosition = e.getPoint();
    }

    public void mouseMoved(MouseEvent e) {
        Point2D modelCoords = canvasController.toModelCoords(e.getPoint());
    }

    /**
     * Zoom with mouse wheel using wheel rotations
     * @param e mouse wheel rotated
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (t != null) t.interrupt();
        canvas.requestFocus();
        double factor = pow(1/1.1, e.getWheelRotation());
        canvasController.zoom(factor, -e.getX(), -e.getY());
    }

    public void mouseReleased(MouseEvent e){
        thread();
    }


    public static void thread(){
        t = new Thread() {
            public void run() {
                CanvasController.getInstance().updateMap();
            }
        };
        t.start();
    }
}

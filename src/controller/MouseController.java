package controller;

import model.Address;
import model.AddressesModel;
import model.Coordinates;
import view.CanvasView;
import view.FooterView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import static java.lang.Math.pow;
/**
 * This controller handles mouse events.
 */
public class MouseController extends MouseAdapter {
    private CanvasView canvas;
    private MapController mapController;
    private AddressesModel addressesModel;
    private FooterView footerView;

    private Point2D lastMousePosition;
    private static Thread t;

    public MouseController(CanvasView c, MapController cc, AddressesModel am, FooterView fv) {
        canvas = c;
        mapController = cc;
        addressesModel = am;
        footerView = fv;
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
        if (t != null) t.interrupt();
        canvas.requestFocus();
        Point2D currentMousePosition = e.getPoint();
        double dx = currentMousePosition.getX() - lastMousePosition.getX();
        double dy = currentMousePosition.getY() - lastMousePosition.getY();
        mapController.pan(dx, dy);
        lastMousePosition = currentMousePosition;
    }

    /**
     * Get position of mouse when pressed
     * @param e mouse pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        canvas.requestFocus();

        if (e.getButton() == MouseEvent.BUTTON3) {
            mapController.clearListOfLocations();
            Point2D modelCoords = mapController.toModelCoords(e.getPoint());
            Address address = addressesModel.nearestNeighbour(modelCoords.getX(), modelCoords.getY());
            mapController.addToListOfLocations(address);
        }
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            mapController.zoom(1.4, -e.getX(), -e.getY());
        }

        lastMousePosition = e.getPoint();
    }

    /**
     * Update the addresses being hovered on mousemove
     */
    public void mouseMoved(MouseEvent e) {
        Point2D modelCoords = mapController.toModelCoords(e.getPoint());
        footerView.updateHoverAddress(addressesModel.nearestNeighbour(modelCoords.getX(), modelCoords.getY()).toString());
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
        mapController.zoom(factor, -e.getX(), -e.getY());
    }

    public void mouseReleased(MouseEvent e){
        thread();
    }


    public static void thread(){
        t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    MapController.repaintMap();
                    return;
                }
                MapController.getInstance().updateMap();
            }
        };
        t.start();
    }
}

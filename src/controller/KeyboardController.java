package controller;

import model.MetaModel;
import view.MapView;
import view.MainWindowView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 * This controller handles keyboard events (pressed keys).
 */
public class KeyboardController extends KeyAdapter {
    private MetaModel model;
    private MainWindowView view;
    private MapView canvas;
    private MapController mapController;

    public KeyboardController(MainWindowView v, MapView c, MetaModel m, MapController cc) {
        view = v;
        canvas = c;
        model = m;
        mapController = cc;

        canvas.addKeyListener(this);
    }

    /**
     * Assign functionality to keys
     * @param e key pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'x':
                mapController.toggleAntiAliasing();
                break;
            case 'w':
                mapController.pan(0, 10); // up
                break;
            case 'a':
                mapController.pan(10, 0); // left
                break;
            case 's':
                mapController.pan(0, -10); // down
                break;
            case 'd':
                mapController.pan(-10, 0); // right
                break;
            case '+':
                mapController.zoomToCenter(1.1); // zoom in
                break;
            case '-':
                mapController.zoomToCenter(1/1.1); // zoom out
                break;
            default:
                break;
        }
    }
}

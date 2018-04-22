package controller;

import model.MetaModel;
import view.CanvasView;
import view.MainWindowView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 * This controller handles keyboard events (pressed keys).
 */
public class KeyboardController extends KeyAdapter {
    private MetaModel model;
    private MainWindowView view;
    private CanvasView canvas;
    private MapController mapController;

    public KeyboardController(MainWindowView v, CanvasView c, MetaModel m, MapController cc) {
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
                mapController.pan(0, 10);
                break;
            case 'a':
                mapController.pan(10, 0);
                break;
            case 's':
                mapController.pan(0, -10);
                break;
            case 'd':
                mapController.pan(-10, 0);
                break;
            case '+':
                mapController.zoomToCenter(1.1);
                break;
            case '-':
                mapController.zoomToCenter(1/1.1);
                break;
            default:
                break;
        }
    }
}

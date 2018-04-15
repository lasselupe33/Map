package controller;

import model.IOModel;
import model.MainModel;
import view.CanvasView;
import view.MainWindowView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 * This controller handles keyboard events (pressed keys).
 */
public class KeyboardController extends KeyAdapter {
    private MainModel model;
    private MainWindowView view;
    private CanvasView canvas;
    private CanvasController canvasController;
    private IOModel ioModel;

    public KeyboardController(MainWindowView v, CanvasView c, MainModel m, CanvasController cc, IOModel iom) {
        view = v;
        canvas = c;
        model = m;
        canvasController = cc;
        ioModel = iom;

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
                canvasController.toggleAntiAliasing();
                break;
            case 'w':
                canvasController.pan(0, 10);
                break;
            case 'a':
                canvasController.pan(10, 0);
                break;
            case 's':
                canvasController.pan(0, -10);
                break;
            case 'd':
                canvasController.pan(-10, 0);
                break;
            case '+':
                canvasController.zoomToCenter(1.1);
                break;
            case '-':
                canvasController.zoomToCenter(1/1.1);
                break;
            default:
                break;
        }
    }
}

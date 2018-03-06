package controller;

import java.awt.event.KeyEvent;

public class KeyboardController {
    private Model model;
    private MainWindowView view;
    private CanvasView canvas;

    public KeyboardController(MainWindowView v, CanvasView c, Model m) {
        view = v;
        canvas = c;
        model = m;

        view.window.addKeyListener(this);
        canvas.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w':
                canvas.pan(0, 10);
                break;
            case 'a':
                canvas.pan(10, 0);
                break;
            case 's':
                canvas.pan(0, -10);
                break;
            case 'd':
                canvas.pan(-10, 0);
                break;
            case '+':
                canvas.zoomToCenter(1.1);
                break;
            case '-':
                canvas.zoomToCenter(1/1.1);
                break;
            case 'l':
                canvas.toggleDrawingMode();
                canvas.repaint();
                break;
            case 'o':
                model.load("output.bin");
                break;
            case 'p':
                model.save("output.bin");
                break;
            default:
                break;
        }
    }
}

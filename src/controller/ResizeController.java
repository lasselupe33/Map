package controller;

import view.MainWindowView;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Timer;
import java.util.TimerTask;


public class ResizeController extends ComponentAdapter{
    private JFrame window;
    private MainWindowView mainWindow;

    /** Fields related to the debouncing of map updates */
    private java.util.Timer timer = new Timer();
    private Long lastAction;

    public ResizeController(MainWindowView mv){
        mainWindow = mv;
        window = mv.getWindow();
        window.addComponentListener(this);


    }

    /** Helper to be called every time a resize event occurs in order to ensure the GUI is responsive. */
    public void componentResized(ComponentEvent e) {
        lastAction = System.currentTimeMillis();

        // Only update the view after 100ms of inactivity
        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    if (System.currentTimeMillis() - lastAction >= 100) {
                        mainWindow.update();
                    }
                }
            },
            100
        );
    }
}

package view;

import controller.CanvasController;
import model.MainModel;

import javax.swing.*;
import java.awt.*;

/**
 * This class creates the main window to display the map in.
 */
public class MainWindowView {
    public JFrame window;

    public MainWindowView(CanvasView cv, MainModel m, CanvasController cc) {
        window = new JFrame("Line Visualiser");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cv.setPreferredSize(new Dimension(500, 500));
        window.add(cv, BorderLayout.CENTER);
        window.pack();
        window.setVisible(true);
        // put screen to correct place on canvas
        cc.pan(-m.getMinLon(), -m.getMaxLat());
        cc.zoom(window.getWidth() / (m.getMaxLon() - m.getMinLon()), 0, 0);
    }
}

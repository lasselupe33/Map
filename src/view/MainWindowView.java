package view;

import model.MainModel;

import javax.swing.*;
import java.awt.*;

public class MainWindowView {
    public JFrame window;

    public MainWindowView(CanvasView cv, MainModel m) {
        window = new JFrame("Line Visualiser");
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cv.setPreferredSize(new Dimension(500, 500));
        window.add(cv, BorderLayout.CENTER);
        window.pack();
        window.setVisible(true);
        // put screen to correct place on canvas
        cv.pan(-m.getMinLon(), -m.getMaxLat());
        cv.zoom(window.getWidth() / (m.getMaxLon() - m.getMinLon()), 0, 0);
    }
}

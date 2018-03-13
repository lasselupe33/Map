package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ZoomView extends JPanel {
    private JButton zoomIn;
    private JButton zoomOut;

    public ZoomView(){
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        createZoomButtons();

    }

    public void createZoomButtons(){
        zoomIn = new JButton("+");
        zoomOut = new JButton("-");
        add(zoomIn);
        add(Box.createVerticalStrut(10));
        add(zoomOut);
    }
}

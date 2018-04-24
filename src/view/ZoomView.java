package view;

import controller.MapController;

import javax.swing.*;
import java.awt.*;

public class ZoomView extends JPanel {
    private JButton zoomIn;
    private JButton zoomOut;
    private int fontSize = 20;
    private String fontFamily = "Myriad Pro";
    private int buttonBorderSize = 50;
    private MapController mapController;

    public ZoomView(MapController cc){
        mapController = cc;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        createZoomButtons();
    }

    public void createZoomButtons(){
        zoomIn = new JButton("+");
        zoomIn.setBackground(Color.WHITE);
        zoomIn.setOpaque(true);
        zoomIn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        zoomIn.setMaximumSize(new Dimension(buttonBorderSize, buttonBorderSize));
        zoomIn.setFont(new Font(fontFamily, Font.PLAIN, fontSize));
        zoomIn.addActionListener((e) -> mapController.zoomToCenter(1.25));


        zoomOut = new JButton("-");
        zoomOut.setBackground(Color.WHITE);
        zoomOut.setOpaque(true);
        zoomOut.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        zoomOut.setMaximumSize(new Dimension(buttonBorderSize, buttonBorderSize));
        zoomOut.setFont(new Font(fontFamily, Font.PLAIN, fontSize));
        zoomOut.addActionListener((e) -> mapController.zoomToCenter(1/1.25));

        add(zoomIn);
        add(Box.createVerticalStrut(10));
        add(zoomOut);

    }
}

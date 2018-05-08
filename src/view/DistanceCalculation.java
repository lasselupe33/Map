package view;

import controller.MapController;
import helpers.UnitConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class DistanceCalculation extends JComponent {
    private MapController mapController;
    private final int screenDistance = 100;
    private Point2D startModelPoint;
    private Point2D endModelPoint;
    private double distance;
    private String text;

    public DistanceCalculation(MapController cc){
        mapController = cc;
        setPreferredSize(new Dimension(200, 30));
    }

    public void paint(Graphics _g){
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.decode("#383838"));

        //Distance calculation
        distance = UnitConverter.PxToKm(100);

        //Distance Text
        if(distance > 1){
            text = Math.round(distance*100.0)/100.0 + "km";
        } else if(distance > 0.001){
            text = Math.round(distance*1000) + "m";
        } else {
            text = "under 1m";
        }

        // Draw text
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, 200 - screenDistance - 20 - textWidth, 20);

        // Draw scale
        g.fillRect(getWidth() - screenDistance - 10, 18, screenDistance, 2);

        // Draw scale edges
        g.fillRect(getWidth() - screenDistance - 10, 12, 2, 6);
        g.fillRect(getWidth() - 12, 12, 2, 6);
    }
}
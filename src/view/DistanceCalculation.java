package view;

import controller.CanvasController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static java.lang.StrictMath.sqrt;

public class DistanceCalculation extends JComponent {
    private CanvasController canvasController;
    private final int screenDistance = 100;
    private Point2D startModelPoint;
    private Point2D endModelPoint;
    private double distance;
    private String text;

    public DistanceCalculation(CanvasController cc){
        canvasController = cc;
        JTextField test = new JTextField("hej");
        setLayout(new BorderLayout());
        add(test, BorderLayout.EAST);

    }

    public void paint(Graphics _g){
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.BLACK);


        //Distance calculation
        startModelPoint =  canvasController.toModelCoords(new Point(0, 10));
        endModelPoint = canvasController.toModelCoords(new Point(screenDistance, 10));
        distance = getDistance(startModelPoint.getX(), startModelPoint.getY(), endModelPoint.getX(), endModelPoint.getY());

        //Distance Text
        if(distance > 1){
            text = Math.round(distance*100.0)/100.0 + "km";
        } else if(distance > 0.01){
            text = Math.round(distance*1000) + "m";
        } else {
            text = "under 1m";
        }

        g.drawString(text, 0, 20);


        //DistanceIcon painting
        int textwidth = g.getFontMetrics().stringWidth(text);
        g.fillRect(textwidth+2, 12, screenDistance, 8);







    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2){
        double R = 6371;
        double dLat = toRadians(lat2-lat1);
        double dLon = toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(toRadians(lat1))*Math.cos(toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }

    private double toRadians(double degrees){
        return degrees * (Math.PI/180);
    }



}
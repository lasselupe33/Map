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

    public DistanceCalculation(CanvasController cc){
        canvasController = cc;
    }

    public void paint(Graphics _g){
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.red);
        g.fillRect(0, 0, screenDistance, 10);
        startModelPoint =  canvasController.toModelCoords(new Point(0, 10));
        endModelPoint = canvasController.toModelCoords(new Point(screenDistance, 10));
        distance = getDistance(startModelPoint.getX(), startModelPoint.getY(), endModelPoint.getX(), endModelPoint.getY());
        System.out.println(distance);

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
package view;

import controller.CanvasController;
import model.MainModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

public class DistanceCalculation extends JComponent {
    private CanvasController canvasController;
    private final int x1 = 10;
    private final int x2 = 110;
    private double distance = 0;
    private Point2D start;
    private Point2D end;

    public DistanceCalculation(CanvasController cc){
        canvasController = cc;
    }

    public void paint(Graphics _g){
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.red);
        g.fillRect(0, 0, 100, 10);
        start =  canvasController.toModelCoords(new Point(x1, 10));
        end = canvasController.toModelCoords(new Point(x2, 10));
        distance = end.getX()-start.getX();
        System.out.println(distance);

    }




}
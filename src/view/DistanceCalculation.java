package view;

import controller.CanvasController;
import model.MainModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

class DistanceCalculation extends JComponent {
    private CanvasController canvasController;
    private MainModel mainModel;

    public DistanceCalculation(CanvasController cc, MainModel mm){
        canvasController = cc;


    }

    public void paint(Graphics _g){
        Graphics2D g = (Graphics2D) _g;
        g.setColor(Color.red);
        g.fillRect(0, 0, 100, 10);
    }



}
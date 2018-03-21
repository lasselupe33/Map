package model;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapElements {
    private Shape shape;
    private double x, y;

    public MapElements(Shape s){
        shape = s;
        Rectangle2D r = shape.getBounds2D();
        x = r.getX();
        y = r.getY();
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

}

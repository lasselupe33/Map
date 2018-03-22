package model;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapElements {
    private Shape shape;
    private double x, y;
    private Rectangle2D r;

    public MapElements(Shape s){
        shape = s;
        r = shape.getBounds2D();
        x = r.getX();
        y = r.getY();
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public Rectangle2D getBounds(){
        return r;
    }

    public Shape getShape(){
        return shape;
    }

}

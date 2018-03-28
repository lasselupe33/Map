package model;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapElement {
    private Shape shape;

    private double x, y;
    private Rectangle2D r;

    public MapElement(Shape s){
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

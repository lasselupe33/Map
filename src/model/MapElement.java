package model;

import model.osm.OSMWayType;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapElement {
    private Shape shape;
    private double x, y;
    private Rectangle2D r;
    private OSMWayType type;

    public MapElement(Shape s, OSMWayType t){
        shape = s;
        type = t;
        r = shape.getBounds2D();
        x = r.getX();
        y = r.getY();
    }

    public double getElementX(){
        return x;
    }

    public double getElementY(){
        return y;
    }

    public Rectangle2D getBounds(){
        return r;
    }

    public OSMWayType getType() { return type; }

    public Shape getShape(){
        return shape;
    }
}

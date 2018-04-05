package model.MapElements;

import model.osm.OSMWayType;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapElement {
    private Shape shape;
    private double x, y;
    private Rectangle2D r;
    private OSMWayType type;
    boolean shouldFill;

    public MapElement(Shape s, OSMWayType t, boolean sf){
        shape = s;
        type = t;
        shouldFill = sf;
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

    public boolean shouldFill() { return shouldFill; }

    public Shape getShape(){ return shape; }
}

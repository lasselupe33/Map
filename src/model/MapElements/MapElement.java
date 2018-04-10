package model.MapElements;

import model.osm.OSMWayType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class MapElement implements Serializable {
    private Shape shape;
    private transient double x, y;
    private transient Rectangle2D r;
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
        if (x == 0.0) {
            x = getBounds().getX();
        }

        return x;
    }

    public double getElementY(){
        if (y == 0.0) {
            y = getBounds().getY();
        }

        return y;
    }

    public Rectangle2D getBounds(){
        if (r == null) {
            r = shape.getBounds2D();
        }

        return r;
    }

    public OSMWayType getType() { return type; }

    public boolean shouldFill() { return shouldFill; }

    public Shape getShape(){ return shape; }
}

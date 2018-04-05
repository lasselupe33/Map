package model;

import model.osm.OSMWayType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class MapElement implements Serializable {
    private Shape shape;
    private transient double x, y;
    private transient Rectangle2D r;
    private OSMWayType type;

    public MapElement(Shape s, OSMWayType t){
        shape = s;
        type = t;
        r = shape.getBounds2D();
        x = r.getX();
        y = r.getY();
    }

    public double getElementX(){
        if ((Double) x == null) {
            x = r.getX();
        }

        return x;
    }

    public double getElementY(){
        if ((Double) y == null) {
            y = r.getY();
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

    public Shape getShape(){
        return shape;
    }
}

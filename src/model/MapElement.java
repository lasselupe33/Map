package model;

import model.osm.OSMWayType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class MapElement extends Coordinates implements Serializable {
    private Shape shape;
    private transient Rectangle2D r;
    private OSMWayType type;
    boolean shouldFill;

    public MapElement(double x, double y, Shape s, OSMWayType t, boolean sf){
        super(x, y);
        shape = s;
        type = t;
        shouldFill = sf;
        r = shape.getBounds2D();
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

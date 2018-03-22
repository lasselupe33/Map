package model.MapElements;

import model.Address;
import model.osm.OSMWayType;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapElement {
    private Shape shape;
    private Rectangle2D rect;
    private double x, y;
    private OSMWayType type;
    Boolean shouldFill;

    MapElement(OSMWayType type, Shape s){
        shape = s;
        rect = shape.getBounds2D();
        x = rect.getX();
        y = rect.getY();
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public Rectangle2D getRect() { return rect; }

}

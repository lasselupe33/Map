package model;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.*;

public class MapElement extends Coordinates implements Externalizable {
    private Shape shape;
    private Rectangle2D r;
    private WayType type;
    boolean shouldFill;

    public MapElement() {}
    public MapElement(double x, double y, Shape s, WayType t, boolean sf){
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

    public WayType getType() { return type; }

    public boolean shouldFill() { return shouldFill; }

    public Shape getShape(){ return shape; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(this.getX());
        out.writeDouble(this.getY());
        out.writeObject(shape);
        out.writeObject(type);
        out.writeBoolean(shouldFill);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setX(in.readDouble());
        this.setY(in.readDouble());
        shape = (Shape) in.readObject();
        type = (WayType) in.readObject();
        shouldFill = in.readBoolean();
    }
}

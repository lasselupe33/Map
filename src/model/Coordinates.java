package model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/** Simple class that contains latitude and longitude coordinates */
public class Coordinates implements Externalizable {
    private Double x, y;

    public Coordinates() {}
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** Getters */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.readDouble();
        y = in.readDouble();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
    }
}
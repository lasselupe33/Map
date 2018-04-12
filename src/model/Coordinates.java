package model;

import java.io.Serializable;

/** Simple class that contains latitude and longitude coordinates */
public class Coordinates implements Serializable {
    private Double x, y;

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
}
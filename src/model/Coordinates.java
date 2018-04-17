package model;

import java.io.Serializable;

/** Simple class that contains latitude and longitude coordinates */
public class Coordinates implements Serializable {
    private Double x, y;

    public Coordinates(double y, double x) {
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
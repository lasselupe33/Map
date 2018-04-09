package model;

import java.io.Serializable;

/** Simple class that contains latitude and longitude coordinates */
public class Coordinates implements Serializable {
    private Double lat, lon;

    public Coordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /** Getters */
    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
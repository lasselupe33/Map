package model;

import java.io.*;

public class MainModel implements Serializable{
    private Addresses addresses = new Addresses();
    private double minLat, minLon, maxLat, maxLon;

    public MainModel(){}

    /** Getters */
    public Addresses getAddresses() {
        return addresses;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMaxLon() {
        return maxLon;
    }

    /** Setters */
    public void setAddresses(Addresses addresses) {
        this.addresses = addresses;
    }

    public void setMinLat(double minLat){this.minLat = minLat;}

    public void setMinLon(double minLon){this.minLon = minLon;}

    public void setMaxLat(double maxLat){this.maxLat = maxLat;}

    public void setMaxLon(double maxLon){this.maxLon = maxLon;}
}

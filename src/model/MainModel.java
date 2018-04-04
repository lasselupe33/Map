package model;

import model.osm.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class MainModel implements Serializable{
    private Addresses addresses = new Addresses();
    private EnumMap<OSMWayType, List<Shape>> shapes = initializeMap();
    public double minLat, minLon, maxLat, maxLon;

    public MainModel(){}

    private EnumMap<OSMWayType, List<Shape>> initializeMap() {
        EnumMap<OSMWayType, List<Shape>> map = new EnumMap<OSMWayType, List<Shape>>(OSMWayType.class);
        for (OSMWayType type: OSMWayType.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }

    public void add(OSMWayType type, Shape shape) {
        shapes.get(type).add(shape);
    }

    public Iterable<Shape> get(OSMWayType type) {
        return shapes.get(type);
    }

    /** Getters */
    public Addresses getAddresses() {
        return addresses;
    }

    public EnumMap<OSMWayType, List<Shape>> getShapes() {
        return shapes;
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

    /** Settters */
    public void setAddresses(Addresses addresses) {
        this.addresses = addresses;
    }

    public void setShapes(EnumMap<OSMWayType, List<Shape>> shapes) {
        this.shapes = shapes;
    }

    public void setMinLat(double minLat){this.minLat = minLat;}

    public void setMinLon(double minLon){this.minLon = minLon;}

    public void setMaxLat(double maxLat){this.maxLat = maxLat;}

    public void setMaxLon(double maxLon){this.maxLon = maxLon;}
}

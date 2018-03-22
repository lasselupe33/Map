package model.MapElements;

import model.Address;
import model.osm.OSMWayType;

import java.awt.*;

public class Road extends MapElement {
    private String streetName;
    public Road(OSMWayType type, Shape shape, String name) {
        super(type, shape);
        shouldFill = false;
        streetName = name;
    }
}

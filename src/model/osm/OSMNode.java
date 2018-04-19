package model.osm;

import model.Coordinates;
import model.graph.Edge;

public class OSMNode {
    private Coordinates coords;

    public OSMNode(double lon, double lat) {
        coords = new Coordinates(lat, lon);
    }

    public double getLon() { return coords.getX(); }

    public double getLat() { return coords.getY(); }

}

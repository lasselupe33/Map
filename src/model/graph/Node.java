package model.graph;

import model.Coordinates;
import model.osm.OSMNode;

import java.util.ArrayList;

public class Node {
    private Coordinates coords;
    ArrayList<Edge> edges = new ArrayList<>();
    private double distTo;
    private Node parent;

    public Node(double lon, double lat) {
        coords = new Coordinates(lat, lon);
        distTo = Double.POSITIVE_INFINITY;
        parent = null;
    }

    public double getLon() { return coords.getLon(); }

    public double getLat() { return coords.getLat(); }

    public double getDistTo() { return distTo; }

    public Node getParent() { return parent; }

    public void addEdge(Edge edge) { edges.add(edge); }
}
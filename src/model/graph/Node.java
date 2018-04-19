package model.graph;

import model.Coordinates;
import model.osm.OSMNode;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {
    private float lat;
    private float lon;
    ArrayList<Edge> edges = new ArrayList<>();
    private float distTo;
    private Node parent;
    private boolean addedToTree = false;

    public Node(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
        distTo = Float.POSITIVE_INFINITY;
        parent = null;
    }

    public float getLon() { return lat; }

    public float getLat() { return lon; }

    public void setAddedToTree() {
        addedToTree = true;
    }

    public void setDistToSource(float newDist) {
        distTo = newDist;
    }
    public float getDistToSource() { return distTo; }

    public Node getParent() { return parent; }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public ArrayList<Edge> getEdges() { return edges; }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean addedToTree() {
        return addedToTree;
    }

    public String toKey() { return lat + "-" + lon; }
}
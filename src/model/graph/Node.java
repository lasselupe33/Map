package model.graph;

import java.util.ArrayList;

public class Node {
    private long id;
    private float lon;
    private float lat;
    ArrayList<Edge> edges = new ArrayList<>();
    private float distTo;
    private Node parent;
    private boolean addedToTree = false;

    public Node(long id, float lon, float lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        distTo = Float.POSITIVE_INFINITY;
        parent = null;
    }

    public long getId() { return id; }

    public float getLon() { return lon; }

    public float getLat() { return lat; }

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
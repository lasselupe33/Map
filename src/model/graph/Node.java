package model.graph;

import java.util.ArrayList;

public class Node {
    private long id;
    private float lon;
    private float lat;
    private ArrayList<Edge> edges;
    private float distTo;
    private float timeTo;
    private Node parent;
    private boolean addedToTree = false;
    private float estimateToDest;

    public Node(long id, float lon, float lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        edges = new ArrayList<>();
        distTo = Float.POSITIVE_INFINITY;
        timeTo = Float.POSITIVE_INFINITY;
        parent = null;
    }

    // Getters
    public long getId() { return id; }

    public float getLon() { return lon; }

    public float getLat() { return lat; }

    // For navigation
    public void setAddedToTree() {
        addedToTree = true;
    }

    public void setDistToSource(float newDist) {
        distTo = newDist;
    }

    public float getDistToSource() { return distTo; }

    public void setTimeToSource(float newTime) { timeTo = newTime; }

    public float getTimeToSource() { return timeTo; }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() { return parent; }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public ArrayList<Edge> getEdges() { return edges; }

    public boolean addedToTree() {
        return addedToTree;
    }

    public void setEstimateToDest(float estimate) { estimateToDest = estimate; }

    public float getEstimateToDest(RouteType rType, VehicleType vType) {
        if (rType == RouteType.SHORTEST) {
            return estimateToDest;
        } else {
            switch (vType) {
                case PEDESTRIAN:
                    return estimateToDest / 5;
                case BICYCLE:
                    return estimateToDest / 18;
                default:
                    break;
            }
            return estimateToDest / 80;
        }
    }

    public String toKey() { return lat + "-" + lon; }
}
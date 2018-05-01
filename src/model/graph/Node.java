package model.graph;

import parsing.OSMNode;

import java.util.ArrayList;

public class Node extends OSMNode {
    private ArrayList<Edge> edges;
    private float lengthTo;
    private float timeTo;
    private Node parent;
    private float estimateToDest;

    public Node(long id, float lon, float lat) {
        super(id, lon, lat);
        edges = new ArrayList<>();
        lengthTo = Float.POSITIVE_INFINITY;
        timeTo = Float.POSITIVE_INFINITY;
        parent = null;
    }

    /**
     * The distance to source depends on the type of route desired.. I.e. if we want the fastest route we need to
     * get the time to source.
     */
    public float getDistToSource(RouteType routeType) {
        if (routeType == RouteType.FASTEST) {
            return timeTo;
        } else {
            return lengthTo;
        }
    }

    public float getTimeToSource() {
        return timeTo;
    }

    public float getLengthToSource() {
        return lengthTo;
    }

    public void setLengthToSource(float newLength) { lengthTo = newLength; }

    public void setTimeToSource(float newTime) { timeTo = newTime; }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() { return parent; }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public ArrayList<Edge> getEdges() { return edges; }

    public void reset() {
        parent = null;
        lengthTo = Float.POSITIVE_INFINITY;
        timeTo = Float.POSITIVE_INFINITY;
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

    public String toKey() { return super.getLat() + "-" + super.getLon(); }
}
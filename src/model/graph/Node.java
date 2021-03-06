package model.graph;

import helpers.UnitConverter;
import parsing.OSMNode;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

/**
 * A node is a class that contains a single vertex of the graph and its required info
 */
public class Node extends OSMNode implements Externalizable {
    private Integer[] edges;
    private ArrayList<Integer> tempEdges; // Contains edges until map has been completely parsed
    private float lengthTo;
    private float timeTo;
    private Integer parentEdge;
    private float estimateToDest;

    public Node() {}
    public Node(long id, float lon, float lat) {
        super(id, lon, lat);
        tempEdges = new ArrayList<>();
        lengthTo = Float.POSITIVE_INFINITY;
        timeTo = Float.POSITIVE_INFINITY;
        parentEdge = null;
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

    public void initialize() {
        tempEdges = new ArrayList<>();
    }

    public float getTimeToSource() {
        return timeTo;
    }

    public float getLengthToSource() {
        return lengthTo;
    }

    public void setLengthToSource(float newLength) { lengthTo = newLength; }

    public void setTimeToSource(float newTime) { timeTo = newTime; }

    public void setParentEdge(Integer parentId) {
        this.parentEdge = parentId;
    }

    public Integer getParentEdge() { return parentEdge; }

    public void addEdge(int edge) {
        tempEdges.add(edge);
    }

    public Integer[] getEdges() { return edges; }

    public ArrayList<Integer> getTempEdges() { return tempEdges; }

    /**
     * Reset node
     * @param dest
     */
    public void reset(Node dest) {
        parentEdge = null;
        lengthTo = Float.POSITIVE_INFINITY;
        timeTo = Float.POSITIVE_INFINITY;
        estimateToDest = (float) UnitConverter.DistInMM(getLat(), getLon(), dest.getLat(), dest.getLon());
    }

    /**
     * Set estimated destiance to destination
     * @param estimate the estimate
     */
    public void setEstimateToDest(float estimate) { estimateToDest = estimate; }

    /**
     * Get estimated distance or time to destination based on route and vehicle type
     * @param rType route type (shortest or fastest)
     * @param vType vehicle type (car, cycle or pedestrian)
     * @return estimated distance or time to destination
     */
    public float getEstimateToDest(RouteType rType, VehicleType vType) {
        if (rType == RouteType.SHORTEST) {
            return estimateToDest;
        } else {
            switch (vType) {
                case CAR:
                    return estimateToDest * 60 / 130;

                case BICYCLE:
                    return estimateToDest * 60 / 18;

                case PEDESTRIAN:
                    return estimateToDest * 60 / 5;

                default:
                    return estimateToDest;
            }
        }
    }

    public String toKey() { return super.getLat() + "-" + super.getLon(); }

    public void finalizeEdges() {
        edges = tempEdges.toArray(new Integer[tempEdges.size()]);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(super.getId());
        out.writeFloat(super.getLat());
        out.writeFloat(super.getLon());
        out.writeObject(edges);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.setId(in.readLong());
        super.setLat(in.readFloat());
        super.setLon(in.readFloat());
        lengthTo = Float.POSITIVE_INFINITY;
        timeTo = Float.POSITIVE_INFINITY;
        edges = (Integer[]) in.readObject();
    }
}
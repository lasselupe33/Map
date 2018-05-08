package model.graph;

import model.Coordinates;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

public class Edge implements Externalizable {
    private long node1;
    private long node2;
    private String name;
    private float length;
    private int speedLimit;
    private Coordinates[] path;
    private boolean supportsCars;
    private boolean supportsBicycles;
    private boolean supportsPedestrians;

    public Edge() {}
    public Edge(long node1, long node2, ArrayList<Coordinates> path, String name, float length, int speedLimit, boolean supportsCars, boolean supportsBicycles, boolean supportsPedestrians) {
        this.node1 = node1;
        this.node2 = node2;
        this.length = length;
        this.name = name;
        this.speedLimit = speedLimit;
        this.supportsCars = supportsCars;
        this.supportsBicycles = supportsBicycles;
        this.supportsPedestrians = supportsPedestrians;
        this.path = path.toArray(new Coordinates[path.size()]);
    }

    /** Get the weight of the edge based on the type of vehicle and the desired route type */
    public float getWeight(VehicleType vtype, RouteType rtype) {
        if (rtype == RouteType.FASTEST) {
            // If the fastest route is desired, then factor in the time to travel the road
            return getTime(vtype);
        } else {
            return getLength();
        }
    }

    /**
     * Returns the time it takes to travel across the edge.
     *
     * Time to travel will be returned in micro-seconds in order to ensure numbers large enough to compute path properly.
     *
     * NB: We assume that people walk with 5km/t and bicycle with 18km/t
     * http://www.naturli.dk/artikel/6-forskellige-mader-at-ga-pa/
     *
     */
    public float getTime(VehicleType type) {
        switch (type) {
            case PEDESTRIAN:
                return length * 60 / 5;
            case BICYCLE:
                return length * 60 / 18;
            default:
                // Default will always be car
                return length * 60 / speedLimit;
        }
    }

    /**
     * Returns the length of the edge in millimeters
     */
    public float getLength() {
        return length;
    }


    /**
     * Returns the opposite node of the edge from the one passed.
     * NB: from must be one of the two nodes of the edge.
     */
    public long getTo(Node from) {
        if (from.getId() == node1) {
            return node2;
        } else if (from.getId() == node2) {
            return node1;
        } else {
            throw new RuntimeException("The 'from' node must be one of the nodes of the current edge!");
        }
    }

    /** Returns whether or not the passed VehicleType is supported by this edge */
    public boolean supportsType(VehicleType type) {
        switch (type) {
            case CAR:
                return supportsCars;

            case BICYCLE:
                return supportsBicycles;

            case PEDESTRIAN:
                return supportsPedestrians;

            default:
                // No other types are supported.
                return false;
        }
    }

    public Coordinates[] getPath() { return path; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(node1);
        out.writeLong(node2);
        out.writeFloat(length);
        out.writeInt(speedLimit);
        out.writeBoolean(supportsCars);
        out.writeBoolean(supportsBicycles);
        out.writeBoolean(supportsPedestrians);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        node1 = in.readLong();
        node2 = in.readLong();
        length = in.readFloat();
        speedLimit = in.readInt();
        supportsCars = in.readBoolean();
        supportsBicycles = in.readBoolean();
        supportsPedestrians = in.readBoolean();
    }
}

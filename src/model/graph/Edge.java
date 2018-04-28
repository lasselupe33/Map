package model.graph;

public class Edge {
    private Node node1;
    private Node node2;
    private float length; //in km
    private int speedLimit;
    private boolean supportsCars;
    private boolean supportsBicycles;
    private boolean supportsPedestrians;

    public Edge(Node node1, Node node2, float length, int speedLimit, boolean supportsCars, boolean supportsBicycles, boolean supportsPedestrians) {
        this.node1 = node1;
        this.node2 = node2;
        this.length = length;
        this.speedLimit = speedLimit;
        this.supportsCars = supportsCars;
        this.supportsBicycles = supportsBicycles;
        this.supportsPedestrians = supportsPedestrians;
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

    /** Returns the time it takes to travel across the edge */
    public float getTime(VehicleType type) {
        switch (type) {
            case PEDESTRIAN:
                return length*1000/5;
            case BICYCLE:
                return length*1000/18;
            default:
                // Default will always be car
                return length * 1000 / speedLimit;
        }
    }

    /** Returns the length of the edge */
    public float getLength() {
        return length * 1000;
    }


    /**
     * Returns the opposite node of the edge from the one passed.
     * NB: from must be one of the two nodes of the edge.
     */
    public Node getTo(Node from) {
        if (from.getId() == node1.getId()) {
            return node2;
        } else if (from.getId() == node2.getId()) {
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
}

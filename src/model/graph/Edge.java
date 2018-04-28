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

    public float getTime(VehicleType type) {
        switch (type) {
            case PEDESTRIAN:
                return length*1000/5;
            case BICYCLE:
                return length*1000/18;
            default:
                break;
        }
        return length*1000/speedLimit;
    }

    public Node getTo(Node from) {
        if (from.getId() == node1.getId()) {
            return node2;
        } else {
            return node1;
        }
    }

    public float getLength() {
        System.out.println(length / speedLimit);
        return length * 1000;
    }

    public boolean supportsCars() { return supportsCars;}
    public boolean supportsBicycles() { return supportsBicycles;}
    public boolean supportsPedestrians() { return supportsPedestrians; }
}

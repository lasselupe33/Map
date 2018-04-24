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

    public float time() {  return length/speedLimit; }

    public Node getTo(Node from) {
        if (from == node1) {
            return node2;
        } else {
            return node1;
        }
    }

    public float getLength() {
        return length;
    }
}

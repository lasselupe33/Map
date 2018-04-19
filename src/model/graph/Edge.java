package model.graph;

public class Edge {
    private Node from;
    private Node to;
    private float length; //in km
    private int speedLimit;
    private boolean supportsCars;
    private boolean supportsBicycles;
    private boolean supportsPedestrians;

    public Edge(Node from, Node to, float length, int speedLimit, boolean supportsCars, boolean supportsBicycles, boolean supportsPedestrians) {
        this.from = from;
        this.to = to;
        this.length = length;
        this.speedLimit = speedLimit;
        this.supportsCars = supportsCars;
        this.supportsBicycles = supportsBicycles;
        this.supportsPedestrians = supportsPedestrians;
    }

    public float time() {  return length/speedLimit; }

    public Node getTo() { return to; }

    public float getLength() {
        return length;
    }
}

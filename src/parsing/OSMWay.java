package parsing;

import java.util.ArrayList;

public class OSMWay extends ArrayList<OSMNode>{
    private long id;
    private int speedLimit;
    private boolean supportsCars;
    private boolean supportsBicycles;
    private boolean supportPedestrians;

    public OSMWay() {}
    public OSMWay(long id) {
        this.id = id;
    }

    public void addWayInfo(int sl, boolean c, boolean b, boolean p) {
        speedLimit = sl;
        supportsCars = c;
        supportsBicycles = b;
        supportPedestrians = p;
    }

    public OSMNode from() {
        return get(0);
    }

    public OSMNode to() {
        return get(size() - 1);
    }

    public ArrayList<OSMNode> getNodes() { return this; }

    public long getId() {
        return id;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public boolean supportsCars() {
        return supportsCars;
    }

    public boolean supportsBicycles() {
        return supportsBicycles;
    }

    public boolean supportPedestrians() {
        return supportPedestrians;
    }
}

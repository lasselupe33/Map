package model.osm;

import java.util.ArrayList;

public class OSMWay extends ArrayList<OSMNode>{
    private double xCoord;
    private double yCoord;
    private ArrayList<OSMNode> nodes = new ArrayList<>();

    public OSMWay() {
        OSMNode startNode = nodes.get(0);
        xCoord = startNode.getLon();
        yCoord = startNode.getLat();
    }

    public OSMNode from() {
        return nodes.get(0);
    }

    public OSMNode to() {
        return nodes.get(size() - 1);
    }
}

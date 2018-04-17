package model.osm;

import model.graph.Node;
import java.util.ArrayList;

public class OSMWay extends ArrayList<Node>{
    private double xCoord;
    private double yCoord;



    public OSMWay() {
    }

    public Node from() {
        return get(0);
    }

    public Node to() {
        return get(size() - 1);
    }

    public ArrayList<Node> getNodes() { return this;}

}

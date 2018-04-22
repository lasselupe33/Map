package parsing;

import parsing.OSMNode;

import java.util.ArrayList;

public class OSMWay extends ArrayList<OSMNode>{
    private float xCoord;
    private float yCoord;

    public OSMWay() {
    }

    public OSMNode from() {
        return get(0);
    }

    public OSMNode to() {
        return get(size() - 1);
    }

}
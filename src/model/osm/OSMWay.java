package model.osm;

import java.util.ArrayList;

public class OSMWay extends ArrayList<OSMNode>{
    private double xCoord;
    private double yCoord;



    public OSMWay() {
    }

    public OSMNode from() {
        return get(0);
    }

    public OSMNode to() {
        return get(size() - 1);
    }

}

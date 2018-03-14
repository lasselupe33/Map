package model.osm;

import java.util.ArrayList;

public class OSMWay extends ArrayList<OSMNode>{
    private double xCoord;
    private double yCoord;

    private double avgLat;
    private double avgLon;

    public OSMWay() {
    }

    public OSMNode from() {
        return get(0);
    }

    public OSMNode to() {
        return get(size() - 1);
    }

    public void setAvgLatandLon(double lat, double lon){
        avgLat = lat; avgLon = lon;
    }

    public double getAvgLat(){
        return avgLat;
    }

    public double getAvgLon(){
        return avgLon;
    }
}

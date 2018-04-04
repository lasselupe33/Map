package model;

import controller.CanvasController;
import helpers.KDTree;
import model.osm.*;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.List;

public class MainModel implements Serializable{
    private Addresses addresses = new Addresses();
    private EnumMap<OSMWayType, List<MapElement>> mapelements = initializeMap();
    private double minLat, minLon, maxLat, maxLon;
    private static KDTree tree;
    private static List<MapElement> maplist = new ArrayList<>();

    public MainModel(){
        tree = new KDTree(this);
    }

    public static void updateMap(Point2D p0, Point2D p1){
        int zoom = CanvasController.getInstance().getZoomLevel();

        maplist.clear();
        for (OSMWayType e : OSMWayType.values()) {
            if ( zoom > e.getZoomValue() ) maplist.addAll(tree.searchTree(p0, p1, e));
        }
    }

    private static EnumMap<OSMWayType, List<MapElement>> initializeMap() {
        EnumMap<OSMWayType, List<MapElement>> map = new EnumMap<>(OSMWayType.class);
        for (OSMWayType type: OSMWayType.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }

    public void add(OSMWayType type, MapElement m) {
        mapelements.get(type).add(m);
    }

    /** Getters */
    public Addresses getAddresses() {
        return addresses;
    }

    public List<MapElement> getTreeData(){
        return maplist;
    }

    public List<MapElement> get(OSMWayType type) {
        return mapelements.get(type);
    }

    public EnumMap<OSMWayType, List<MapElement>> getMapElements() {
        return mapelements;
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMaxLon() {
        return maxLon;
    }

    /** Settters */
    public void setAddresses(Addresses addresses) {
        this.addresses = addresses;
    }

    public void setMapElements(EnumMap<OSMWayType, List<MapElement>> mapElements) {
        this.mapelements = mapElements;
    }

    public void setMinLat(double minLat){this.minLat = minLat;}

    public void setMinLon(double minLon){this.minLon = minLon;}

    public void setMaxLat(double maxLat){this.maxLat = maxLat;}

    public void setMaxLon(double maxLon){this.maxLon = maxLon;}
}

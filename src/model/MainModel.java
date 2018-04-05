package model;

import controller.CanvasController;
import model.osm.*;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.List;

public class MainModel implements Serializable{
    private Addresses addresses = new Addresses();
    private static EnumMap<ZoomLevel, List<MapElement>> mapelements = initializeMap();
    private double minLat, minLon, maxLat, maxLon;
    private static KDTree tree;
    private static List<MapElement> maplist = new ArrayList<>();

    public MainModel(){}

    public void createTree() { tree = new KDTree(this); }

    public static void updateMap(Point2D p0, Point2D p1){
        int zoom = CanvasController.getInstance().getZoomLevel();

        maplist.clear();
        for (ZoomLevel e : ZoomLevel.values()) {
            if ( zoom > e.getZoomValue() ) maplist.addAll(tree.searchTree(p0, p1, e));
        }
    }

    private static EnumMap<ZoomLevel, List<MapElement>> initializeMap() {
        EnumMap<ZoomLevel, List<MapElement>> map = new EnumMap<>(ZoomLevel.class);
        for (ZoomLevel type: ZoomLevel.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }

    public void add(ZoomLevel type, MapElement m) {
        mapelements.get(type).add(m);
    }

    /** Getters */
    public Addresses getAddresses() {
        return addresses;
    }

    public List<MapElement> getTreeData(){
        return maplist;
    }

    public List<MapElement> get(ZoomLevel type) {
        return mapelements.get(type);
    }

    public EnumMap<ZoomLevel, List<MapElement>> getMapElements() {
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

    /** Setters */
    public void setAddresses(Addresses addresses) {
        this.addresses = addresses;
    }

    public void setMapElements(EnumMap<ZoomLevel, List<MapElement>> mapElements) {
        mapelements = mapElements;
    }

    public void setMinLat(double minLat){this.minLat = minLat;}

    public void setMinLon(double minLon){this.minLon = minLon;}

    public void setMaxLat(double maxLat){this.maxLat = maxLat;}

    public void setMaxLon(double maxLon){this.maxLon = maxLon;}



}

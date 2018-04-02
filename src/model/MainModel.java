package model;

import controller.CanvasController;
import helpers.KDTree;
import model.osm.*;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class MainModel extends Observable implements Serializable{
    private static  EnumMap<OSMWayType, List<MapElement>> mapelements = initializeMap();
    private double minLat, minLon, maxLat, maxLon;
    private static KDTree tree;
    private static List<MapElement> maplist = new ArrayList<>();

    public MainModel(){}

    public MainModel(String filename) {
        load(filename);
        tree = new KDTree(this);
    }


    public static void updateMap(Point2D p0, Point2D p1){
        int zoom = CanvasController.getInstance().getZoomLevel();

        maplist.clear();
        for (OSMWayType e : OSMWayType.values()) {
            if ( zoom > e.getZoomValue() ) maplist.addAll(tree.searchTree(p0, p1, e));
        }
    }

    public List<MapElement> getTreeData(){
        return maplist;
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
        dirty();
    }

    public void dirty() {
        setChanged();
        notifyObservers();
    }

    public void readFromOSM(InputSource filename) {
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(new OSMHandler(this));
            xmlReader.parse(filename);

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String filename) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(mapelements);
            os.writeObject(minLon);
            os.writeObject(minLat);
            os.writeObject(maxLon);
            os.writeObject(maxLat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String filename) {
        if (filename.endsWith(".osm")) {
            readFromOSM(new InputSource(filename));
        } else if (filename.endsWith(".zip")) {
            try {
                ZipInputStream zis = new ZipInputStream(new FileInputStream(filename));
                zis.getNextEntry();
                readFromOSM(new InputSource(zis));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (filename.endsWith(".bin")) {
            try {
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
                mapelements = (EnumMap<OSMWayType, List<MapElement>>) is.readObject();
                minLon = (double) is.readObject();
                minLat = (double) is.readObject();
                maxLon = (double) is.readObject();
                maxLat = (double) is.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public List<MapElement> get(OSMWayType type) {
        return mapelements.get(type);
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

    public void setMinLat(double minLat){this.minLat = minLat;}

    public void setMinLon(double minLon){this.minLon = minLon;}

    public void setMaxLat(double maxLat){this.maxLat = maxLat;}

    public void setMaxLon(double maxLon){this.maxLon = maxLon;}


}

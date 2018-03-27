package model;

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
    private EnumMap<OSMWayType, List<Shape>> shapes = initializeMap();
    private double minLat, minLon, maxLat, maxLon;
    private OSMHandler handler;
    private static KDTree tree;
    private static List<MapElements> maplist;

    public MainModel(){}

    public MainModel(String filename) {
        load(filename);
        tree = new KDTree();
        tree.createTree(handler.getListOfElements(), this);
    }


    public static void updateMap(Point2D p0, Point2D p1){
        maplist = tree.searchTree(p0, p1);
    }

    public List<MapElements> getTreeData(){
        //System.out.println(maplist.size());
        return maplist;
    }


    private EnumMap<OSMWayType, List<Shape>> initializeMap() {
        EnumMap<OSMWayType, List<Shape>> map = new EnumMap<>(OSMWayType.class);
        for (OSMWayType type: OSMWayType.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }

    public void add(OSMWayType type, Shape shape) {
        shapes.get(type).add(shape);
        dirty();
    }

    public void dirty() {
        setChanged();
        notifyObservers();
    }

    public void readFromOSM(InputSource filename) {
        try {
            handler = new OSMHandler(this);
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(handler);
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
            os.writeObject(shapes);
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
                shapes = (EnumMap<OSMWayType, List<Shape>>) is.readObject();
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

    public Iterable<Shape> get(OSMWayType type) {
        return shapes.get(type);
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

package model;

import model.osm.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class MainModel extends Observable implements Serializable{

    private EnumMap<OSMWayType, List<Shape>> shapes = initializeMap();
    private double minLat, minLon, maxLat, maxLon;

    public MainModel(){}

    public MainModel(String filename) {
        load(filename);
    }

    private EnumMap<OSMWayType, List<Shape>> initializeMap() {
        EnumMap<OSMWayType, List<Shape>> map = new EnumMap<OSMWayType, List<Shape>>(OSMWayType.class);
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
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(new model.osm.OSMHandler());
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
        dirty();
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


}

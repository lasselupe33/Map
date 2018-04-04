package model;

import helpers.OSMHandler;
import model.osm.OSMWayType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.*;
import java.io.*;
import java.util.EnumMap;
import java.util.List;
import java.util.zip.ZipInputStream;

public class IOModel {
    private MainModel model;

    public IOModel(MainModel m) {
        model = m;
    }

    public IOModel(MainModel m, String filename) {
        model = m;
        load(filename);
    }

    public void readFromOSM(InputSource filename) {
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(new OSMHandler(model));
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
            os.writeObject(model.getMapElements());
            os.writeObject(model.getMinLon());
            os.writeObject(model.getMinLat());
            os.writeObject(model.getMaxLon());
            os.writeObject(model.getMaxLat());
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
                model.setMapElements((EnumMap<OSMWayType, List<MapElement>>) is.readObject());
                model.setMinLon((double) is.readObject());
                model.setMinLat((double) is.readObject());
                model.setMaxLon((double) is.readObject());
                model.setMaxLat((double) is.readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

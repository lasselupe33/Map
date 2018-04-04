package model;

import helpers.OSMHandler;
import model.osm.OSMWayType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.List;
import java.util.zip.ZipInputStream;

public class IOModel {
    private MainModel model;

    public IOModel(MainModel m) {
        model = m;
    }

    public IOModel(MainModel m, URL filepath) {
        model = m;

        try {
            load(filepath.openStream(), filepath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            File out = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + filename);
            System.out.println(out.getAbsolutePath());
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

    /** Helper that allows loading of a file only by passing the filename */
    public void load(String filename) {
        try {
            load(new FileInputStream(filename), filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Helper that loads OSM-data from a inputStream */
    public void load(InputStream is, String filename) {
        if (filename.endsWith(".osm")) {
            readFromOSM(new InputSource(filename));
        } else if (filename.endsWith(".zip")) {
            try {

                ZipInputStream zis = new ZipInputStream(is);
                zis.getNextEntry();
                readFromOSM(new InputSource(zis));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (filename.endsWith(".bin")) {
            try {
                ObjectInputStream ois = new ObjectInputStream(is);
                model.setMapElements((EnumMap<OSMWayType, List<MapElement>>) ois.readObject());
                model.setMinLon((double) ois.readObject());
                model.setMinLat((double) ois.readObject());
                model.setMaxLon((double) ois.readObject());
                model.setMaxLat((double) ois.readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

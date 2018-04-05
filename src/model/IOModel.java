package model;

import helpers.OSMHandler;
import model.osm.OSMWayType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
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
    public IOModel(MainModel m, String filename, Boolean loadBinary) {
        model = m;
        load(filename, loadBinary);
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

    public void save() {
        try {
            // Get the correct path in relation to where the files currently reside
            String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/output.bin", "UTF-8");

            // Write data
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            out.writeObject(model.getMapElements());
            out.writeObject(model.getMinLon());
            out.writeObject(model.getMinLat());
            out.writeObject(model.getMaxLon());
            out.writeObject(model.getMaxLat());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Helper that allows loading of a file only by passing the filename */
    public void load(String filename, Boolean loadBinary) {
        try {
            if (loadBinary) {
                // Get the correct path in relation to where the binary files currently reside
                filename = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/" + filename, "UTF-8");
            }

            load(new FileInputStream(filename), filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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
                model.reInitTree();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

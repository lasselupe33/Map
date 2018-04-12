package model;

import helpers.OSMHandler;
import helpers.SerializeObject;
import org.nustaq.serialization.FSTConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.zip.ZipInputStream;

public class IOModel {
    public static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
    private MainModel model;
    private MapModel mapModel;

    public IOModel(MainModel m, MapModel mm, String filename) {
        model = m;
        mapModel = mm;
        load(filename);
    }

    /** Constructor for loading a URL */
    public IOModel(MainModel m, MapModel mm, URL filepath) {
        model = m;
        mapModel = mm;
        load(filepath);
    }

    /** Constructor for loading binary data */
    public IOModel(MainModel m, MapModel mm) {
        model = m;
        mapModel = mm;
        loadBinary();
    }

    /** Internal helper that sets up the OSMHandler and begins reading from an OSM-file */
    public void readFromOSM(InputSource filename) {
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(new OSMHandler(model, mapModel));
            xmlReader.parse(filename);
            mapModel.createTree();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Helper that serializses all models */
    public void save() {
        model.serialize();
        mapModel.serialize();
    }

    /** Load data from a string */
    public void load(String filename) {
        try {
            load(new FileInputStream(filename), filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Load data from a url */
    public void load(URL filename) {
        try {
            load(filename.openStream(), filename.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Helper that loads OSM-data from a inputStream */
    public void load(InputStream is, String filename) {
        System.out.println(filename);

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
        }

        // Always save data after a new map has been loaded.
        save();
    }

    public void test(Object test) {
        System.out.println("Success" + test);
    }

    /** Helper that loads files from binary format */
    private void loadBinary() {
        // Deserialize models
        model.deserialize();
        mapModel.deserialize();
    }
}

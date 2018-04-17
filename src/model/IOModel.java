package model;

import controller.CanvasController;
import helpers.OSMHandler;
import helpers.SerializeObject;
import main.Main;
import model.graph.Graph;
import org.nustaq.serialization.FSTConfiguration;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import view.MainWindowView;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.zip.ZipInputStream;

public class IOModel {
    private static final int modelsToDeserialize = 2;
    private static int deserializedModels = 0;
    public static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
    private MainModel model;
    private MapModel mapModel;
    private Graph graph;
    private MainWindowView mainView;

    public IOModel(MainModel m, MapModel mm, Graph g, String filename) {
        model = m;
        mapModel = mm;
        graph = g;
        load(filename);
    }

    /** Constructor for loading a URL */
    public IOModel(MainModel m, MapModel mm, Graph g, URL filepath) {
        model = m;
        mapModel = mm;
        graph = g;
        load(filepath);
    }

    /** Constructor for loading binary data */
    public IOModel(MainModel m, MapModel mm, Graph g) {
        model = m;
        mapModel = mm;
        graph = g;
        loadBinary();
    }

    public void addView(MainWindowView mv) {
        mainView = mv;
    }

    /** Internal helper that sets up the OSMHandler and begins reading from an OSM-file */
    public void readFromOSM(InputSource filename) {
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(new OSMHandler(model, mapModel, graph));
            xmlReader.parse(filename);
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

        // Prepare models to recieve new data
        mapModel.reset();

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

        if (!Main.initialRender) {
            // If a new map has been loaded, then refresh the canvas.
            CanvasController.getInstance().reset();
        } else {
            // Always save data after when a new map has been loaded
            save();
        }
    }

    public static void serializationComplete() {
        deserializedModels++;

        // Everything has been deserialized! Boot application
        if (deserializedModels == modelsToDeserialize) {
            // Indicate that data is ready
            Main.dataLoaded = true;

            // If MVC has been initialized, run application!
            if (Main.hasInitialized) {
                Main.run();
            }
        }
    }

    /** Helper that loads files from binary format */
    private void loadBinary() {
        // Deserialize models
        model.deserialize();
        mapModel.deserialize();
    }
}

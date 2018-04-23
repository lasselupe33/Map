package model;

import controller.CanvasController;
import helpers.OSMHandler;
import main.Main;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import view.FooterView;
import view.LoadingScreen;

import java.io.*;
import java.net.URL;
import java.util.zip.ZipInputStream;

public class IOModel {
    public static IOModel instance = new IOModel(); // Create an instance of the IOModel
    private int deserializedObjects = 0;
    private int serializedObjects = 0;
    private int objectsToSerialize = 0;
    private int objectsToDeserialize = 0;
    private boolean initialLoad = true;

    // Models and views
    private MetaModel model;
    private MapModel mapModel;
    private AddressesModel addressesModel;
    private FooterView footerView;
    private LoadingScreen loadingScreen;

    private IOModel() {}

    public void addModels(MetaModel m, MapModel mm, AddressesModel am) {
        model = m;
        mapModel = mm;
        addressesModel = am;
    }

    public void addView(FooterView fv) {
        footerView = fv;
    }


    public void loadFromURL(URL filepath) {
        if (initialLoad) {
            loadingScreen = new LoadingScreen();
            initialLoad = false;
        }

        load(filepath);
    }

    public void loadFromString(String filename) {
        if (initialLoad) {
            loadingScreen = new LoadingScreen();
            initialLoad = false;
        }

        load(filename);
    }

    public void loadFromBinary() {
        if (initialLoad) {
            loadingScreen = new LoadingScreen();
            initialLoad = false;
        }

        loadBinary();
    }

    /** Helper that serializses all models */
    public void save() {
        // Reset serialized objects before starting
        serializedObjects = 0;
        objectsToSerialize = 0;

        model.serialize();
        mapModel.serialize();
        addressesModel.serialize();
    }

    /** Load data from a string */
    private void load(String filename) {
        try {
            load(new FileInputStream(filename), filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Load data from a url */
    private void load(URL filename) {
        try {
            load(filename.openStream(), filename.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Helper that loads OSM-data from a inputStream */
    private void load(InputStream is, String filename) {
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
            // Always save data after launch
            save();
        }

        loadingScreen.onLoaded();
    }

    /** Helper that loads files from binary format */
    private void loadBinary() {
        objectsToDeserialize = 0;
        serializedObjects = 0;

        // Deserialize models
        model.deserialize();
        mapModel.deserialize();
        addressesModel.deserialize();
    }

    /** Internal helper that sets up the OSMHandler and begins reading from an OSM-file */
    private void readFromOSM(InputSource filename) {
        try {
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(new OSMHandler(model, mapModel, addressesModel, loadingScreen));
            xmlReader.parse(filename);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onObjectDeserializationComplete() {
        deserializedObjects++;
        loadingScreen.updateProgress(((double) deserializedObjects / objectsToDeserialize) * 100);

        // Everything has been deserialized! Boot application
        if (deserializedObjects == objectsToDeserialize) {
            // Indicate that data is ready
            Main.dataLoaded = true;
            loadingScreen.onLoaded();

            // If MVC has been initialized, run application!
            if (Main.hasInitialized) {
                Main.run();
            }
        }
    }

    /** Function to be called once an object has serialized */
    public void onObjectSerializationComplete() {
        serializedObjects++;

        if (footerView != null) {
            footerView.updateSaveStatus(((double) serializedObjects / objectsToSerialize) * 100);
        }
    }

    public void onDeserializeStart() {
        objectsToDeserialize++;
    }

    /** Function to be called when a new object begins to deserialize */
    public void onSerializationStart() {
        objectsToSerialize++;
    }
}

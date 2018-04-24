package helpers.io;

import controller.MapController;
import model.AddressesModel;
import model.MapModel;
import model.MetaModel;
import parsing.OSMHandler;
import main.Main;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import view.FooterView;
import view.LoadingScreen;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.zip.ZipInputStream;

public class IOHandler {
    public static IOHandler instance = new IOHandler(); // Create an instance of the IOHandler
    public static URL internalRootPath;
    public static URI externalRootPath;
    public static boolean useExternalSource;
    public boolean isJar = false;

    private int deserializedObjects = 0;
    private int serializedObjects = 0;
    private int objectsToSerialize = 0;
    private int objectsToDeserialize = 0;

    // Models and views
    private MetaModel model;
    private MapModel mapModel;
    private AddressesModel addressesModel;
    private FooterView footerView;
    private LoadingScreen loadingScreen;

    /** Initialize IOHandler with reference to the rootPath */
    private IOHandler() {
        try {
            internalRootPath = Main.class.getResource("");
            externalRootPath = IOHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI();

            // If program is being run from .jar, then trim the filename of the .jar out of the url.
            if (externalRootPath.toString().endsWith(".jar")) {
                isJar = true;
                String[] temp = externalRootPath.toString().split("/");
                externalRootPath = new URI(String.join("/", Arrays.copyOf(temp, temp.length - 1)));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void addModels(MetaModel m, MapModel mm, AddressesModel am) {
        model = m;
        mapModel = mm;
        addressesModel = am;
    }

    public void addView(FooterView fv) {
        footerView = fv;
    }

    public void loadFromString(String filename) {
        loadingScreen = new LoadingScreen();

        load(filename);
    }

    public void loadFromBinary(boolean shouldUseExternalSource) {
        useExternalSource = shouldUseExternalSource;
        loadingScreen = new LoadingScreen();

        loadBinary();
    }

    /** Helper that serializses all models */
    public void save() {
        // Reset serialized objects before starting
        serializedObjects = 0;
        objectsToSerialize = 0;

        this.cleanDirs();
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

    /** Helper that loads OSM-data from a inputStream */
    private void load(InputStream is, String filename) {
        System.out.println(filename);

        Thread loaderThread = new Thread(() -> {
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

            if (Main.initialRender) {
                // If MVC is ready, then run application!
                if (Main.hasInitialized) {
                    Main.run();
                }
            } else {
                // If a new map has been loaded, then refresh the canvas.
                MapController.getInstance().reset();
            }

            // Indicate that the application data has finished loading
            loadingScreen.onLoaded();
            loadingScreen = null;
        });

        loaderThread.start();
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

    /**
     * Callback to be called once an object has been successfully deserialized
     */
    public void onObjectDeserializationComplete() {
        deserializedObjects++;

        // Update loadingScreen progress.
        loadingScreen.updateProgress(((double) deserializedObjects / objectsToDeserialize) * 100);

        if (deserializedObjects == objectsToDeserialize) {
            // Everything has been deserialized! Boot application
            loadingScreen.onLoaded();
            loadingScreen = null;

            // If MVC has been initialized, run application!
            if (Main.hasInitialized) {
                Main.run();
            }

            // Indicate that data is ready
            Main.dataLoaded = true;
        }
    }

    /** Function to be called once an object has serialized */
    public void onObjectSerializationComplete() {
        serializedObjects++;
        footerView.updateProgressbar("Gemmer...", ((double) serializedObjects / objectsToSerialize) * 100);
    }

    public void onDeserializeStart() {
        objectsToDeserialize++;
    }

    /** Function to be called when a new object begins to deserialize */
    public void onSerializationStart() {
        objectsToSerialize++;
    }

    private void cleanDirs() {
        try {
            // Attempt to delete the while data-folder recursively if exists
            if (Files.exists(Paths.get(new URI(externalRootPath + "/BFST18_binary")))) {
                Files.walkFileTree(Paths.get(new URI(externalRootPath + "/BFST18_binary")), new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                        // Delete file when found
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exception) throws IOException {
                        // Delete folder after content has been cleared.
                        new File(dir.toUri()).delete();

                        return FileVisitResult.CONTINUE;
                    }
                });
            }

            // Recreate folders in preparation for data storage
            Files.createDirectory(Paths.get(new URI(externalRootPath + "/BFST18_binary")));
            Files.createDirectory(Paths.get(new URI(externalRootPath + "/BFST18_binary/address")));
            Files.createDirectory(Paths.get(new URI(externalRootPath + "/BFST18_binary/map")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

package model;

import helpers.OSMHandler;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import model.MapElements.MapElement;
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
            model.createTree();
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
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(path));
            for (ZoomLevel level : ZoomLevel.values()) {
                out.writeObject(model.getMapElements().get(level));
                // Ensure to flush with each zoomLevel to avoid heap overflow
                out.flush();
            }

            // Write data to model
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
        } else if (filename.endsWith(".bin")) {
            try {
                FSTObjectInput ois = new FSTObjectInput(is);

                // Firstly, create the mapelements map
                EnumMap<ZoomLevel, List<MapElement>> tempMap = MainModel.initializeMap();
                for (ZoomLevel level : ZoomLevel.values()) {
                    tempMap.put(level, (List<MapElement>) ois.readObject());
                }

                // Add data to model
                model.setMapElements(tempMap);
                model.setMinLon((double) ois.readObject());
                model.setMinLat((double) ois.readObject());
                model.setMaxLon((double) ois.readObject());
                model.setMaxLat((double) ois.readObject());

                // Create tree with loaded data
                model.createTree();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

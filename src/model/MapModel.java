package model;

import helpers.DeserializeObject;
import helpers.KDTree;
import helpers.SerializeObject;
import helpers.ZoomLevelMap;

import main.Main;
import model.MapElement;
import model.osm.OSMWayType;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import view.MainWindowView;

import java.awt.geom.Point2D;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MapModel {
    private int initializedTypes = 0;
    private int amountOfTypes = 0;
    private EnumMap<OSMWayType, List<Coordinates>> mapElements = initializeMap();
    private KDTree[] trees;
    private List<MapElement> maplist = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();
    private MainModel mainModel;
    private MainWindowView mainView;

    public MapModel(MainModel m) {
        mainModel = m;
    }

    public void addMainView(MainWindowView mv) {
        mainView = mv;
    }

    public void reset() {
        mapElements = initializeMap();
        maplist = new ArrayList<>();
        if (trees != null) {
            for (int i = 0; i < trees.length; i++) trees[i] = null;
        }
    }

    /** Public helper that initializses an empty enum-map filled with arraylist for all mapTypes */
    public static EnumMap<OSMWayType, List<Coordinates>> initializeMap() {
        EnumMap<OSMWayType, List<Coordinates>> map = new EnumMap<>(OSMWayType.class);
        for (OSMWayType type: OSMWayType.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }

    /** Add a Coordinates to the list. This will happen while parsing OSM-files */
    public void add(OSMWayType type, Coordinates m) {
        mapElements.get(type).add(m);
    }

    public void addAddress(Address address) { addresses.add(address); }

    /** Internal helper that retrieves the currently required points to render the map */
    public void updateMap(Point2D p0, Point2D p1){
        int zoomLevel = ZoomLevelMap.getZoomLevel();

        int i = 0;
        List<MapElement> tmplist = new ArrayList<>();
        for (OSMWayType type : OSMWayType.values()) {
            if (type.getPriority() <= zoomLevel) {
                tmplist.addAll(trees[i].searchTree(p0, p1));
            }
            i++;
        }
        //trees[i].searchTree(p0, p1);
        maplist = tmplist;
    }


    /** Callback to be called once a thread has finished deserializing a mapType */
    public void onThreadDeserializeComplete(ArrayList loadedList, String type) {
        initializedTypes++;
        mapElements.put(OSMWayType.valueOf(type.split("/")[1]), loadedList);

        System.out.println("Loaded " + initializedTypes + " of " + amountOfTypes + " mapTypes.");

        if (initializedTypes == amountOfTypes) {
            System.out.println("Building tree..");
            // Always rebuild tree, since loading the binary tree takes longer in total
            createTree();

            // Remove mapElements once tree has been build to preserve space
            mapElements = null;

            // Indicate that serialization has been completed
            IOModel.serializationComplete();
        }
    }

    public String nearestNeighbour(double px, double py) {
        return trees[trees.length-1].nearestNeighbour(px, py);
    }

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        try {
            URL path = Main.class.getResource("/data/info.bin");
            File file = new File(path.toURI());
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(file));

            for (OSMWayType type : OSMWayType.values()) {
                List<Coordinates> currList = get(type);
                ArrayList<String> listNames = new ArrayList<>();

                if (currList.size() > 200000) {
                    int currentlyProcessed = 0;

                    while (currentlyProcessed < currList.size()) {
                        List<Coordinates> tempList = new ArrayList<>(currList.subList(currentlyProcessed, Math.min(currentlyProcessed + 200000, currList.size() - 1)));
                        currentlyProcessed += 200000;

                        String name = "map/" + type.toString() + "-" + currentlyProcessed;
                        listNames.add(name);
                        new SerializeObject(new String[] { name }, tempList);
                    }

                    out.writeObject(listNames.toArray(new String[listNames.size()]), String[].class);
                } else {
                    out.writeObject(new String[] { "map/" + type.toString() }, String[].class);
                    new SerializeObject(new String[] { "map/" + type.toString()}, get(type));
                }
            }

            out.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Now that the map has been saved, we are free to remove the mapElements list in order to preserve space
        mapElements = null;
    }

    /** Internal helper that deserializses the MapModel */
    public void deserialize() {
        try {
            // Setup thread callback
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = ArrayList.class;
            parameterTypes[1] = String.class;
            Method callback = MapModel.class.getMethod("onThreadDeserializeComplete", parameterTypes);

            URL path = Main.class.getResource("/data/info.bin");
            InputStream stream = path.openStream();
            FSTObjectInput in = new FSTObjectInput(stream);

            while (true) {
                String[] filenames = (String[]) in.readObject(String[].class);
                new DeserializeObject(filenames, ArrayList.class, this, callback);
                amountOfTypes++;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Threads started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Coordinates> get(OSMWayType type) {
        return mapElements.get(type);
    }

    /** Returns the mapData required to render the screen */
    public List<MapElement> getMapData(){ return maplist; }

    /** Helper that creates a new KDTree based on the mapElements currently available to the MapModel */
    public void createTree() {


        trees = new KDTree[OSMWayType.values().length + 1];

        int i = 0;
        for (OSMWayType type : OSMWayType.values()) {
            trees[i++] = new KDTree(mapElements.get(type), mainModel.getMaxLat(), mainModel.getMinLat(), mainModel.getMaxLon(), mainModel.getMinLon());
        }
        trees[i] = new KDTree(addresses);

    }
}

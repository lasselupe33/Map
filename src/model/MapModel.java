package model;

import helpers.DeserializeObject;
import helpers.KDTree;
import helpers.SerializeObject;
import helpers.ZoomLevelMap;
import main.Main;
import model.MapElements.MapElement;
import model.osm.OSMWayType;
import view.MainWindowView;

import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MapModel {
    private EnumMap<OSMWayType, List<MapElement>> mapElements = initializeMap();
    private int initializedTypes = 0;
    private int amountOfTypes = 0;
    private KDTree tree;
    private List<MapElement> maplist = new ArrayList<>();
    private MainModel mainModel;
    private MainWindowView mainView;

    public MapModel(MainModel m) {
        mainModel = m;
    }

    public void addMainView(MainWindowView mv) {
        mainView = mv;
    }

    /** Public helper that initializses an empty enum-map filled with arraylist for all mapTypes */
    public static EnumMap<OSMWayType, List<MapElement>> initializeMap() {
        EnumMap<OSMWayType, List<MapElement>> map = new EnumMap<>(OSMWayType.class);
        for (OSMWayType type: OSMWayType.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }

    /** Add a mapElement to the list. This will happen while parsing OSM-files */
    public void add(OSMWayType type, MapElement m) {
        mapElements.get(type).add(m);
    }

    /** Internal helper that retrieves the currently required points to render the map */
    public void updateMap(Point2D p0, Point2D p1){
        int zoomLevel = ZoomLevelMap.getZoomLevel();

        int i = 0;
        maplist.clear();
        for (OSMWayType type : OSMWayType.values()) {
            if (type.getPriority() <= zoomLevel) {
                maplist.addAll(tree.searchTree(p0, p1, i));
            }

            i++;
        }
    }

    public void onThreadDeserializeComplete(ArrayList loadedList, String type) {
        initializedTypes++;
        mapElements.put(OSMWayType.valueOf(type), loadedList);

        System.out.println(initializedTypes + " " + amountOfTypes);
        if (initializedTypes == amountOfTypes) {
            // Always rebuild tree, since loading the binary tree takes longer in total
            createTree();

            Main.run();

            // Remove mapElements once tree has been build to preserve space
            mapElements = null;
        }
    }

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        for (OSMWayType type : OSMWayType.values()) {
            new SerializeObject(type.toString(), get(type));
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

                for (OSMWayType type : OSMWayType.values()) {
                    amountOfTypes++;
                    new DeserializeObject(type.toString(), ArrayList.class, this, callback);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

    }

    public List<MapElement> get(OSMWayType type) {
        return mapElements.get(type);
    }

    /** Returns the mapData required to render the screen */
    public List<MapElement> getMapData(){
        return maplist;
    }

    /** Helper that creates a new KDTree based on the mapElements currently available to the MapModel */
    public void createTree() { tree = new KDTree(mapElements, mainModel.getMaxLat(), mainModel.getMinLat(), mainModel.getMaxLon(), mainModel.getMinLon()); }
}

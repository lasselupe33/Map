package model;

import helpers.DeserializeObject;
import helpers.KDTree;
import helpers.SerializeObject;
import helpers.ZoomLevelMap;

import model.osm.OSMWayType;
import view.MainWindowView;

import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MapModel {
    private int initializedTypes = 0;
    private int amountOfTypes = 0;
    private EnumMap<OSMWayType, List<Coordinates>> mapElements = initializeMap();
    private KDTree[] mapTrees; // Contain a reference to trees containing all elements
    private List<MapElement> maplist = new ArrayList<>();
    private MetaModel metaModel;
    private MainWindowView mainView;

    public MapModel(MetaModel m) {
        metaModel = m;
    }

    public void reset() {
        mapElements = initializeMap();
        maplist = new ArrayList<>();
        if (mapTrees != null) {
            for (int i = 0; i < mapTrees.length; i++) mapTrees[i] = null;
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

    /** Internal helper that retrieves the currently required points to render the map */
    public void updateMap(Point2D p0, Point2D p1){
        int zoomLevel = ZoomLevelMap.getZoomLevel();

        int i = 0;
        List<MapElement> tmplist = new ArrayList<>();
        for (OSMWayType type : OSMWayType.values()) {
            if (type.getPriority() <= zoomLevel) {
                tmplist.addAll(mapTrees[i].searchTree(p0, p1));
            }
            i++;
        }
        //trees[i].searchTree(p0, p1);
        maplist = tmplist;
    }


    /** Callback to be called once a thread has finished deserializing a mapType */
    public void onThreadDeserializeComplete(KDTree loadedTree, String name) {
        initializedTypes++;

        String index = name.split("-")[1];
        mapTrees[Integer.parseInt(index)] = loadedTree;
    }

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        for (int i = 0; i < mapTrees.length; i++) {
            new SerializeObject("map/tree-" + i, mapTrees[i]);
        }

        // Now that the map has been saved, we are free to remove the mapElements list in order to preserve space
        mapElements = null;
    }

    /** Internal helper that deserializses the MapModel */
    public void deserialize() {
        try {
            mapTrees = new KDTree[OSMWayType.values().length];
            amountOfTypes = mapTrees.length;

            // Setup thread callback
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = KDTree.class;
            parameterTypes[1] = String.class;
            Method callback = MapModel.class.getMethod("onThreadDeserializeComplete", parameterTypes);

            for (int i = 0; i < mapTrees.length; i++) {
                new DeserializeObject("map/tree-" + i, this, callback);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Coordinates> get(OSMWayType type) {
        return mapElements.get(type);
    }

    /** Returns the mapData required to render the screen */
    public List<MapElement> getMapData(){ return maplist; }

    /** Helper that creates new KDTrees based on the mapElements currently available to the MapModel */
    public void createTrees() {
        mapTrees = new KDTree[OSMWayType.values().length];

        // Loop over all map types and generate trees for all.
        int i = 0;
        for (OSMWayType type : OSMWayType.values()) {
            mapTrees[i++] = new KDTree(mapElements.get(type), metaModel.getMaxLat(), metaModel.getMinLat(), metaModel.getMaxLon(), metaModel.getMinLon());
        }
    }
}

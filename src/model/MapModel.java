package model;

import helpers.io.DeserializeObject;
import helpers.structures.KDTree;
import helpers.io.SerializeObject;
import model.graph.Graph;
import model.graph.Node;
import model.graph.VehicleType;

import java.awt.geom.Path2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MapModel {
    private int initializedTypes = 0;
    private int amountOfTypes = 0;
    private EnumMap<WayType, List<Coordinates>> mapElements = initializeMap();
    private KDTree[] mapTrees; // Contain a reference to trees containing all elements
    private List<MapElement> currentMapData = new ArrayList<>();
    private MetaModel metaModel;
    private Graph graph;

    public MapModel(MetaModel m, Graph g) {
        graph = g;
        metaModel = m;
    }

    /** Internal helper that resets the MapModel, clearing all data */
    public void reset() {
        mapElements = initializeMap();
        currentMapData = new ArrayList<>();

        if (mapTrees != null) {
            for (int i = 0; i < mapTrees.length; i++) mapTrees[i] = null;
        }
    }

    /** Add a Coordinates to the list. This will happen while parsing OSM-files */
    public void add(WayType type, Coordinates m) {
        mapElements.get(type).add(m);
    }

    /** Returns the mapElements of a specific type */
    public List<Coordinates> getMapElements(WayType type) {
        return mapElements.get(type);
    }

    public void setMapData(List<MapElement> newData) {
        currentMapData = newData;
    }

    /** Returns the mapData required to render the screen */
    public List<MapElement> getMapData(){ return currentMapData; }

    public KDTree getTree(int index) {
        return mapTrees[index];
    }

    /** Helper that creates new KDTrees based on the mapElements currently available to the MapModel */
    public void createTrees() {
        mapTrees = new KDTree[WayType.values().length];

        // Loop over all map types and generate trees for all.
        int i = 0;
        for (WayType type : WayType.values()) {
            mapTrees[i++] = new KDTree(mapElements.get(type), metaModel.getMaxLat(), metaModel.getMinLat(), metaModel.getMaxLon(), metaModel.getMinLon());
        }
    }

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        for (int i = 0; i < mapTrees.length; i++) {
            new SerializeObject("map/tree-" + i, mapTrees[i]);
        }

        // Now that the map has been saved, we are free to remove the mapElements list in order to preserve space
        mapElements = null;
    }

    public long getNearestNodeId(Coordinates coords) {
        List<MapElement> candidates = new ArrayList<>();

        switch(graph.getType()) {
            case CAR:
                addCandidatesCar(coords, candidates);
                break;
            case BICYCLE:
                addCandidatesBicycle(coords, candidates);
                break;
            case PEDESTRIAN:
                addCandidatesPedestrian(coords, candidates);
                break;
            default:
                break;
        }

        long nearestNeighbour = 0;

        double currentNeighbour = Double.MAX_VALUE;
        for (MapElement way : candidates) {
            if (way != null) {
                for (long nodeId : way.getNodeIds()) {
                    Node node = graph.getNode(nodeId);
                    double distanceTo = Math.hypot( coords.getX() - node.getLon(), coords.getY() - node.getLat());
                    if ( distanceTo < currentNeighbour ) {
                        nearestNeighbour = nodeId;
                        currentNeighbour = distanceTo;
                    }
                }
            }
        }

        return nearestNeighbour;
    }

    private void addCandidatesCar(Coordinates coords, List<MapElement> candidates) {
        int i = 0;
        for (WayType type : WayType.values()) {
            switch(type) {
                case SERVICE:
                case MOTORWAY:
                case TRUNK:
                case ROAD:
                case TERTIARYROAD:
                case SECONDARYROAD:
                case HIGHWAY:
                    candidates.add((MapElement) mapTrees[i].nearestNeighbour(coords.getX(), coords.getY()));
                    break;
                default:
                    break;
            }
            i++;
        }
    }

    private void addCandidatesBicycle(Coordinates coords, List<MapElement> candidates) {
        int i = 0;
        for (WayType type : WayType.values()) {
            switch(type) {
                case SERVICE:
                case CYCLEWAY:
                case ROAD:
                case PEDESTRIAN:
                case PATH:
                case TERTIARYROAD:
                case SECONDARYROAD:
                case HIGHWAY:
                    candidates.add((MapElement) mapTrees[i].nearestNeighbour(coords.getX(), coords.getY()));
                    break;
                default:
                    break;
            }
            i++;
        }
    }

    private void addCandidatesPedestrian(Coordinates coords, List<MapElement> candidates) {
        int i = 0;
        for (WayType type : WayType.values()) {
            switch(type) {
                case SERVICE:
                case ROAD:
                case PEDESTRIAN:
                case FOOTWAY:
                case PATH:
                case TERTIARYROAD:
                case SECONDARYROAD:
                case HIGHWAY:
                    candidates.add((MapElement) mapTrees[i].nearestNeighbour(coords.getX(), coords.getY()));
                    break;
                default:
                    break;
            }
            i++;
        }
    }

    /** Internal helper that deserializses the MapModel */
    public void deserialize() {
        try {
            mapTrees = new KDTree[WayType.values().length];
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

    /** Callback to be called once a thread has finished deserializing a mapType */
    public void onThreadDeserializeComplete(KDTree loadedTree, String name) {
        initializedTypes++;

        String index = name.split("-")[1];
        mapTrees[Integer.parseInt(index)] = loadedTree;
    }

    /** Public helper that initializses an empty enum-map filled with arraylist for all mapTypes */
    private EnumMap<WayType, List<Coordinates>> initializeMap() {
        EnumMap<WayType, List<Coordinates>> map = new EnumMap<>(WayType.class);
        for (WayType type: WayType.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }
}

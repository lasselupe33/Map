package model;

import controller.MapController;
import helpers.io.DeserializeObject;
import helpers.structures.KDTree;
import helpers.io.SerializeObject;
import model.graph.Graph;
import model.graph.Node;
import model.graph.VehicleType;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * The MapModel keeps track of all data required to render the canvas that draws the map, along with queries to keep
 * the data up to speed.
 */
public class MapModel {
    private EnumMap<WayType, List<MapElement>> mapElements = initializeMap();
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

    /** Internal helper that updates the list of map elements required to render the current viewport. */
     public void updateCurrentMapElement(Point2D p0, Point2D p1) {
         int i = 0;
         List<MapElement> tmpList = new ArrayList<>();

         for (WayType type : WayType.values()) {
             if (type.getPriority() <= MapController.getInstance().getZoomLevel()) {
                 tmpList.addAll(getTree(i).searchTree(p0, p1));
             }
             i++;
         }

         this.currentMapData = tmpList;
     }

    /** Add a Coordinates to the list. This will happen while parsing OSM-files */
    public void add(WayType type, MapElement m) {
        mapElements.get(type).add(m);
    }

    /** Returns the mapElements of a specific type */
    public List<MapElement> getMapElements(WayType type) {
        return mapElements.get(type);
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

    /** Internal helper that returns the id of the nearest way-node from a given coord */
    public long getNearestNodeId(Coordinates coords) {
        // Loop through relevant KD-tree for current vehicleType and get nearest way node ids
        List<MapElement> candidates = new ArrayList<>();
        addCandidatesForNearestNode(candidates, coords, graph.getVehicleType());

        long nearestNeighbour = 0;

        double currentNeighbour = Double.MAX_VALUE;
        for (MapElement way : candidates) {
            if (way != null) {
                for (long nodeId : way.getNodeIds()) {
                    Node node = graph.getNode(nodeId);


                    if (node != null) {
                        double distanceTo = Math.hypot( coords.getX() - node.getLon(), coords.getY() - node.getLat());
                        if ( distanceTo < currentNeighbour ) {
                            nearestNeighbour = nodeId;
                            currentNeighbour = distanceTo;
                        }
                    }
                }
            }
        }

        return nearestNeighbour;
    }

    /** Internal helper that adds all the nearest way node ids from relevant KD trees based on passed vehicleType */
    private void addCandidatesForNearestNode(List<MapElement> candidates, Coordinates coords, VehicleType vehicleType) {
        int i = 0;
        for (WayType wayType : WayType.values()) {
            switch (wayType) {
                case SERVICE:
                case TRUNK:
                case ROAD:
                case TERTIARYROAD:
                case SECONDARYROAD:
                case HIGHWAY:
                    candidates.add((MapElement) mapTrees[i].nearestNeighbour(coords.getX(), coords.getY()));
                    break;

                case MOTORWAY:
                    // Only add motorway for cars
                    if (vehicleType == VehicleType.CAR) {
                        candidates.add((MapElement) mapTrees[i].nearestNeighbour(coords.getX(), coords.getY()));
                    }
                    break;

                case CYCLEWAY:
                    // Add bicycle only paths
                    if (vehicleType == VehicleType.BICYCLE) {
                        candidates.add((MapElement) mapTrees[i].nearestNeighbour(coords.getX(), coords.getY()));
                    }
                    break;

                case FOOTWAY:
                    // Add pedestrian only paths
                    if (vehicleType == VehicleType.PEDESTRIAN) {
                        candidates.add((MapElement) mapTrees[i].nearestNeighbour(coords.getX(), coords.getY()));
                    }
                    break;

                case PEDESTRIAN:
                case PATH:
                    // Add paths only pedestrians and bicycles can use
                    if (vehicleType != VehicleType.CAR) {
                        candidates.add((MapElement) mapTrees[i].nearestNeighbour(coords.getX(), coords.getY()));
                    }
                    break;
            }
            i++;
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

    /** Internal helper that deserializses the MapModel */
    public void deserialize() {
        try {
            mapTrees = new KDTree[WayType.values().length];

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
        String index = name.split("-")[1];
        mapTrees[Integer.parseInt(index)] = loadedTree;
    }

    /** Public helper that initializses an empty enum-map filled with arraylist for all mapTypes */
    private EnumMap<WayType, List<MapElement>> initializeMap() {
        EnumMap<WayType, List<MapElement>> map = new EnumMap<>(WayType.class);
        for (WayType type: WayType.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }
}

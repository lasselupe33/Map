package model;

import controller.CanvasController;
import helpers.KDTree;
import helpers.ZoomLevelMap;
import model.MapElements.MapElement;
import model.osm.OSMWayType;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class MapModel {
    private EnumMap<OSMWayType, List<MapElement>> mapElements = initializeMap();
    private KDTree tree;
    private List<MapElement> maplist = new ArrayList<>();
    private MainModel mainModel;

    public MapModel(MainModel m) {
        mainModel = m;
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

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        try {
            String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/map.bin", "UTF-8");
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(path));

            for (OSMWayType type : OSMWayType.values()) {
                out.writeObject(get(type));
                out.flush();
            }

            out.close();

            // Now that the map has been saved, we are free to remove the mapElements list in order to preserve space
            mapElements = null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Internal helper that deserializses the MapModel */
    public void deserialize() {
        try {
            String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/map.bin", "UTF-8");
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(path));

            for (OSMWayType type : OSMWayType.values()) {
                mapElements.put(type, (List<MapElement>) in.readObject());
            }

            in.close();

            // Always rebuild tree, since loading the binary tree takes longer in total
            createTree();

            // Remove mapElements once tree has been build to preserve space
            mapElements = null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

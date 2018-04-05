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

    public void createTree() { tree = new KDTree(mapElements, mainModel.getMaxLat(), mainModel.getMinLat(), mainModel.getMaxLon(), mainModel.getMinLon()); }

    public static EnumMap<OSMWayType, List<MapElement>> initializeMap() {
        EnumMap<OSMWayType, List<MapElement>> map = new EnumMap<>(OSMWayType.class);
        for (OSMWayType type: OSMWayType.values()) {
            map.put(type, new ArrayList<>());
        }
        return map;
    }

    public void add(OSMWayType type, MapElement m) {
        mapElements.get(type).add(m);
    }

    public void updateMap(Point2D p0, Point2D p1){
        Rectangle2D viewRect = CanvasController.getInstance().getModelViewRect();
        int zoomLevel = ZoomLevelMap.getZoomLevel();
        int i = 0;
        maplist.clear();
        for (OSMWayType type : OSMWayType.values()) {
            if (type.getPriority() <= zoomLevel) {
                maplist.addAll(tree.searchTree(p0, p1, i));
            }

            i++;
            /*
            List<MapElement> newElements = tree.searchTree(p0, p1, i);


            if (maplist.size() + newElements.size() < 2000) {
                for (MapElement elm : newElements) {
                    if (elm.getBounds().intersects(viewRect)) {
                        maplist.add(elm);
                    }
                }
            }
            */
        }
    }

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        try {
            for (OSMWayType type : OSMWayType.values()) {
                String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/" + type + ".bin", "UTF-8");
                FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(path));

                out.writeObject(get(type));
                out.close();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deserialize() {
        try {
            for (OSMWayType type : OSMWayType.values()) {
                String path = URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/" + type + ".bin", "UTF-8");
                FSTObjectInput in = new FSTObjectInput(new FileInputStream(path));

                mapElements.put(type, (List<MapElement>) in.readObject());
                in.close();
            }

            createTree();

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

    public EnumMap<OSMWayType, List<MapElement>> getMapElements() {
        return mapElements;
    }

    public List<MapElement> getMapData(){
        return maplist;
    }
}

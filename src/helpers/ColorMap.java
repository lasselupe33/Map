package helpers;

import controller.MenuController;
import model.osm.OSMWay;
import model.osm.OSMWayType;

import java.awt.*;
import java.util.EnumMap;

public class ColorMap {
    private static EnumMap<OSMWayType, Color> standardMode = null;
    private static EnumMap<OSMWayType, Color> colorBlindMode = null;

    public static Color getColor(OSMWayType type) {
        if (standardMode == null) initializeStandard();
        if (colorBlindMode == null) initializeColorBlind();
        switch (MenuController.getMode()) {
            case STANDARD:
                return standardMode.get(type);
            case COLORBLIND:
                return colorBlindMode.get(type);
            default:
                break;
        }
        return standardMode.get(type);
    }

    private static void initializeStandard() {
        standardMode = new EnumMap<>(OSMWayType.class);
        standardMode.put(OSMWayType.COASTLINE, new Color(237, 237, 237));
        standardMode.put(OSMWayType.PLACE, new Color(242, 239, 233));
        standardMode.put(OSMWayType.RESIDENTIAL, new Color(234, 224, 216));
        standardMode.put(OSMWayType.FORREST, new Color(173, 216, 176));
        standardMode.put(OSMWayType.FARMLAND, new Color(251, 236, 215));
        standardMode.put(OSMWayType.WATER, new Color(60, 149, 255));
        standardMode.put(OSMWayType.UNKNOWN, Color.black);
        standardMode.put(OSMWayType.BUILDING, new Color(223, 213, 206));
        standardMode.put(OSMWayType.PITCH, new Color(170, 224, 203));
        standardMode.put(OSMWayType.ALLOMENTS, new Color(238, 207, 179));
        standardMode.put(OSMWayType.SERVICE, new Color(255, 255, 255));
        standardMode.put(OSMWayType.ROAD, new Color(255, 255, 255));
        standardMode.put(OSMWayType.PEDESTRIAN, new Color(255, 255, 255));
        standardMode.put(OSMWayType.PARK, new Color(200, 250, 204));
        standardMode.put(OSMWayType.PLAYGROUND, new Color(225, 255, 233));
        standardMode.put(OSMWayType.CEMETERY, new Color(193, 219, 200));
        standardMode.put(OSMWayType.FOOTWAY, new Color(250, 128, 114));
        standardMode.put(OSMWayType.PATH, new Color(250, 128, 114));
        standardMode.put(OSMWayType.FERRY, new Color(125, 139, 244));
        standardMode.put(OSMWayType.SUBWAY, new Color(168, 172, 190));
        standardMode.put(OSMWayType.CYCLEWAY, new Color(125, 139, 244));
        standardMode.put(OSMWayType.TERTIARYROAD, new Color(255, 255, 255));
        standardMode.put(OSMWayType.SECONDARYROAD, new Color(255, 255, 255));
        standardMode.put(OSMWayType.HIGHWAY, new Color(252, 214, 164));
        standardMode.put(OSMWayType.PLACE_OF_WORSHIP, new Color(175, 156, 141));
        standardMode.put(OSMWayType.BARRIER, new Color(128, 129, 122));
        standardMode.put(OSMWayType.HEDGE, new Color(170, 224, 203));
        standardMode.put(OSMWayType.MOTORWAY, new Color(232, 146, 162));
        standardMode.put(OSMWayType.DRAIN, new Color(60, 149, 255));
    }

    private static void initializeColorBlind() {
    }

    public enum Mode {
        STANDARD,
        COLORBLIND
    }

}

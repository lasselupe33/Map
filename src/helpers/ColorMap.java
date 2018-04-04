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
        if (standardMode == null) { initializeStandard(); }
        if (colorBlindMode == null) { initializeColorBlind(); }
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
        //standardMode.put(OSMWayType.PLACE);
        standardMode.put(OSMWayType.WATER, new Color(60, 149, 255));

        standardMode.put(OSMWayType.UNKNOWN, Color.black);
        standardMode.put(OSMWayType.ROAD, new Color(230, 139, 213));
        standardMode.put(OSMWayType.HIGHWAY, new Color(255, 114, 109));
        standardMode.put(OSMWayType.BUILDING, new Color(172, 169, 151));
    }

    private static void initializeColorBlind() {
    }

    public enum Mode {
        STANDARD,
        COLORBLIND
    }

}

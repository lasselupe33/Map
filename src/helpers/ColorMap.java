package helpers;

import controller.MenuController;
import model.osm.OSMWay;
import model.osm.OSMWayType;

import java.awt.*;
import java.util.EnumMap;

public class ColorMap {
    private static EnumMap<OSMWayType, Color[]> colorMap = null;
    private static EnumMap<OSMWayType, Color> standardMode = null;
    private static EnumMap<OSMWayType, Color> colorBlindMode = null;
    private static int mode;
    private static Color standardRoadColor = new Color(255,255,255);

    public static Color getColor(OSMWayType type) {
        if (standardMode == null) initializeStandard();
        if (colorBlindMode == null) initializeColorBlind();
        if (colorMap == null) initializeMap();
        /*switch (MenuController.getMode()) {
            case STANDARD:
                return standardMode.get(type);
            case COLORBLIND:
                return colorBlindMode.get(type);
            default:
                break;
        }*/
        //return standardMode.get(type);
        Color[] c = colorMap.get(type);
        /*
        if (MenuController.getMode() == Mode.GRAYSCALE) mode = 2;
        else if(MenuController.getMode() == Mode.COLORBLIND) mode = 1;
        else mode = 0;
        */
        switch (MenuController.getMode()) {
            case STANDARD:
                mode = 0;
                break;
            case COLORBLIND:
                mode = 1;
                break;
            case GRAYSCALE:
                mode = 2;
                break;
            default:
                break;
        }
        return c[mode];
        //return colorBlindMode.get(type);
    }

    private static void initializeMap() {
        colorMap = new EnumMap<>(OSMWayType.class);
        colorMap.put(OSMWayType.COASTLINE, new Color[] {new Color(237, 237, 237),
                new Color(249, 230, 189),
                new Color(237, 237, 237)});

        colorMap.put(OSMWayType.PLACE, new Color[] {new Color(237, 237, 237),
                new Color(249, 230, 189),
                new Color(237, 237, 237)});

        colorMap.put(OSMWayType.RESIDENTIAL, new Color[] {new Color(234, 224, 216),
                new Color(255, 13, 87),
                new Color(225, 225, 225)});

        colorMap.put(OSMWayType.FORREST, new Color[] {new Color(173, 216, 176),
                new Color(9, 180, 90),
                new Color(188,188,188)});

        colorMap.put(OSMWayType.FARMLAND, new Color[] {new Color(251, 236, 215),
                new Color(249, 120, 80),
                new Color(234,234,234)});

        colorMap.put(OSMWayType.WATER, new Color[] {new Color(170, 211, 223),
                new Color(170, 211, 223),
                new Color(155,155,155)});

        colorMap.put(OSMWayType.UNKNOWN, new Color[] {Color.black, Color.black, Color.black});

        colorMap.put(OSMWayType.BUILDING, new Color[] {new Color(223, 213, 206),
                new Color(170, 9, 59),
                new Color(214,214,214)});

        colorMap.put(OSMWayType.PITCH, new Color[] {new Color(170, 224, 203),
                new Color(9, 180, 90),
                new Color(199,199,199)});

        colorMap.put(OSMWayType.ALLOMENTS, new Color[] {new Color(238, 207, 179),
                new Color(249, 120, 80),
                new Color(208,208,208)});

        colorMap.put(OSMWayType.SERVICE, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.ROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.PEDESTRIAN, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.PARK, new Color[] {new Color(200, 250, 204),
                new Color(9, 180, 90),
                new Color(218,218,218)});

        colorMap.put(OSMWayType.PLAYGROUND, new Color[] {new Color(225, 255, 233),
                new Color(9, 180, 90),
                new Color(248,248,248)});

        colorMap.put(OSMWayType.CEMETERY, new Color[] {new Color(193, 219, 200),
                new Color(9, 180, 90),
                new Color(204,204,204)});

        colorMap.put(OSMWayType.FOOTWAY, new Color[] {new Color(250, 128, 114),
                new Color(249, 120, 249),
                new Color(164,164,164)});

        colorMap.put(OSMWayType.PATH, new Color[] {new Color(250, 128, 114),
                new Color(249, 120, 249),
                new Color(164,164,164)});

        colorMap.put(OSMWayType.FERRY, new Color[] {new Color(125, 139, 244),
                new Color(0,90,199),
                new Color(170,170,170)});

        colorMap.put(OSMWayType.SUBWAY, new Color[] {new Color(168, 172, 190),
                new Color(168, 172, 190),
                new Color(177,177,177)});

        colorMap.put(OSMWayType.CYCLEWAY, new Color[] {new Color(125, 139, 244),
                new Color(0,90,199),
                new Color(170,170,170)});

        colorMap.put(OSMWayType.TERTIARYROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.SECONDARYROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.HIGHWAY, new Color[] {new Color(252, 214, 164),
                new Color(239,239,49),
                new Color(176,176,176)});

        colorMap.put(OSMWayType.PLACE_OF_WORSHIP, new Color[] {new Color(175, 156, 141),
                new Color(139, 7, 48),
                new Color(157,157,157)});

        colorMap.put(OSMWayType.BARRIER, new Color[] {new Color(128, 129, 122),
                new Color(62, 62, 59),
                new Color(126,126,126)});

        colorMap.put(OSMWayType.HEDGE, new Color[] {new Color(170, 224, 203),
                new Color(5, 108, 54),
                new Color(199,199,199)});

        colorMap.put(OSMWayType.MOTORWAY, new Color[] {new Color(232, 146, 162),
                new Color(232,239, 0),
                new Color(180,180,180)});

        colorMap.put(OSMWayType.DRAIN, new Color[] {new Color(60, 149, 255),
                new Color(60, 149, 255),
                new Color(155,155,155)});

        colorMap.put(OSMWayType.AEROWAY, new Color[] {new Color(233, 209, 255),
                new Color(233, 209, 255),
                new Color(232,232,232)});

        colorMap.put(OSMWayType.RUNWAY, new Color[] {new Color(187, 187, 204),
                new Color(187, 187, 204),
                new Color(193,193,193)});

        colorMap.put(OSMWayType.GRASS, new Color[] {new Color(205, 235, 176),
                new Color(9, 180, 90),
                new Color(205,205,205)});

    }

    private static void initializeStandard() {
        standardMode = new EnumMap<>(OSMWayType.class);
        standardMode.put(OSMWayType.COASTLINE, new Color(237, 237, 237));
        standardMode.put(OSMWayType.PLACE, new Color(0, 239, 233));
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
        standardMode.put(OSMWayType.AEROWAY, new Color(233, 209, 255));
        standardMode.put(OSMWayType.RUNWAY, new Color(187, 187, 204));
        standardMode.put(OSMWayType.GRASS, new Color(205, 235, 176));
    }

    private static void initializeColorBlind() {
        colorBlindMode = new EnumMap<>(OSMWayType.class);
        //Color.decode("#3c8eff")
        colorBlindMode.put(OSMWayType.COASTLINE, new Color(249, 230, 189));
        colorBlindMode.put(OSMWayType.PLACE, new Color(249, 230, 189));
        colorBlindMode.put(OSMWayType.RESIDENTIAL, new Color(255, 13, 87));
        colorBlindMode.put(OSMWayType.FORREST, new Color(9, 180, 90));
        colorBlindMode.put(OSMWayType.FARMLAND, new Color(249, 120, 80));
        colorBlindMode.put(OSMWayType.WATER, new Color(60, 149, 255));
        colorBlindMode.put(OSMWayType.UNKNOWN, Color.black);
        colorBlindMode.put(OSMWayType.BUILDING, new Color(170, 9, 59));
        colorBlindMode.put(OSMWayType.PITCH, new Color(9, 180, 90));
        colorBlindMode.put(OSMWayType.ALLOMENTS, new Color(249, 120, 80));
        colorBlindMode.put(OSMWayType.SERVICE, new Color(255, 255, 255));
        colorBlindMode.put(OSMWayType.ROAD, new Color(255, 255, 255));
        colorBlindMode.put(OSMWayType.PEDESTRIAN, new Color(255, 255, 255));
        colorBlindMode.put(OSMWayType.PARK, new Color(9, 180, 90));
        colorBlindMode.put(OSMWayType.PLAYGROUND, new Color(9, 180, 90));
        colorBlindMode.put(OSMWayType.CEMETERY, new Color(9, 180, 90));
        colorBlindMode.put(OSMWayType.FOOTWAY, new Color(249, 120, 249));
        colorBlindMode.put(OSMWayType.PATH, new Color(249, 120, 249));
        colorBlindMode.put(OSMWayType.FERRY, new Color(0,90,199));
        colorBlindMode.put(OSMWayType.SUBWAY, new Color(0,9,130));
        colorBlindMode.put(OSMWayType.CYCLEWAY, new Color(0,90,199));
        colorBlindMode.put(OSMWayType.TERTIARYROAD, new Color(255, 255, 255));
        colorBlindMode.put(OSMWayType.SECONDARYROAD, new Color(255, 255, 255));
        colorBlindMode.put(OSMWayType.HIGHWAY, new Color(232,239, 0));
        colorBlindMode.put(OSMWayType.PLACE_OF_WORSHIP, new Color(139, 7, 48));
        colorBlindMode.put(OSMWayType.BARRIER, new Color(62, 62, 59));
        colorBlindMode.put(OSMWayType.HEDGE, new Color(5, 108, 54));
        colorBlindMode.put(OSMWayType.MOTORWAY, new Color(239,239,49));
        colorBlindMode.put(OSMWayType.DRAIN, new Color(60, 149, 255));
        //colorBlindMode.put(OSMWayType.AEROWAY, new Color(233, 209, 255));
        //colorBlindMode.put(OSMWayType.RUNWAY, new Color(187, 187, 204));
        colorBlindMode.put(OSMWayType.GRASS, new Color(9, 180, 90));
    }

    public enum Mode {
        STANDARD,
        GRAYSCALE,
        COLORBLIND
    }

}

package helpers;

import controller.MenuController;
import model.WayType;

import java.awt.*;
import java.util.EnumMap;

public class ColorMap {
    private static EnumMap<WayType, Color[]> colorMap = null;
    private static EnumMap<WayType, Color> standardMode = null;
    private static EnumMap<WayType, Color> colorBlindMode = null;
    private static int mode;
    private static Color standardRoadColor = new Color(255,255,255);
    private static Color coastlineColor = new Color(237,237,237);

    public static Color getColor(WayType type) {
        if (standardMode == null) initializeStandard();
        if (colorBlindMode == null) initializeColorBlind();
        if (colorMap == null) initializeMap();
        Color[] c = colorMap.get(type);
        switch (MenuController.getMode()) {
            case STANDARD:
                mode = 0;
                break;
            case PROTANOPIA:
                mode = 1;
                break;
            case DEUTERANOPIA:
                mode = 2;
                break;
            case TRITANOPIA:
                mode = 3;
                break;
            case GRAYSCALE:
                mode = 4;
                break;
            default:
                break;
        }
        return c[mode];
        //return colorBlindMode.get(type);
    }

    private static void initializeMap() {
        colorMap = new EnumMap<>(WayType.class);
        colorMap.put(WayType.COASTLINE, new Color[] {coastlineColor,coastlineColor,coastlineColor,coastlineColor,coastlineColor});

        colorMap.put(WayType.PLACE, new Color[] {coastlineColor,coastlineColor,coastlineColor,coastlineColor,coastlineColor});

        colorMap.put(WayType.RESIDENTIAL, new Color[] {new Color(219, 219, 219),
                new Color(219, 219, 219),
                new Color(219, 219, 219),
                new Color(219, 219, 219),
                new Color(219, 219, 219)});

        colorMap.put(WayType.FORREST, new Color[] {new Color(173, 216, 176),
                new Color(9, 180, 90),
                new Color(9, 180, 90),
                new Color(9, 180, 90),
                new Color(188,188,188)});

        colorMap.put(WayType.FARMLAND, new Color[] {new Color(235, 222, 201),
                new Color(235, 199, 175),
                new Color(235, 199, 175),
                new Color(235, 222, 201),
                new Color(234,234,234)});

        colorMap.put(WayType.WATER, new Color[] {new Color(122, 199, 235),
                new Color(94, 184, 223),
                new Color(122, 199, 235),
                new Color(122, 199, 235),
                new Color(155,155,155)});

        colorMap.put(WayType.UNKNOWN, new Color[] {Color.black,Color.black,Color.black, Color.black, Color.black});

        colorMap.put(WayType.BUILDING, new Color[] {new Color(215, 205, 199),
                new Color(215, 205, 199),
                new Color(215, 205, 199),
                new Color(215, 205, 199),
                new Color(214,214,214)});
        //new Color(170, 9, 59)

        colorMap.put(WayType.PITCH, new Color[] {new Color(170, 224, 203),
                new Color(7, 138, 69),
                new Color(7, 138, 69),
                new Color(7, 138, 69),
                new Color(199,199,199)});

        colorMap.put(WayType.ALLOMENTS, new Color[] {new Color(238, 207, 179),
                new Color(249, 120, 80),
                new Color(249, 120, 80),
                new Color(249, 120, 80),
                new Color(208,208,208)});

        colorMap.put(WayType.SERVICE, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.ROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.PEDESTRIAN, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.PARK, new Color[] {new Color(200, 250, 204),
                new Color(10, 205, 102),
                new Color(11, 255, 125),
                new Color(200, 250, 204),
                new Color(218,218,218)});

        colorMap.put(WayType.PLAYGROUND, new Color[] {new Color(225, 255, 233),
                new Color(9, 180, 90),
                new Color(13, 231, 116),
                new Color(9, 180, 90),
                new Color(248,248,248)});

        colorMap.put(WayType.CEMETERY, new Color[] {new Color(193, 219, 200),
                new Color(193, 219, 200),
                new Color(193, 219, 200),
                new Color(193, 219, 200),
                new Color(204,204,204)});

        colorMap.put(WayType.FOOTWAY, new Color[] {new Color(250, 128, 114),
                new Color(249, 120, 249),
                new Color(249, 120, 249),
                new Color(249, 120, 249),
                new Color(164,164,164)});

        colorMap.put(WayType.PATH, new Color[] {new Color(250, 128, 114),
                new Color(249, 120, 249),
                new Color(249, 120, 249),
                new Color(249, 120, 249),
                new Color(164,164,164)});

        colorMap.put(WayType.FERRY, new Color[] {new Color(125, 139, 244),
                new Color(0,90,199),
                new Color(0,90,199),
                new Color(0,90,199),
                new Color(170,170,170)});

        colorMap.put(WayType.SUBWAY, new Color[] {new Color(168, 172, 190),
                new Color(168, 172, 190),
                new Color(168, 172, 190),
                new Color(168, 172, 190),
                new Color(177,177,177)});

        colorMap.put(WayType.CYCLEWAY, new Color[] {new Color(125, 139, 244),
                new Color(0,90,199),
                new Color(0,90,199),
                new Color(0,90,199),
                new Color(170,170,170)});

        colorMap.put(WayType.TERTIARYROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.SECONDARYROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.HIGHWAY, new Color[] {new Color(252, 214, 164),
                new Color(244, 244, 124),
                new Color(244, 244, 124),
                new Color(239,239,49),
                new Color(176,176,176)});

        colorMap.put(WayType.PLACE_OF_WORSHIP, new Color[] {new Color(175, 156, 141),
                new Color(156, 8, 54),
                new Color(175, 156, 141),
                new Color(156, 8, 54),
                new Color(157,157,157)});

        colorMap.put(WayType.BARRIER, new Color[] {new Color(128, 129, 122),
                new Color(62, 62, 59),
                new Color(62, 62, 59),
                new Color(62, 62, 59),
                new Color(126,126,126)});

        colorMap.put(WayType.HEDGE, new Color[] {new Color(170, 224, 203),
                new Color(9, 182, 91),
                new Color(9, 182, 91),
                new Color(9, 182, 91),
                new Color(199,199,199)});

        colorMap.put(WayType.MOTORWAY, new Color[] {new Color(232, 146, 162),
                new Color(66, 94, 148),
                new Color(66, 94, 148),
                new Color(232, 146, 162),
                new Color(180,180,180)});

        colorMap.put(WayType.DRAIN, new Color[] {new Color(60, 149, 255),
                new Color(60, 149, 255),
                new Color(60, 149, 255),
                new Color(122, 199, 235),
                new Color(155,155,155)});

        colorMap.put(WayType.AEROWAY, new Color[] {new Color(233, 209, 255),
                new Color(233, 209, 255),
                new Color(233, 209, 255),
                new Color(233, 209, 255),
                new Color(232,232,232)});

        colorMap.put(WayType.RUNWAY, new Color[] {new Color(187, 187, 204),
                new Color(187, 187, 204),
                new Color(187, 187, 204),
                new Color(187, 187, 204),
                new Color(193,193,193)});

        colorMap.put(WayType.GRASS, new Color[] {new Color(205, 235, 176),
                new Color(9, 180, 90),
                new Color(53, 242, 103),
                new Color(9, 180, 90),
                new Color(205,205,205)});

    }

    private static void initializeStandard() {
        standardMode = new EnumMap<>(WayType.class);
        standardMode.put(WayType.COASTLINE, new Color(237, 237, 237));
        standardMode.put(WayType.PLACE, new Color(0, 239, 233));
        standardMode.put(WayType.RESIDENTIAL, new Color(234, 224, 216));
        standardMode.put(WayType.FORREST, new Color(173, 216, 176));
        standardMode.put(WayType.FARMLAND, new Color(251, 236, 215));
        standardMode.put(WayType.WATER, new Color(60, 149, 255));
        standardMode.put(WayType.UNKNOWN, Color.black);
        standardMode.put(WayType.BUILDING, new Color(223, 213, 206));
        standardMode.put(WayType.PITCH, new Color(170, 224, 203));
        standardMode.put(WayType.ALLOMENTS, new Color(238, 207, 179));
        standardMode.put(WayType.SERVICE, new Color(255, 255, 255));
        standardMode.put(WayType.ROAD, new Color(255, 255, 255));
        standardMode.put(WayType.PEDESTRIAN, new Color(255, 255, 255));
        standardMode.put(WayType.PARK, new Color(200, 250, 204));
        standardMode.put(WayType.PLAYGROUND, new Color(225, 255, 233));
        standardMode.put(WayType.CEMETERY, new Color(193, 219, 200));
        standardMode.put(WayType.FOOTWAY, new Color(250, 128, 114));
        standardMode.put(WayType.PATH, new Color(250, 128, 114));
        standardMode.put(WayType.FERRY, new Color(125, 139, 244));
        standardMode.put(WayType.SUBWAY, new Color(168, 172, 190));
        standardMode.put(WayType.CYCLEWAY, new Color(125, 139, 244));
        standardMode.put(WayType.TERTIARYROAD, new Color(255, 255, 255));
        standardMode.put(WayType.SECONDARYROAD, new Color(255, 255, 255));
        standardMode.put(WayType.HIGHWAY, new Color(252, 214, 164));
        standardMode.put(WayType.PLACE_OF_WORSHIP, new Color(175, 156, 141));
        standardMode.put(WayType.BARRIER, new Color(128, 129, 122));
        standardMode.put(WayType.HEDGE, new Color(170, 224, 203));
        standardMode.put(WayType.MOTORWAY, new Color(232, 146, 162));
        standardMode.put(WayType.DRAIN, new Color(60, 149, 255));
        standardMode.put(WayType.AEROWAY, new Color(233, 209, 255));
        standardMode.put(WayType.RUNWAY, new Color(187, 187, 204));
        standardMode.put(WayType.GRASS, new Color(205, 235, 176));
    }

    private static void initializeColorBlind() {
        colorBlindMode = new EnumMap<>(WayType.class);
        //Color.decode("#3c8eff")
        colorBlindMode.put(WayType.COASTLINE, new Color(249, 230, 189));
        colorBlindMode.put(WayType.PLACE, new Color(249, 230, 189));
        colorBlindMode.put(WayType.RESIDENTIAL, new Color(255, 13, 87));
        colorBlindMode.put(WayType.FORREST, new Color(9, 180, 90));
        colorBlindMode.put(WayType.FARMLAND, new Color(249, 120, 80));
        colorBlindMode.put(WayType.WATER, new Color(60, 149, 255));
        colorBlindMode.put(WayType.UNKNOWN, Color.black);
        colorBlindMode.put(WayType.BUILDING, new Color(170, 9, 59));
        colorBlindMode.put(WayType.PITCH, new Color(9, 180, 90));
        colorBlindMode.put(WayType.ALLOMENTS, new Color(249, 120, 80));
        colorBlindMode.put(WayType.SERVICE, new Color(255, 255, 255));
        colorBlindMode.put(WayType.ROAD, new Color(255, 255, 255));
        colorBlindMode.put(WayType.PEDESTRIAN, new Color(255, 255, 255));
        colorBlindMode.put(WayType.PARK, new Color(9, 180, 90));
        colorBlindMode.put(WayType.PLAYGROUND, new Color(9, 180, 90));
        colorBlindMode.put(WayType.CEMETERY, new Color(9, 180, 90));
        colorBlindMode.put(WayType.FOOTWAY, new Color(249, 120, 249));
        colorBlindMode.put(WayType.PATH, new Color(249, 120, 249));
        colorBlindMode.put(WayType.FERRY, new Color(0,90,199));
        colorBlindMode.put(WayType.SUBWAY, new Color(0,9,130));
        colorBlindMode.put(WayType.CYCLEWAY, new Color(0,90,199));
        colorBlindMode.put(WayType.TERTIARYROAD, new Color(255, 255, 255));
        colorBlindMode.put(WayType.SECONDARYROAD, new Color(255, 255, 255));
        colorBlindMode.put(WayType.HIGHWAY, new Color(232,239, 0));
        colorBlindMode.put(WayType.PLACE_OF_WORSHIP, new Color(139, 7, 48));
        colorBlindMode.put(WayType.BARRIER, new Color(62, 62, 59));
        colorBlindMode.put(WayType.HEDGE, new Color(5, 108, 54));
        colorBlindMode.put(WayType.MOTORWAY, new Color(239,239,49));
        colorBlindMode.put(WayType.DRAIN, new Color(60, 149, 255));
        //colorBlindMode.put(WayType.AEROWAY, new Color(233, 209, 255));
        //colorBlindMode.put(WayType.RUNWAY, new Color(187, 187, 204));
        colorBlindMode.put(WayType.GRASS, new Color(9, 180, 90));
    }

    public enum Mode {
        STANDARD,
        GRAYSCALE,
        DEUTERANOPIA,
        TRITANOPIA, PROTANOPIA
    }

}

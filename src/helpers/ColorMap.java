package helpers;

import model.osm.OSMWayType;

import java.awt.*;
import java.util.EnumMap;

public class ColorMap {
    private EnumMap<OSMWayType, Color[]> colorMap = null;
    private int mode = 0;
    private Color standardRoadColor = new Color(255,255,255);
    private Color coastlineColor = new Color(237,237,237);

    public void setMode(Mode m) {
        switch (m) {
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
                mode = 0;
                break;
        }
    }

    public Color getColor(OSMWayType type) {
        if (colorMap == null) initializeMap();
        Color[] c = colorMap.get(type);
        return c[mode];
    }

    private void initializeMap() {
        colorMap = new EnumMap<>(OSMWayType.class);
        colorMap.put(OSMWayType.COASTLINE, new Color[] {coastlineColor,coastlineColor,coastlineColor,coastlineColor,coastlineColor});

        colorMap.put(OSMWayType.PLACE, new Color[] {coastlineColor,coastlineColor,coastlineColor,coastlineColor,coastlineColor});

        colorMap.put(OSMWayType.RESIDENTIAL, new Color[] {new Color(219, 219, 219),
                new Color(219, 219, 219),
                new Color(219, 219, 219),
                new Color(219, 219, 219),
                new Color(219, 219, 219)});

        colorMap.put(OSMWayType.FORREST, new Color[] {new Color(173, 216, 176),
                new Color(9, 180, 90),
                new Color(9, 180, 90),
                new Color(9, 180, 90),
                new Color(188,188,188)});

        colorMap.put(OSMWayType.FARMLAND, new Color[] {new Color(235, 222, 201),
                new Color(235, 199, 175),
                new Color(235, 199, 175),
                new Color(235, 222, 201),
                new Color(234,234,234)});

        colorMap.put(OSMWayType.WATER, new Color[] {new Color(122, 199, 235),
                new Color(94, 184, 223),
                new Color(122, 199, 235),
                new Color(122, 199, 235),
                new Color(155,155,155)});

        colorMap.put(OSMWayType.UNKNOWN, new Color[] {Color.black,Color.black,Color.black, Color.black, Color.black});

        colorMap.put(OSMWayType.BUILDING, new Color[] {new Color(215, 205, 199),
                new Color(215, 205, 199),
                new Color(215, 205, 199),
                new Color(215, 205, 199),
                new Color(214,214,214)});
        //new Color(170, 9, 59)

        colorMap.put(OSMWayType.PITCH, new Color[] {new Color(170, 224, 203),
                new Color(7, 138, 69),
                new Color(7, 138, 69),
                new Color(7, 138, 69),
                new Color(199,199,199)});

        colorMap.put(OSMWayType.ALLOMENTS, new Color[] {new Color(238, 207, 179),
                new Color(249, 120, 80),
                new Color(249, 120, 80),
                new Color(249, 120, 80),
                new Color(208,208,208)});

        colorMap.put(OSMWayType.SERVICE, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.ROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.PEDESTRIAN, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.PARK, new Color[] {new Color(200, 250, 204),
                new Color(10, 205, 102),
                new Color(11, 255, 125),
                new Color(200, 250, 204),
                new Color(218,218,218)});

        colorMap.put(OSMWayType.PLAYGROUND, new Color[] {new Color(225, 255, 233),
                new Color(9, 180, 90),
                new Color(13, 231, 116),
                new Color(9, 180, 90),
                new Color(248,248,248)});

        colorMap.put(OSMWayType.CEMETERY, new Color[] {new Color(193, 219, 200),
                new Color(193, 219, 200),
                new Color(193, 219, 200),
                new Color(193, 219, 200),
                new Color(204,204,204)});

        colorMap.put(OSMWayType.FOOTWAY, new Color[] {new Color(250, 128, 114),
                new Color(249, 120, 249),
                new Color(249, 120, 249),
                new Color(249, 120, 249),
                new Color(164,164,164)});

        colorMap.put(OSMWayType.PATH, new Color[] {new Color(250, 128, 114),
                new Color(249, 120, 249),
                new Color(249, 120, 249),
                new Color(249, 120, 249),
                new Color(164,164,164)});

        colorMap.put(OSMWayType.FERRY, new Color[] {new Color(125, 139, 244),
                new Color(0,90,199),
                new Color(0,90,199),
                new Color(0,90,199),
                new Color(170,170,170)});

        colorMap.put(OSMWayType.SUBWAY, new Color[] {new Color(168, 172, 190),
                new Color(168, 172, 190),
                new Color(168, 172, 190),
                new Color(168, 172, 190),
                new Color(177,177,177)});

        colorMap.put(OSMWayType.CYCLEWAY, new Color[] {new Color(125, 139, 244),
                new Color(0,90,199),
                new Color(0,90,199),
                new Color(0,90,199),
                new Color(170,170,170)});

        colorMap.put(OSMWayType.TERTIARYROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.SECONDARYROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(OSMWayType.HIGHWAY, new Color[] {new Color(252, 214, 164),
                new Color(244, 244, 124),
                new Color(244, 244, 124),
                new Color(239,239,49),
                new Color(176,176,176)});

        colorMap.put(OSMWayType.PLACE_OF_WORSHIP, new Color[] {new Color(175, 156, 141),
                new Color(156, 8, 54),
                new Color(175, 156, 141),
                new Color(156, 8, 54),
                new Color(157,157,157)});

        colorMap.put(OSMWayType.BARRIER, new Color[] {new Color(128, 129, 122),
                new Color(62, 62, 59),
                new Color(62, 62, 59),
                new Color(62, 62, 59),
                new Color(126,126,126)});

        colorMap.put(OSMWayType.HEDGE, new Color[] {new Color(170, 224, 203),
                new Color(9, 182, 91),
                new Color(9, 182, 91),
                new Color(9, 182, 91),
                new Color(199,199,199)});

        colorMap.put(OSMWayType.MOTORWAY, new Color[] {new Color(232, 146, 162),
                new Color(66, 94, 148),
                new Color(66, 94, 148),
                new Color(232, 146, 162),
                new Color(180,180,180)});

        colorMap.put(OSMWayType.DRAIN, new Color[] {new Color(60, 149, 255),
                new Color(60, 149, 255),
                new Color(60, 149, 255),
                new Color(122, 199, 235),
                new Color(155,155,155)});

        colorMap.put(OSMWayType.AEROWAY, new Color[] {new Color(233, 209, 255),
                new Color(233, 209, 255),
                new Color(233, 209, 255),
                new Color(233, 209, 255),
                new Color(232,232,232)});

        colorMap.put(OSMWayType.RUNWAY, new Color[] {new Color(187, 187, 204),
                new Color(187, 187, 204),
                new Color(187, 187, 204),
                new Color(187, 187, 204),
                new Color(193,193,193)});

        colorMap.put(OSMWayType.GRASS, new Color[] {new Color(205, 235, 176),
                new Color(9, 180, 90),
                new Color(53, 242, 103),
                new Color(9, 180, 90),
                new Color(205,205,205)});

    }

    public enum Mode {
        STANDARD,
        GRAYSCALE,
        DEUTERANOPIA,
        TRITANOPIA, PROTANOPIA
    }

}

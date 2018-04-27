package helpers;

import model.WayType;

import java.awt.*;
import java.util.EnumMap;

public class ColorMap {
    private EnumMap<WayType, Color[]> colorMap = null;
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

    public Color getColor(WayType type) {
        if (colorMap == null) initializeMap();
        Color[] c = colorMap.get(type);
        return c[mode];
    }

    private void initializeMap() {
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


        colorMap.put(WayType.SQUARE, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

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
        /**
         * @TODO color blind trunk
         */
        colorMap.put(WayType.TRUNK, new Color[] {new Color(248, 161, 136),
                new Color(66, 94, 148),
                new Color(66, 94, 148),
                new Color(248, 161, 136),
                new Color(182, 182, 182)});
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

    public enum Mode {
        STANDARD,
        GRAYSCALE,
        DEUTERANOPIA,
        TRITANOPIA, PROTANOPIA
    }

}
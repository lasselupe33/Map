package helpers;

import model.WayType;

import java.awt.*;
import java.util.EnumMap;

public class ColorMap {
    private EnumMap<WayType, Color[]> colorMap = null;
    private int mode = 0;

    // Standard colors
    private Color standardRoadColor = new Color(255,255,255);
    private Color coastlineColor = new Color(237,237,237);
    private Color residentialColor = new Color(219, 219, 219);
    private Color buildingColor = new Color(215, 205, 199);
    private Color cemeteryColor = new Color(193, 219, 200);
    private Color pathColor = new Color(250, 128, 114);
    private Color cycleAndFerryColor = new Color(125, 139, 244);
    private Color aerowayColor = new Color(233, 209, 255);
    private Color runwayColor = new Color(187, 187, 204);
    private Color bridgeColor = new Color(206, 206, 206);
    private Color waterColor = new Color(122, 199, 235);
    private Color railwayColor = new Color(200,200,200);

    // Color blind colors
    private Color colorBlindForrest = new Color(9, 180, 90);
    private Color colorBlindFarmland = new Color(235, 199, 175);
    private Color colorBlindPitch = new Color(7, 138, 69);
    private Color colorBlindAlloments = new Color(249, 120, 80);
    private Color colorBlindPath = new Color(249, 120, 249);
    private Color colorBlindCycleAndFerry = new Color(0,90,199);
    private Color colorBlindHighway = new Color(244, 244, 124);
    private Color colorBlindBarrier = new Color(62, 62, 59);
    private Color colorBlindHedge = new Color(9, 182, 91);
    private Color colorBlindMotorWay = new Color(66, 94, 148);
    private Color colorBlindTrunk = new Color(59, 146, 148);
    private Color protanopiaWater = new Color(94, 184, 223);

    //Grayscale colors
    private Color grayscaleWater = new Color(155,155,155);
    private Color grayscalePath = new Color(164,164,164);
    private Color grayscaleCycleAndFerry = new Color(170,170,170);

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
            case GRAYSCALE:
                mode = 3;
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
        colorMap.put(WayType.COASTLINE, new Color[] {coastlineColor,coastlineColor,coastlineColor,coastlineColor});

        colorMap.put(WayType.PLACE, new Color[] {coastlineColor,coastlineColor,coastlineColor,coastlineColor});

        colorMap.put(WayType.RESIDENTIAL, new Color[] {residentialColor, residentialColor, residentialColor, residentialColor});

        colorMap.put(WayType.FORREST, new Color[] {new Color(173, 216, 176),
                colorBlindForrest,
                colorBlindForrest,
                new Color(188,188,188)});

        colorMap.put(WayType.FARMLAND, new Color[] {new Color(235, 222, 201),
                colorBlindFarmland,
                colorBlindFarmland,
                new Color(234,234,234)});

        colorMap.put(WayType.WATER, new Color[] {waterColor,
                protanopiaWater,
                waterColor,
                grayscaleWater});

        colorMap.put(WayType.UNKNOWN, new Color[] {Color.black,Color.black,Color.black, Color.black});

        colorMap.put(WayType.BUILDING, new Color[] {buildingColor,
                buildingColor,
                buildingColor,
                new Color(214,214,214)});

        colorMap.put(WayType.PITCH, new Color[] {new Color(170, 224, 203),
                colorBlindPitch,
                colorBlindPitch,
                new Color(199,199,199)});

        colorMap.put(WayType.ALLOMENTS, new Color[] {new Color(238, 207, 179),
                colorBlindAlloments,
                colorBlindAlloments,
                new Color(208,208,208)});

        colorMap.put(WayType.SERVICE, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.ROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.PEDESTRIAN, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.SQUARE, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.PARK, new Color[] {new Color(200, 250, 204),
                new Color(10, 205, 102),
                new Color(11, 255, 125),
                new Color(218,218,218)});

        colorMap.put(WayType.PLAYGROUND, new Color[] {new Color(225, 255, 233),
                new Color(9, 180, 90),
                new Color(13, 231, 116),
                new Color(248,248,248)});

        colorMap.put(WayType.CEMETERY, new Color[] {cemeteryColor,
                cemeteryColor,
                cemeteryColor,
                new Color(204,204,204)});

        colorMap.put(WayType.FOOTWAY, new Color[] {pathColor,
                colorBlindPath,
                colorBlindPath,
                grayscalePath});

        colorMap.put(WayType.PATH, new Color[] {pathColor,
                colorBlindPath,
                colorBlindPath,
                grayscalePath});

        colorMap.put(WayType.FERRY, new Color[] {cycleAndFerryColor,
                colorBlindCycleAndFerry,
                colorBlindCycleAndFerry,
                grayscaleCycleAndFerry});

        colorMap.put(WayType.CYCLEWAY, new Color[] {cycleAndFerryColor,
                colorBlindCycleAndFerry,
                colorBlindCycleAndFerry,
                grayscaleCycleAndFerry});

        colorMap.put(WayType.TERTIARYROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.SECONDARYROAD, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.HIGHWAY, new Color[] {new Color(252, 214, 164),
                colorBlindHighway,
                colorBlindHighway,
                new Color(176,176,176)});

        colorMap.put(WayType.PLACE_OF_WORSHIP, new Color[] {new Color(175, 156, 141),
                new Color(156, 8, 54),
                new Color(175, 156, 141),
                new Color(157,157,157)});

        colorMap.put(WayType.BARRIER, new Color[] {new Color(128, 129, 122),
                colorBlindBarrier,
                colorBlindBarrier,
                new Color(126,126,126)});

        colorMap.put(WayType.HEDGE, new Color[] {new Color(170, 224, 203),
                colorBlindHedge,
                colorBlindHedge,
                new Color(199,199,199)});

        colorMap.put(WayType.MOTORWAY, new Color[] {new Color(232, 146, 162),
                colorBlindMotorWay,
                colorBlindMotorWay,
                new Color(180,180,180)});

        colorMap.put(WayType.TRUNK, new Color[] {new Color(248, 161, 136),
                colorBlindTrunk,
                colorBlindTrunk,
                new Color(182, 182, 182)});

        colorMap.put(WayType.DRAIN, new Color[] {waterColor,
                protanopiaWater,
                waterColor,
                grayscaleWater});

        colorMap.put(WayType.AEROWAY, new Color[] {aerowayColor,
                aerowayColor,
                aerowayColor,
                new Color(232,232,232)});

        colorMap.put(WayType.RUNWAY, new Color[] {runwayColor,
                runwayColor,
                runwayColor,
                new Color(193,193,193)});

        colorMap.put(WayType.GRASS, new Color[] {new Color(205, 235, 176),
                new Color(9, 180, 90),
                new Color(53, 242, 103),
                new Color(205,205,205)});

        colorMap.put(WayType.PIER, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.HIGHWAYBRIDGE, new Color[] {standardRoadColor,standardRoadColor,standardRoadColor,standardRoadColor});

        colorMap.put(WayType.MANMADEBRIDGE, new Color[] {bridgeColor,bridgeColor,bridgeColor,bridgeColor});

        colorMap.put(WayType.SWIMMINGPOOL, new Color[] {waterColor,
                protanopiaWater,
                waterColor,
                grayscaleWater});

        colorMap.put(WayType.RAILWAY, new Color[] {railwayColor,railwayColor,railwayColor,railwayColor});
    }

    public boolean isGrayscale() {
        if (mode == 3) return true;
        return false;
    }

    public enum Mode {
        STANDARD,
        GRAYSCALE,
        DEUTERANOPIA,
        PROTANOPIA
    }

}
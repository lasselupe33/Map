package helpers;

import model.osm.OSMWayType;

import java.awt.*;
import java.util.EnumMap;

public class StrokeMap {
    private static EnumMap<OSMWayType, Stroke> strokeMap = null;
    private final static float dash[] = {0.00003f};
    private final static float dash2[] = {0.00006f};

    public static Stroke getStroke(OSMWayType type) {
        if (strokeMap == null) initializeStandard();
        return strokeMap.get(type);
    }

    private static void initializeStandard() {
        strokeMap = new EnumMap<>(OSMWayType.class);
        strokeMap.put(OSMWayType.UNKNOWN, new BasicStroke(0.00002f));
        strokeMap.put(OSMWayType.SERVICE, new BasicStroke(0.00002f));
        strokeMap.put(OSMWayType.ROAD, new BasicStroke(0.00010f));
        strokeMap.put(OSMWayType.PEDESTRIAN, new BasicStroke(0.00015f));
        strokeMap.put(OSMWayType.FOOTWAY, new BasicStroke(0.000009f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
        strokeMap.put(OSMWayType.PATH, new BasicStroke(0.000009f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f));
        strokeMap.put(OSMWayType.FERRY, new BasicStroke(0.000009f));
        strokeMap.put(OSMWayType.SUBWAY, new BasicStroke(0.000009f));
        strokeMap.put(OSMWayType.CYCLEWAY, new BasicStroke(0.000009f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dash2, 0.0f));
        strokeMap.put(OSMWayType.TERTIARYROAD, new BasicStroke(0.00009f));
        strokeMap.put(OSMWayType.SECONDARYROAD, new BasicStroke(0.00010f));
        strokeMap.put(OSMWayType.HIGHWAY, new BasicStroke(0.00020f));
        strokeMap.put(OSMWayType.MOTORWAY, new BasicStroke(0.00025f));
        strokeMap.put(OSMWayType.BARRIER, new BasicStroke(0.000007f));
        strokeMap.put(OSMWayType.HEDGE, new BasicStroke(0.000007f));
        strokeMap.put(OSMWayType.DRAIN, new BasicStroke(0.000007f));
        strokeMap.put(OSMWayType.RUNWAY, new BasicStroke(0.000009f));
    }
}
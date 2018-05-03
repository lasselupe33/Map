package helpers;

import model.WayType;

import java.awt.*;
import java.util.EnumMap;

public class StrokeMap {
    private static EnumMap<WayType, Stroke> strokeMap = null;
    private final static float dash[] = {0.00003f};
    private final static float dash2[] = {0.00006f};

    private static BasicStroke path = new BasicStroke(0.000015f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);

    public static Stroke getStroke(WayType type) {
        if (strokeMap == null) initializeStandard();
        return strokeMap.get(type);
    }

    private static void initializeStandard() {
        strokeMap = new EnumMap<>(WayType.class);
        strokeMap.put(WayType.UNKNOWN, new BasicStroke(0.00002f));
        strokeMap.put(WayType.SERVICE, new BasicStroke(0.00002f));
        strokeMap.put(WayType.ROAD, new BasicStroke(0.00010f));
        strokeMap.put(WayType.PEDESTRIAN, new BasicStroke(0.00015f));
        strokeMap.put(WayType.FOOTWAY, path);
        strokeMap.put(WayType.PATH, path);
        strokeMap.put(WayType.FERRY, new BasicStroke(0.000015f));
        strokeMap.put(WayType.SUBWAY, new BasicStroke(0.000015f));
        strokeMap.put(WayType.CYCLEWAY, new BasicStroke(0.000015f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dash2, 0.0f));
        strokeMap.put(WayType.TERTIARYROAD, new BasicStroke(0.00009f));
        strokeMap.put(WayType.SECONDARYROAD, new BasicStroke(0.00010f));
        strokeMap.put(WayType.RESIDENTIAL, new BasicStroke(0.00010f));
        strokeMap.put(WayType.HIGHWAY, new BasicStroke(0.00020f));
        strokeMap.put(WayType.MOTORWAY, new BasicStroke(0.00025f));
        strokeMap.put(WayType.TRUNK, new BasicStroke(0.00025f));
        strokeMap.put(WayType.BARRIER, new BasicStroke(0.000007f));
        strokeMap.put(WayType.HEDGE, new BasicStroke(0.000007f));
        strokeMap.put(WayType.DRAIN, new BasicStroke(0.000007f));
        strokeMap.put(WayType.RUNWAY, new BasicStroke(0.000009f));
        strokeMap.put(WayType.HIGHWAYBRIDGE, new BasicStroke(0.00009f));
    }
}
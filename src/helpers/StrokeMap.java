package helpers;

import controller.MapController;
import model.WayType;

import java.awt.*;
import java.util.EnumMap;

public class StrokeMap {
    private static EnumMap<WayType, Float> strokeMap = null;
    private static float factor = 1;

    private final static float dash[] = {0.00003f};
    private final static float dash2[] = {0.00006f};

    public static Stroke getStroke(WayType type) {
        if (strokeMap == null) {initializeMap();}

        float zoomLevel = MapController.getZoomLevel();

        if (zoomLevel < 500) {
            factor = (511 - zoomLevel) * (zoomLevel / 10);
            float width = strokeMap.get(type) * factor / zoomLevel;
            return basicStroke(type, width);
        } else {
            return basicStroke(type, strokeMap.get(type));
        }
    }

    private static BasicStroke basicStroke(WayType type, float width) {
        switch (type) {
            case PATH:
            case FOOTWAY:
                return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);
            case CYCLEWAY:
                return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 1.0f, dash2, 0.0f);
            default:
                return new BasicStroke(width);
        }
    }

    private static void initializeMap() {
        strokeMap = new EnumMap<>(WayType.class);
        strokeMap.put(WayType.UNKNOWN, 0.00002f);
        strokeMap.put(WayType.SERVICE, 0.00002f);
        strokeMap.put(WayType.ROAD, 0.00010f);
        strokeMap.put(WayType.PEDESTRIAN, 0.00015f);
        strokeMap.put(WayType.FOOTWAY, 0.000015f);
        strokeMap.put(WayType.PATH, 0.000015f);
        strokeMap.put(WayType.FERRY, 0.000015f);
        strokeMap.put(WayType.SUBWAY, 0.000015f);
        strokeMap.put(WayType.CYCLEWAY, 0.000015f);
        strokeMap.put(WayType.TERTIARYROAD, 0.00009f);
        strokeMap.put(WayType.SECONDARYROAD, 0.00010f);
        strokeMap.put(WayType.RESIDENTIAL, 0.00010f);
        strokeMap.put(WayType.HIGHWAY, 0.00020f);
        strokeMap.put(WayType.MOTORWAY, 0.00025f);
        strokeMap.put(WayType.TRUNK, 0.00025f);
        strokeMap.put(WayType.BARRIER, 0.000007f);
        strokeMap.put(WayType.HEDGE, 0.000007f);
        strokeMap.put(WayType.DRAIN, 0.000007f);
        strokeMap.put(WayType.RUNWAY, 0.000009f);
        strokeMap.put(WayType.HIGHWAYBRIDGE, 0.00009f);
    }
}
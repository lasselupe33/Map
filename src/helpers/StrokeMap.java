package helpers;

import controller.MapController;
import model.WayType;

import java.awt.BasicStroke;
import java.util.EnumMap;

/**
 * Map stroke to element type
 */
public class StrokeMap {
    private static EnumMap<WayType, Float> strokeMap = null;

    /**
     * Return stroke based on element type and zoom level
     * @param type map element type
     * @return a BasicStroke
     */
    public static BasicStroke getStroke(WayType type, float zoomLevel) {
        // Initialize map if it hasn't been done
        if (strokeMap == null) {initializeMap();}

        // If zoom level is less than 500 then multiply the width
        // of the stroke with a factor based on zoom level
        if (zoomLevel < 500) {
            float factor = (511 - zoomLevel) * (zoomLevel / 10);
            float width = strokeMap.get(type) * factor / zoomLevel;
            return basicStroke(type, width);
        } else {
            return basicStroke(type, strokeMap.get(type));
        }
    }

    /**
     * Create stroke based on vehicle type
     * @param type map element type
     * @param width stroke width
     * @return a stroke
     */
    private static BasicStroke basicStroke(WayType type, float width) {
        switch (type) {
            case PATH:
            case FOOTWAY:
                return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 1.0f, new float[] {0.00003f}, 0.0f);
            case CYCLEWAY:
                return new BasicStroke(width, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 1.0f, new float[] {0.00006f}, 0.0f);
            default:
                return new BasicStroke(width);
        }
    }

    /**
     * Initialize stroke map
     */
    private static void initializeMap() {
        strokeMap = new EnumMap<>(WayType.class);
        strokeMap.put(WayType.SERVICE, 0.00002f);
        strokeMap.put(WayType.ROAD, 0.00010f);
        strokeMap.put(WayType.PEDESTRIAN, 0.00015f);
        strokeMap.put(WayType.FOOTWAY, 0.000015f);
        strokeMap.put(WayType.PATH, 0.000015f);
        strokeMap.put(WayType.FERRY, 0.000015f);
        strokeMap.put(WayType.CYCLEWAY, 0.000015f);
        strokeMap.put(WayType.TERTIARYROAD, 0.00009f);
        strokeMap.put(WayType.SECONDARYROAD, 0.00010f);
        strokeMap.put(WayType.RESIDENTIAL, 0.00010f);
        strokeMap.put(WayType.HIGHWAY, 0.00020f);
        strokeMap.put(WayType.MOTORWAY, 0.00025f);
        strokeMap.put(WayType.TRUNK, 0.00025f);
        strokeMap.put(WayType.BARRIER, 0.000007f);
        strokeMap.put(WayType.HEDGE, 0.000007f);
        strokeMap.put(WayType.PIER, 0.000017f);
        strokeMap.put(WayType.DRAIN, 0.0000015f);
        strokeMap.put(WayType.RUNWAY, 0.000009f);
        strokeMap.put(WayType.HIGHWAYBRIDGE, 0.00009f);
        strokeMap.put(WayType.RAILWAY, 0.00002f);
    }
}
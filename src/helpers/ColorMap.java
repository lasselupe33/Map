package helpers;

import model.osm.OSMWayType;

import java.awt.*;
import java.util.EnumMap;

public class ColorMap {
    private static EnumMap<OSMWayType, Color> colorMap = null;

    public Color getColor(OSMWayType type) {
        if (colorMap == null) {
            initializeMap();
        }

        return
    }

    private void initializeMap() {
        colorMap = new EnumMap<>(OSMWayType.class);
        colorMap.put(OSMWayType.COASTLINE, new Color(237, 237, 237));
        colorMap.put(OSMWayType.PLACE);
        colorMap.put(OSMWayType.WATER, new Color(60, 149, 255));

        ,

                WATER,
                UNKNOWN,
                BUILDING,
                PITCH,
                ALLOMENTS,
                SERVICE,
                ROAD,
                PEDESTRIAN,
                PARK,
                PLAYGROUND,
                CEMETERY,
                FOOTWAY,
                PATH,
                FERRY,
                SUBWAY,
                CYCLEWAY,
                TERTIARYROAD,
                SECONDARYROAD,
                HIGHWAY,
                PLACE_OF_WORSHIP

    }


}

package model.osm;

import java.io.Serializable;

public enum OSMWayType implements Serializable {
    WATER(440),
    FORREST(460),
    COASTLINE(1),
    MOTORWAY(1),
    HIGHWAY(200),
    PLACE(5000), // Shouldn't be drawn
    RESIDENTIAL(330),
    FARMLAND(500),
    SECONDARYROAD(500),
    PITCH(500),
    FERRY(500),
    SUBWAY(500),
    ROAD(500),
    TERTIARYROAD(500),
    SERVICE(500),
    CYCLEWAY(500),
    PARK(500),
    PLAYGROUND(500),
    CEMETERY(500),
    ALLOMENTS(500),
    BUILDING(500),
    PLACE_OF_WORSHIP(500),
    HEDGE(500),
    BARRIER(500),
    PATH(500),
    FOOTWAY(500),
    PEDESTRIAN(500),
    UNKNOWN(500);

    private final int priority;

    OSMWayType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

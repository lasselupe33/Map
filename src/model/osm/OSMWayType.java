package model.osm;

import java.io.Serializable;

public enum OSMWayType implements Serializable {
    COASTLINE(1),
    PLACE(500),
    RESIDENTIAL(500),
    FARMLAND(500),
    AEROWAY(500),
    PARK(500),
    FORREST(460),
    WATER(440),
    UNKNOWN(500),
    BUILDING(500),
    PITCH(500),
    ALLOMENTS(500),
    GRASS(500),
    SERVICE(500),
    CYCLEWAY(500),
    MOTORWAY(500),
    ROAD(500),
    PEDESTRIAN(500),
    PLAYGROUND(500),
    CEMETERY(500),
    FOOTWAY(500),
    PATH(500),
    FERRY(500),
    SUBWAY(500),
    TERTIARYROAD(500),
    SECONDARYROAD(500),
    HIGHWAY(500),
    PLACE_OF_WORSHIP(500),
    BARRIER(500),
    DRAIN(500),
    RUNWAY(500),
    HEDGE(500);

    private final int priority;

    OSMWayType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

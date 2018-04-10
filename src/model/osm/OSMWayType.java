package model.osm;

import java.io.Serializable;

public enum OSMWayType implements Serializable {
    COASTLINE(1),
    PLACE(490),
    RESIDENTIAL(300),
    FARMLAND(490),
    AEROWAY(490),
    PARK(490),
    FORREST(380),
    WATER(400),
    UNKNOWN(500),
    BUILDING(490),
    PITCH(490),
    ALLOMENTS(490),
    GRASS(490),
    SERVICE(490),
    CYCLEWAY(490),
    MOTORWAY(1),
    ROAD(490),
    PEDESTRIAN(490),
    PLAYGROUND(490),
    CEMETERY(490),
    FOOTWAY(490),
    PATH(490),
    FERRY(490),
    SUBWAY(490),
    TERTIARYROAD(450),
    SECONDARYROAD(450),
    HIGHWAY(490),
    PLACE_OF_WORSHIP(490),
    BARRIER(490),
    DRAIN(490),
    RUNWAY(490),
    HEDGE(490);

    private final int priority;

    OSMWayType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

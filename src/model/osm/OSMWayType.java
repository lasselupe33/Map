package model.osm;

import java.io.Serializable;

public enum OSMWayType implements Serializable {
    COASTLINE(1),
    PLACE(490),
    RESIDENTIAL(300),
    FARMLAND(370),
    AEROWAY(490),
    PARK(485),
    FORREST(370),
    WATER(400),
    BUILDING(509),
    PITCH(490),
    ALLOMENTS(495),
    GRASS(485),
    SERVICE(490),
    CYCLEWAY(508),
    MOTORWAY(1),
    TRUNK(390),
    ROAD(490),
    PEDESTRIAN(500),
    SQUARE(500),
    PLAYGROUND(490),
    CEMETERY(500),
    FOOTWAY(509),
    PATH(509),
    FERRY(490),
    SUBWAY(490),
    TERTIARYROAD(450),
    SECONDARYROAD(450),
    HIGHWAY(390),
    PLACE_OF_WORSHIP(490),
    BARRIER(509),
    DRAIN(490),
    RUNWAY(490),
    HEDGE(509),
    UNKNOWN(510);

    private final int priority;

    OSMWayType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

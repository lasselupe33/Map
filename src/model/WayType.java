package model;

import java.io.Serializable;

public enum WayType implements Serializable {
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

    /**
     * The priority of a mapElement specifies when it'll be rendered on the map.
     *
     * A priority of 1 means that the mapElement will be drawn at a distance of 50km's/100px while a priority of
     * 500 means that the mapElement will be drawn at a distance of 100m's/100px.
     *
     * Each priority step has 100m's interval, i.e. a priority of 100 would result in rendering at a distance of 40km's/100px.
     */
    private final int priority;

    WayType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}

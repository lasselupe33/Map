package model.osm;

public enum OSMWayType {
    COASTLINE(-100),

    PLACE(zoomValue),

    WATER(-100),

    UNKNOWN(50),

    BUILDING(40),

    PITCH(zoomValue),

    ALLOMENTS(zoomValue),

    SERVICE(zoomValue),

    ROAD(35),

    PEDESTRIAN(zoomValue),

    PARK(zoomValue),

    PLAYGROUND(zoomValue),

    CEMETERY(zoomValue),

    FOOTWAY(zoomValue),

    PATH(zoomValue),

    FERRY(zoomValue),

    SUBWAY(zoomValue),

    CYCLEWAY(zoomValue),

    TERTIARYROAD(zoomValue),

    SECONDARYROAD(zoomValue),

    HIGHWAY(10),

    PLACE_OF_WORSHIP(40);

    private final int zoomValue;

    OSMWayType(int zoomValue) {
        this.zoomValue = zoomValue;
    }

    public int getZoomValue() {
        return zoomValue;
    }
}

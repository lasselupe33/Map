package model.osm;

public enum OSMWayType {
    COASTLINE(-100),
    PLACE(0),
    WATER(-100),
    UNKNOWN(50),
    BUILDING(40),
    PITCH(0),
    ALLOMENTS(0),
    SERVICE(0),
    ROAD(35),
    PEDESTRIAN(0),
    PARK(0),
    PLAYGROUND(0),
    CEMETERY(0),
    FOOTWAY(0),
    PATH(0),
    FERRY(0),
    SUBWAY(0),
    CYCLEWAY(0),
    TERTIARYROAD(0),
    SECONDARYROAD(0),
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

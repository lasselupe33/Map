package model.osm;

public enum OSMWayType {
    COASTLINE(-100), WATER(-100), UNKNOWN(50), ROAD(35), HIGHWAY(10), BUILDING(40);

    private final int zoomValue;

    OSMWayType(int zoomValue) { this.zoomValue = zoomValue; }

    public int getZoomValue(){ return zoomValue; }
}

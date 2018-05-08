package parsing;

import helpers.structures.SimpleLongSet;

public class OSMNode {
    private long id;
    private float lon;
    private float lat;
    private SimpleLongSet refs;
    private SimpleLongSet wayIds;

    public OSMNode() {
    }

    public OSMNode(long id, float lon, float lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        refs = new SimpleLongSet();
        wayIds = new SimpleLongSet();
    }

    public long getId() {
        return id;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void addRef(long id) {
        refs.add(id);
    }

    public void addWayId(long wayId) {
        wayIds.add(wayId);
    }

    public SimpleLongSet getRefs() { return refs; }

    public SimpleLongSet getWayIds() {
        return wayIds;
    }
}

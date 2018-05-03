package parsing;

import java.util.HashSet;

public class OSMNode {
    private HashSet<Long> wayIds;
    private long id;
    private float lon;
    private float lat;
    private HashSet<Long> refs;

    public OSMNode() {
    }

    public OSMNode(long id, float lon, float lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.refs = new HashSet<>();
        this.wayIds = new HashSet<>();
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

    public HashSet<Long> getRefs() { return refs; }

    public HashSet<Long> getWayIds() {
        return wayIds;
    }
}

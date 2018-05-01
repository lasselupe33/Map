package parsing;

public class OSMNode {
    private long id;
    private float lon;
    private float lat;

    public OSMNode() {}
    public OSMNode(long id, float lon, float lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
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

    public void setId(long id) { this.id = id; }

    public void setLon(float lon) { this.lon = lon; }

    public void setLat(float lat) { this.lat = lat; }
}

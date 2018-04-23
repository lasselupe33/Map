package helpers;

import model.osm.OSMNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class LongToOSMNodeMapTest {
    private LongToOSMNodeMap map;
    private long id = 12345;
    private double lat = 55.6598896;
    private double lon = 12.5911909;
    private long wrongId = 01234;
    private double wrongLat = 55.6598895;
    private double wrongLon = 12.5911908;

    @Test
    public void putAndGet() throws Exception {
        map = new LongToOSMNodeMap(2);

        map.put(id, lon, lat);
        OSMNode node = map.get(id);

        assertEquals(lat, node.getLat(), 0);
        assertEquals(lon, node.getLon(), 0);

        assertFalse(node.getLat() == wrongLat);
        assertFalse(node.getLon() == wrongLon);

        map.put(wrongId, wrongLon, wrongId);
        assertFalse(node == map.get(wrongId));
    }

    @Test
    public void wrongID() throws Exception {
        map = new LongToOSMNodeMap(2);

        map.put(id, lon, lat);

        assertEquals(null, map.get(wrongId));
    }

}
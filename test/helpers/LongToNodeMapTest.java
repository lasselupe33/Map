package helpers;

import helpers.structures.LongToNodeMap;
import model.graph.Node;
import org.junit.Test;
import parsing.OSMNode;

import static org.junit.Assert.*;

public class LongToNodeMapTest {
    private LongToNodeMap<OSMNode> map;
    private long id = 12345;
    private float lat = (float) 55.6598896;
    private float lon = (float) 12.5911909;
    private long wrongId = 01234;
    private float wrongLat = (float) 55.6498896;
    private float wrongLon = (float) 12.5911908;

    @Test
    public void putAndGet() throws Exception {
        map = new LongToNodeMap(2);

        map.put(new OSMNode(id, lon, lat));
        OSMNode node = map.get(id);

        assertEquals(lat, node.getLat(), 0);
        assertEquals(lon, node.getLon(), 0);

        assertFalse(node.getLat() == wrongLat);
        assertFalse(node.getLon() == wrongLon);

        map.put(new OSMNode(wrongId, wrongLon, wrongId));
        assertFalse(node.getId() == map.get(wrongId).getId());
    }

    @Test
    public void wrongID() throws Exception {
        map = new LongToNodeMap(2);

        map.put(new OSMNode(id, lon, lat));

        assertEquals(null, map.get(wrongId));
    }
}
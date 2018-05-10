package helpers;

import helpers.io.IOHandler;
import helpers.structures.KDTree;
import model.*;
import model.graph.Graph;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class OSMHandlerTest {
    private static MetaModel m;
    private static MapModel mm;
    private static AddressesModel am;
    private static Graph g;
    private static FavoritesModel fm;

    @BeforeClass
    public static void setup() throws Exception {
        m = new MetaModel();
        g = new Graph();
        mm = new MapModel(m, g);
        am = new AddressesModel();
        fm = new FavoritesModel();


        IOHandler.instance.addModels(m, mm, am, g, fm);
        IOHandler.instance.loadFromString("./test/data/tiny.osm");

        // Give time to parse osm on another thread
        Thread.sleep(1000);
    }

    @Test
    public void testBounds() throws Exception {
        assertEquals(-55.6631000, m.getMinLat(), 0);
        assertEquals(-55.6804000, m.getMaxLat(), 0);
        assertEquals(7.090333289649874, m.getMinLon(), 0);
        assertEquals(7.107307681761419, m.getMaxLon(), 0);
    }

    @Test
    public void testMapModel() throws Exception {
        // Should have one road
        assertEquals(1, mm.getMapElements(WayType.ROAD).size());

        // Should be of type mapElement
        assertTrue(mm.getMapElements(WayType.ROAD).get(0) instanceof MapElement);

        // Reset model
        mm.reset();

        // Expect everything to be empty
        assertNull(mm.getTree(0));
        assertEquals(0, mm.getMapElements(WayType.ROAD).size());
    }

    @Test
    public void testAddressModel() throws Exception {

    }
}
package helpers;

import model.WayType;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class StrokeMapTest {
    private StrokeMap map;
    @Test
    /**
     * Test that the correct stroke is returned when the zoom level is less than 500
     */
    public void zoomLevelLessThan500() throws Exception {
        map = new StrokeMap();

        float zoomLevel = 407;
        WayType type = WayType.PATH;
        assertEquals(new BasicStroke((float)1.56E-4, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, new float[] {0.00003f}, 0.0f), map.getStroke(type, zoomLevel));

        type = WayType.FOOTWAY;
        assertEquals(new BasicStroke((float)1.56E-4, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, new float[] {0.00003f}, 0.0f), map.getStroke(type, zoomLevel));

        type = WayType.CYCLEWAY;
        assertEquals(new BasicStroke((float)1.56E-4, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, new float[] {0.00006f}, 0.0f), map.getStroke(type, zoomLevel));

        zoomLevel = 498;

        type = WayType.HIGHWAY;
        assertEquals(new BasicStroke((float) 2.5999997E-4), map.getStroke(type, zoomLevel));

        zoomLevel = 1;

        type = WayType.MOTORWAY;
        assertEquals(new BasicStroke((float) 0.012750001), map.getStroke(type, zoomLevel));
    }

    @Test
    /**
     * Test that the correct stroke is returned when the zoom level is 500
     * The same strokes are returned for zoom levels greater than 500 so a test for i.g.
     * 505 would be a dublicate of this test with the zoom level being the only thing different.
     * Therefore is has been deemed unnecessary
     */
    public void zoomLevel500() throws Exception {
        map = new StrokeMap();

        float zoomLevel = 500;
        WayType type = WayType.PATH;
        assertEquals(new BasicStroke(0.000015f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, new float[] {0.00003f}, 0.0f), map.getStroke(type, zoomLevel));

        type = WayType.FOOTWAY;
        assertEquals(new BasicStroke(0.000015f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, new float[] {0.00003f}, 0.0f), map.getStroke(type, zoomLevel));

        type = WayType.CYCLEWAY;
        assertEquals(new BasicStroke(0.000015f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, new float[] {0.00006f}, 0.0f), map.getStroke(type, zoomLevel));

        type = WayType.HIGHWAY;
        assertEquals(new BasicStroke(0.00020f), map.getStroke(type, zoomLevel));

        type = WayType.MOTORWAY;
        assertEquals(new BasicStroke(0.00025f), map.getStroke(type, zoomLevel));
    }
}
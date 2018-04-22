package helpers;

import model.osm.OSMWayType;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class StrokeMapTest {
    private StrokeMap map;
    @Test
    public void getStroke() throws Exception {
        map = new StrokeMap();

        OSMWayType type = OSMWayType.PATH;
        float dash[] = {0.00003f};
        assertEquals(new BasicStroke(0.000009f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f), map.getStroke(type));

        type = OSMWayType.CYCLEWAY;
        float dash2[] = {0.00006f};
        assertEquals(new BasicStroke(0.000009f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dash2, 0.0f), map.getStroke(type));

        type = OSMWayType.ROAD;
        assertEquals(new BasicStroke(0.00010f), map.getStroke(type));

        type = OSMWayType.MOTORWAY;
        assertEquals(new BasicStroke(0.00025f), map.getStroke(type));
    }

}
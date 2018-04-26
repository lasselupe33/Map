package helpers;

import model.WayType;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ColorMapTest {
    private ColorMap map;

    @Test
    //This test works alone but it doesn't pass when it is run together with the other tests
    public void testDefault() throws Exception {
        map = new ColorMap();
        WayType type = WayType.GRASS;
        assertEquals(new Color(205, 235, 176), map.getColor(type));
    }

    @Test
    public void getColorBuildings() throws Exception {
        map = new ColorMap();
        WayType type = WayType.RESIDENTIAL;
        map.setMode(ColorMap.Mode.STANDARD);
        assertEquals(new Color(219, 219, 219), map.getColor(type));
        map.setMode(ColorMap.Mode.PROTANOPIA);
        assertEquals(new Color(219, 219, 219), map.getColor(type));
        map.setMode(ColorMap.Mode.DEUTERANOPIA);
        assertEquals(new Color(219, 219, 219), map.getColor(type));
        map.setMode(ColorMap.Mode.TRITANOPIA);
        assertEquals(new Color(219, 219, 219), map.getColor(type));
        map.setMode(ColorMap.Mode.GRAYSCALE);
        assertEquals(new Color(219, 219, 219), map.getColor(type));
    }

    @Test
    public void getColorGrass() throws Exception {
        map = new ColorMap();
        WayType type = WayType.GRASS;
        map.setMode(ColorMap.Mode.STANDARD);
        assertEquals(new Color(205, 235, 176), map.getColor(type));
        map.setMode(ColorMap.Mode.PROTANOPIA);
        assertEquals(new Color(9, 180, 90), map.getColor(type));
        map.setMode(ColorMap.Mode.DEUTERANOPIA);
        assertEquals(new Color(53, 242, 103), map.getColor(type));
        map.setMode(ColorMap.Mode.TRITANOPIA);
        assertEquals(new Color(9, 180, 90), map.getColor(type));
        map.setMode(ColorMap.Mode.GRAYSCALE);
        assertEquals(new Color(205,205,205), map.getColor(type));
    }
}
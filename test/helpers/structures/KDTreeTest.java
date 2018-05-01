package helpers.structures;

import model.Coordinates;
import model.MapElement;
import model.WayType;
import model.graph.Node;
import org.junit.Test;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KDTreeTest {
    @Test
    public void searchTreeCoordinates() throws Exception {

        /* Expected results */
        List<Coordinates> expectedResult = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            expectedResult.add(new Coordinates(i, i));
        }

        /* Actual results */
        List<Coordinates> list = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            list.add(new Coordinates(i, i));
        }

        KDTree<Coordinates> tree = new KDTree(list);

        List<Coordinates> actualResult = tree.searchTree(new Point2D.Double(8, 17), new Point2D.Double(100, 190));

        /* Are they equal? */
        for (int i = 0; i < actualResult.size(); i++) {
            String expected = "" + expectedResult.get(i);
            String actual = "" + actualResult.get(i);
            assertEquals(expected, actual);
        }

    }

    @Test
    public void searchTreeMapElements() throws Exception {
        List<MapElement> expectedResults = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Ellipse2D shape = new Ellipse2D.Float(i, i, 300, 100);
            MapElement me = new MapElement(i, i, shape, WayType.UNKNOWN, true, new ArrayList<>());
            expectedResults.add(me);
        }

        List<MapElement> actualResults = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            Ellipse2D shape = new Ellipse2D.Float(i, i, 300, 100);
            MapElement me = new MapElement(i, i, shape, WayType.UNKNOWN, true, new ArrayList<>());
            actualResults.add(me);
        }

        KDTree<MapElement> tree = new KDTree(actualResults);

        actualResults = tree.searchTree(new Point2D.Double(1, 2), new Point2D.Double(3, 4));


        for (int i = 0; i < actualResults.size(); i++) {
            String expected = "" + expectedResults.get(i).getBounds();
            String actual = "" + actualResults.get(i).getBounds();

            assertEquals(expected, actual);
        }
    }

    @Test
    public void nearestNeighbour() throws Exception {

        /* Expected results */
        Coordinates expected = new Coordinates(10, 10);

        /* Actual results */
        List<Coordinates> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(new Coordinates(i, i));
        }
        KDTree<Coordinates> tree = new KDTree(list);
        Coordinates actual = tree.nearestNeighbour(9.99, 10.5);

        /* Are they equal? */
        assertEquals("" + expected, "" + actual);
    }
}
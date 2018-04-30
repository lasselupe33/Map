package helpers.structures;

import model.Coordinates;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KDTreeTest {
    @Test
    public void searchTree() throws Exception {

        /* Expected results */
        List<Coordinates> expectedResult = new ArrayList<>();
        for (int i = 0; i < 781; i++) {
            expectedResult.add(new Coordinates(i, i));
        }

        /* Actual results */
        List<Coordinates> list = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            list.add(new Coordinates(i, i));
        }

        KDTree<Coordinates> tree = new KDTree(list);

        List<Coordinates> actualResult = tree.searchTree(new Point2D.Double(8, 17), new Point2D.Double(10, 19));

        /* Are they equal? */
        for (int i = 0; i < actualResult.size(); i++) {
            String expected = "" + expectedResult.get(i);
            String actual = "" + actualResult.get(i);
            assertEquals(expected, actual);
        }

    }

    @Test
    public void nearestNeighbour() throws Exception {


        Coordinates expected = new Coordinates(10, 10);

        List<Coordinates> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            list.add(new Coordinates(i, i));
        }
        KDTree<Coordinates> tree = new KDTree(list);
        Coordinates actual = tree.nearestNeighbour(9.99, 10.5);

        assertEquals("" + expected, "" + actual);
    }
}
package helpers.structures;

import model.Coordinates;
import model.MapElement;
import model.WayType;
import org.junit.Before;
import org.junit.Test;
import parsing.OSMNode;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class WhiteBoxKDTreeTest {
    KDTree<Coordinates> kdTree;
    List<Coordinates> coordinates;
    Random randomGenerator;
    Point2D p0;
    Point2D p1;

    @Before
    public void setUp(){
        randomGenerator = new Random();
        coordinates = new ArrayList<>();
    }

    @Test
    public void searchTree() {
        for(int i = 0; i < 10000; i++){
            coordinates.add(new Coordinates(randomGenerator.nextInt(1000), randomGenerator.nextInt(1000)));
        }
        kdTree = new KDTree<>(coordinates, 1000, 0, 1000, 0);
        p1 = new Point2D.Double(700, 300);
        p0 = new Point2D.Double(400, 600);

        List<Coordinates> treeCoordinates = kdTree.searchTree(p0, p1);
        for(int i = 0; i < treeCoordinates.size(); i++){
            assertEquals(true, coordinates.contains(treeCoordinates.get(i)));
        }
    }

    @Test
    public void mapElement(){
        ArrayList<OSMNode> nodes = new ArrayList<>();
        MapElement mapElement;
        for(int i = 0; i < 10000; i++){

            int x = 400;
            int y = 100;
            mapElement = new MapElement(x, y, new Rectangle2D.Double(x, y, 100, 100), WayType.BUILDING, true, nodes);
            coordinates.add(mapElement);
        }
        kdTree = new KDTree<>(coordinates, 200, 0, 100, 0);
        //todo cover 106 to 112 + 193
        //TEMP!
        assertTrue(true);
    }



    @Test
    public void searchTreeCoordinates() throws Exception {

        /* Expected results */
        List<Coordinates> expectedResult = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            expectedResult.add(new Coordinates(i, i));
        }

        /* Actual results */
        List<Coordinates> list = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
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
            MapElement me = new MapElement(i, i, shape, WayType.ROAD, true, new ArrayList<>());
            expectedResults.add(me);
        }

        List<MapElement> actualResults = new ArrayList<>();

        for (int i = 0; i < 1000000; i++) {
            Ellipse2D shape = new Ellipse2D.Float(i, i, 300, 100);
            MapElement me = new MapElement(i, i, shape, WayType.ROAD, true, new ArrayList<>());
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
    public void nearestNeighbour() {
        coordinates.add(new Coordinates(5, 6));
        coordinates.add(new Coordinates(2, 4));
        coordinates.add(new Coordinates(9, 1));
        coordinates.add(new Coordinates(3, 3));
        kdTree = new KDTree<>(coordinates);

        Coordinates nearestNeighbour = kdTree.nearestNeighbour(10, 4);
        assertEquals(coordinates.get(2), nearestNeighbour);

        nearestNeighbour = kdTree.nearestNeighbour(4, 4);
        assertEquals(coordinates.get(3), nearestNeighbour);

        nearestNeighbour = kdTree.nearestNeighbour(1, 3);
        assertEquals(coordinates.get(1), nearestNeighbour);

        nearestNeighbour = kdTree.nearestNeighbour(4, 8);
        assertEquals(coordinates.get(0), nearestNeighbour);
    }

    @Test
    public void nearestNeighbour2() throws Exception {


        /* Expected results */
        Coordinates expected = new Coordinates(10, 10);

        /* Actual results */
        List<Coordinates> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            list.add(new Coordinates(i, i));
        }
        KDTree<Coordinates> tree = new KDTree(list);
        Coordinates actual = tree.nearestNeighbour(9.99, 10.5);

        /* Are they equal? */
        assertEquals("" + expected, "" + actual);

    }


}
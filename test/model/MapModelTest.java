package model;

import helpers.structures.KDTree;
import model.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class MapModelTest {
    MapModel mapModel;

    @Before
    public void setUp() throws Exception {
        mapModel = new MapModel(new MetaModel(), new Graph());

    }

    @Test(expected = NullPointerException.class)
    public void testReset() {
        mapModel.reset();
        mapModel.getTree(0);
    }

    @Test
    public void createTrees() {
        mapModel.createTrees();
        KDTree tree = mapModel.getTree(1);
        assertNotEquals(null, tree);
    }
}
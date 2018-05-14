package model.graph;

import model.Address;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class GraphTest {
    private static Graph g;
    private static Node start;
    private static Node dest;
    private static Address startAddr;
    private static Address endAddr;
    private static Node errNode;

    @BeforeClass
    public static void setupGraph() {
        // Build test-case
        g = new Graph();

        startAddr = new Address("Engbovej", "58D", "2610");
        endAddr = new Address("Testaddr", "1", "2000");

        Node node1 = new Node(1, (float) 55.714929, (float) 12.442308);
        Node node2 = new Node(2, (float) 55.691351, (float) 12.521573);
        Node node3 = new Node(3, (float) 55.688528, (float) 12.439950);
        Node node4 = new Node(4, (float) 55.2, (float) 55.1);

        start = node1;
        dest = node3;
        errNode = node4;

        // Create edges between nodes
        Edge edge1 = new Edge(1, 3, new ArrayList<>(), "Edge1", 2939000, 20, true, true, false);
        g.putEdge(edge1);
        node1.addEdge(0);
        node3.addEdge(0);

        Edge edge2 = new Edge(2, 3, new ArrayList<>(), "Edge2", 5126000, 130, true, true, true);
        g.putEdge(edge2);
        node2.addEdge(1);
        node3.addEdge(1);

        Edge edge3 = new Edge(1, 2, new ArrayList<>(), "Edge3", 5616000, 130, true, true, true);
        g.putEdge(edge3);
        node1.addEdge(2);
        node2.addEdge(2);

        g.putNode(node1);
        g.putNode(node2);
        g.putNode(node3);

        g.finalizeNodes();
    }

    @Test
    public void testFastestCarRoute() {
        g.setRouteType(RouteType.FASTEST);
        g.setVehicleType(VehicleType.CAR);

        g.computePath(start, dest, startAddr, endAddr);

        // We expect that we have visited the edge from node1 to node2 and to node2 to node3 even though the path is physically longer, the
        // speed limit is waay higher
        assertTrue(g.getLength().equals("10.74km"));
        assertTrue(g.getTime().equals("4min 57sek"));
    }

    @Test
    public void testShortestCarRoute() {
        g.setRouteType(RouteType.SHORTEST);
        g.setVehicleType(VehicleType.CAR);

        g.computePath(start, dest, startAddr, endAddr);

        assertTrue(g.getLength().equals("2.94km"));
        assertTrue(g.getTime().equals("8min 49sek"));
    }

    @Test
    public void testPedestrianRoute() {
        g.setRouteType(RouteType.SHORTEST);
        g.setVehicleType(VehicleType.PEDESTRIAN);

        g.computePath(start, dest, startAddr, endAddr);

        assertTrue(g.getLength().equals("10.74km"));
        assertTrue(g.getTime().equals("2h 8min 54sek"));
    }

    @Test
    public void testBicycleRoute() {
        g.setRouteType(RouteType.SHORTEST);
        g.setVehicleType(VehicleType.BICYCLE);

        g.computePath(start, dest, startAddr, endAddr);

        assertTrue(g.getLength().equals("2.94km"));
        assertTrue(g.getTime().equals("9min 47sek"));
    }

    @Test
    public void testUnconnectedComponent() {
        g.setVehicleType(VehicleType.CAR);
        g.setRouteType(RouteType.SHORTEST);

        // Should fail since there's no edge to node4
        g.computePath(start, errNode, startAddr, endAddr);

        assertTrue(g.didError());
    }
}
package model.graph;

import helpers.GetDistance;
import helpers.structures.LongToNodeMap;
import model.MapModel;
import view.MainWindowView;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Graph {
    private LongToNodeMap nodes;
    private VehicleType vehicleType;
    private Path2D shortestPath = new Path2D.Float();
    private PriorityQueue<Node> pq;
    private RouteType routeType = RouteType.FASTEST;
    private Node source;
    private Node dest;

    public Graph() {
        nodes = new LongToNodeMap(25);
        vehicleType = VehicleType.CAR;
    }

    public void computePath(Node source, Node dest) {
        System.out.println(nodes.size());
        this.source = source;
        this.dest = dest;

        // Create priorityQueue that always returns the node with the closest distance to source
        pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) ((a.getDistToSource()) - (b.getDistToSource())));

        // Reset variables
        ArrayList<Node> visitedVerticies = new ArrayList<>();
        shortestPath = null;
        source.setDistToSource(0);
        pq.add(source);

        while (pq.size() != 0) {
            Node current = pq.remove();
            visitedVerticies.add(current);

            // No need to continue searching for dest if already found.
            if (current.getId() == dest.getId()) {
                break;
            }

            // Go through all neighbours and relax them if possible
            for (int i = 0; i < current.getEdges().size(); i++) {
                relaxNeighbour(current, current.getEdges().get(i));
            }
        }

        Path2D sp = new Path2D.Float();
        Node node = dest;
        sp.moveTo(node.getLon(), node.getLat());

        while(node.getParent() != null) {
            node = node.getParent();
            sp.lineTo(node.getLon(), node.getLat());
        }
        shortestPath = sp;

        // Reset all visited vertexes once the path has been computed (no need to reset vertices that never have been
        // visited).
        resetVerticies(visitedVerticies);
    }

    /**
     * Internal helper that relaxes the neighbour of the current node if a shorter path can be found.
     */
    private void relaxNeighbour(Node current, Edge edgeToNeighbour) {
        // Ensure edge supports current vehicle type
        if (!edgeToNeighbour.supportsType(vehicleType)) {
            return;
        }

        Node neighbour = nodes.get(edgeToNeighbour.getTo(current).getId());

        if (neighbour.getDistToSource() > current.getDistToSource() + edgeToNeighbour.getWeight(vehicleType, routeType)) {
            // We found a shorter path for the neighbour! Relax neighbour node.
            neighbour.setDistToSource(current.getDistToSource() + edgeToNeighbour.getLength());
            neighbour.setParent(current);
            pq.add(neighbour);
        }
    }

    /** Internal helper that resets all visited vertexes of the map */
    private void resetVerticies(ArrayList<Node> vertexes) {
        for (Node node : vertexes) {
            node.setDistToSource(Float.POSITIVE_INFINITY);
            node.setParent(null);
        }
    }

    public void setSourceAndDest(Node s, Node d) {
        source = s;
        dest = d;
    }

    public void recalculatePath() {
        if (source != null && dest != null) {
            computePath(source, dest);
        }
    }

    public Path2D getRoutePath() {
        return shortestPath;
    }

    /** Getters and setters */
    public void putNode(Node node) {
        nodes.put(node.getId(), node.getLon(), node.getLat());
    }

    public Node getNode(long id) {
        return nodes.get(id);
    }

    public void setVehicleType(VehicleType type) {
        this.vehicleType = type;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }
}

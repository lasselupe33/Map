package model.graph;

import helpers.GetDistance;
import helpers.io.DeserializeObject;
import helpers.io.SerializeObject;
import helpers.structures.KDTree;
import helpers.structures.LongToNodeMap;
import model.Coordinates;
import model.MapModel;
import model.WayType;
import view.MainWindowView;

import java.awt.geom.Path2D;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Graph {
    private HashMap<Long, Node> nodes;
    private HashMap<Integer, Edge> edges;
    private int currEdgeId = 0;
    private VehicleType vehicleType;
    private Path2D routePath;
    private String length;
    private String time;
    private PriorityQueue<Node> pq;
    private RouteType routeType = RouteType.FASTEST;
    private Node source;
    private Node dest;
    private ArrayList<Edge> routeNodes;
    private boolean failed = false;

    public Graph() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        vehicleType = VehicleType.CAR;
        routeNodes = new ArrayList<>();
    }

    public int size() {
        return nodes.size();
    }

    public void computePath(Node source, Node dest) {
        // Performance logs
        long start = System.currentTimeMillis();

        // Reset nodes
        for (Node node : nodes.values()) {
            node.reset(dest);
        }

        this.source = source;
        this.dest = dest;

        // Create priorityQueue that always returns the node with the closest distance to source
        pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) (((a.getDistToSource(routeType)) + a.getEstimateToDest(routeType, vehicleType)) - ((b.getDistToSource(routeType)) + b.getEstimateToDest(routeType, vehicleType))));

        // Reset variables
        ArrayList<Node> visitedVerticies = new ArrayList<>();
        routePath = null;
        source.setLengthToSource(0);
        source.setTimeToSource(0);
        source.setEstimateToDest((float) GetDistance.inMM(source.getLat(), source.getLon(), dest.getLat(), dest.getLon()));
        pq.add(source);

        while (pq.size() != 0) {
            Node current = pq.remove();
            visitedVerticies.add(current);

            // No need to continue searching for dest if already found.
            if (current.getId() == dest.getId()) {
                break;
            }

            // Go through all neighbours and relax them if possible
            for (int i = 0; i < current.getEdges().length; i++) {
                relaxNeighbour(current, current.getEdges()[i]);
            }
        }

        if (dest.getParentEdge() != null) {
            setRouteLength(dest);
            setRouteTime(dest);
            createComputedPath(dest);
            failed = false;
        } else {
            failed = true;
        }


        System.out.println("Distance: " + GetDistance.inKM(source.getLat(), source.getLon(), dest.getLat(), dest.getLon()));
        System.out.println("Visited verticies: " + visitedVerticies.size());
        System.out.println("Time in ms: " + (System.currentTimeMillis() - start));
    }

    /**
     * Internal helper that relaxes the neighbour of the current node if a shorter path can be found.
     */
    private void relaxNeighbour(Node current, Integer edgeToNeighbourId) {
        Edge edgeToNeighbour = edges.get(edgeToNeighbourId);

        // Ensure edge supports current vehicle type
        if (!edgeToNeighbour.supportsType(vehicleType)) {
            return;
        }

        Node neighbour = nodes.get(edgeToNeighbour.getTo(current));

        if (neighbour.getDistToSource(routeType) > current.getDistToSource(routeType) + edgeToNeighbour.getWeight(vehicleType, routeType)) {
            // We found a shorter path for the neighbour! Relax neighbour node.
            neighbour.setLengthToSource(current.getLengthToSource() + edgeToNeighbour.getLength());
            neighbour.setTimeToSource(current.getTimeToSource() + edgeToNeighbour.getTime(vehicleType));
            neighbour.setParentEdge(edgeToNeighbourId);
            pq.add(neighbour);
        }
    }

    /** Internal helper that sets the route length in KM based on the computed path destination */
    private void setRouteLength(Node dest) {
        // Get length in KM's
        float length = dest.getLengthToSource() / 1000000;

        // Nicely format distance
        DecimalFormat formatter = new DecimalFormat("0.0");
        this.length = formatter.format(length);
    }

    private void setRouteTime(Node dest) {
        // Get time in hours
        float time = dest.getTimeToSource() / 1000000 / 60;

        // We always round the time up
        int hours = (int) time;
        int min = (int) ((time % 1) * 60);
        int seconds = (int) ((((time % 1) * 60) % 1) * 60);

        this.time = (hours != 0 ? hours + "h " : "") + (min != 0 || hours != 0 ? min + "min " : "") + (seconds != 0 || min != 0 || hours != 0 ? seconds + "sek" : "");
    }

    /** Internal helper that creates the path of the found path between two nodes */
    private void createComputedPath(Node dest) {
        routeNodes = new ArrayList<>();
        Node node = dest;

        // Prepare drawing route path
        Path2D routePath = new Path2D.Float();
        Edge parentEdge = edges.get(node.getParentEdge());
        routePath.moveTo(node.getLon(), node.getLat());

        while(parentEdge != null) {
            // Determine if we should start at the end of the edge path or the start...
            if (parentEdge.getPath()[0].getX() == node.getLon() && parentEdge.getPath()[0].getY() == node.getLat()) {
                // If the previous point matches with the start of the path, then go through the path forwards
                for (int i = 1; i < parentEdge.getPath().length; i++) {
                    Coordinates point = parentEdge.getPath()[i];
                    routePath.lineTo(point.getX(), point.getY());
                }
            } else {
                // ... else go through the path backwards
                for (int i = parentEdge.getPath().length - 2; i >= 0; i--) {
                    Coordinates point = parentEdge.getPath()[i];
                    routePath.lineTo(point.getX(), point.getY());
                }
            }

            // Go to next matched path
            node = nodes.get(parentEdge.getTo(node));
            routeNodes.add(parentEdge);
            parentEdge = edges.get(node.getParentEdge());
        }

        this.routePath = routePath;
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
        return routePath;
    }

    public void resetRoute() {
        routePath = null;
        time = null;
        length = null;
        routeNodes = new ArrayList<>();
    }

    /** Helper that converts the ArrayList of edges in a node to an array once all edges have been created */
    public void finalizeNodes() {
        for (Node node : nodes.values()) {
            node.finalizeEdges();
        }
    }

    /** Getters and setters */
    public void putNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public int putEdge(Edge edge) {
        edges.put(currEdgeId, edge);
        return currEdgeId++;
    }

    public Node getNode(long id) {
        return nodes.get(id);
    }

    public Edge getEdge(int id) { return edges.get(id);}

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

    public String getLength() { return length; }

    public String getTime() { return time; }

    public Node getSource() {
        return source;
    }


    public ArrayList<Edge> getRouteNodes() {
        return routeNodes;
    }

    public boolean didError() {
        return failed;
    }

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        new SerializeObject("graph/nodes", nodes);
    }

    /** Internal helper that deserializses the MapModel */
    public void deserialize() {
        try {
            // Setup thread callback
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = HashMap.class;
            parameterTypes[1] = String.class;
            Method callback = Graph.class.getMethod("onThreadDeserializeComplete", parameterTypes);

            new DeserializeObject("graph/nodes", this, callback);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Callback to be called once a thread has finished deserializing a mapType */
    public void onThreadDeserializeComplete(HashMap<Long, Node> nodes, String name) {
        this.nodes = nodes;
    }
}

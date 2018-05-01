package model.graph;

import helpers.GetDistance;
import helpers.io.DeserializeObject;
import helpers.io.SerializeObject;
import helpers.structures.KDTree;
import helpers.structures.LongToNodeMap;
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
    private VehicleType vehicleType;
    private Path2D routePath;
    private String length;
    private String time;
    private PriorityQueue<Node> pq;
    private RouteType routeType = RouteType.FASTEST;
    private Node source;
    private Node dest;

    public Graph() {
        nodes = new HashMap<>();
        vehicleType = VehicleType.CAR;
    }

    public void computePath(Node source, Node dest) {
        this.source = source;
        this.dest = dest;

        // Create priorityQueue that always returns the node with the closest distance to source
        pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) ((a.getDistToSource(routeType)) - (b.getDistToSource(routeType))));

        // Reset variables
        ArrayList<Node> visitedVerticies = new ArrayList<>();
        routePath = null;
        source.setLengthToSource(0);
        source.setTimeToSource(0);
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

        setRouteLength(dest);
        setRouteTime(dest);
        createComputedPath(dest);

        System.out.println(GetDistance.inKM(source.getLat(), source.getLon(), dest.getLat(), dest.getLon()));

        // Reset all visited vertexes once the path has been computed (no need to reset vertices that never have been
        // visited).
        resetVerticies(visitedVerticies);
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
        // Get time in minutes
        float time = dest.getTimeToSource() / 1000000;

        // We always round the time up
        this.time = (int) time + "min " + (int) ((time % 1) * 60) + "sek";
    }

    /** Internal helper that creates the path of the found path between two nodes */
    private void createComputedPath(Node dest) {
        Node node = dest;

        // Prepare drawing route path
        Path2D sp = new Path2D.Float();
        sp.moveTo(node.getLon(), node.getLat());

        while(node.getParent() != null) {
            node = node.getParent();
            sp.lineTo(node.getLon(), node.getLat());
        }

        routePath = sp;
    }

    /**
     * Internal helper that relaxes the neighbour of the current node if a shorter path can be found.
     */
    private void relaxNeighbour(Node current, Edge edgeToNeighbour) {
        // Ensure edge supports current vehicle type
        if (!edgeToNeighbour.supportsType(vehicleType)) {
            return;
        }

        Node neighbour = nodes.get(edgeToNeighbour.getTo(current));

        if (neighbour.getDistToSource(routeType) > current.getDistToSource(routeType) + edgeToNeighbour.getWeight(vehicleType, routeType)) {
            // We found a shorter path for the neighbour! Relax neighbour node.
            neighbour.setLengthToSource(current.getLengthToSource() + edgeToNeighbour.getLength());
            neighbour.setTimeToSource(current.getTimeToSource() + edgeToNeighbour.getTime(vehicleType));
            neighbour.setParent(current);
            pq.add(neighbour);
        }
    }

    /** Internal helper that resets all visited vertexes of the map */
    private void resetVerticies(ArrayList<Node> vertexes) {
        for (Node node : vertexes) {
            node.reset();
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
        return routePath;
    }

    public void resetRoute() {
        routePath = null;
        time = null;
        length = null;
    };

    /** Internal helper that converts the ArrayList of edges in a node to an array once all edges have been created */
    public void finalizeNodes() {
        for (Node node : nodes.values()) {
            node.finalizeEdges();
        }
    }

    /** Getters and setters */
    public void putNode(Node node) {
        nodes.put(node.getId(), node);
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

    public String getLength() { return length; }

    public String getTime() { return time; }

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

package model.graph;

import helpers.UnitConverter;
import helpers.VectorMath;
import helpers.io.DeserializeObject;
import helpers.io.SerializeObject;
import model.Address;
import model.Coordinates;

import java.awt.geom.Path2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Model responsible for handling the graph used for navigation between two points
 */
public class Graph {
    // Contain the vertices and edges of the graph
    private HashMap<Long, Node> nodes;
    private HashMap<Integer, Edge> edges;

    // Used while parsing the graph
    private int currEdgeId = 0;

    // Used while computing path
    private PriorityQueue<Node> pq;

    // Contains the current settings of the navigation
    private VehicleType vehicleType;
    private RouteType routeType = RouteType.FASTEST;

    // Fields containing the info gathered after computing a path
    private ArrayList<Edge> routeEdges;
    private Path2D routePath;
    private String length;
    private String time;
    private Address sourceAddress;
    private Address destAddress;
    private Node source;
    private Node dest;
    private ArrayList<TextualElement> textualNavigation;
    private boolean failed = false;

    public Graph() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        vehicleType = VehicleType.CAR;
        routeEdges = new ArrayList<>();
    }

    public void reset() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    public int size() {
        return nodes.size();
    }

    /**
     * Helper that computes a path between two nodes/vertices using the A* search algorithm.
     *
     * This method takes the current settings of the navigation into account, e.g. if we want the fastest route or the
     * shortest, on bike or in car.
     */
    public void computePath(Node source, Node dest, Address sourceAddress, Address destAddress) {
        // We don't want to compute a path between two identical nodes, since there is no path to compute...
        if (source.getId() == dest.getId()) {
            failed = false;
            return;
        }

        this.sourceAddress = sourceAddress;
        this.destAddress = destAddress;
        this.source = source;
        this.dest = dest;

        // Performance logs
        long start = System.currentTimeMillis();

        // Reset nodes
        for (Node node : nodes.values()) {
            node.reset(dest);
        }

        // Create priorityQueue that always returns the node with the closest distance to source
        pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) (((a.getDistToSource(routeType)) + a.getEstimateToDest(routeType, vehicleType)) - ((b.getDistToSource(routeType)) + b.getEstimateToDest(routeType, vehicleType))));

        // Reset variables
        ArrayList<Node> visitedVerticies = new ArrayList<>();
        routePath = null;
        source.setLengthToSource(0);
        source.setTimeToSource(0);
        source.setEstimateToDest((float) UnitConverter.DistInMM(source.getLat(), source.getLon(), dest.getLat(), dest.getLon()));
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


        System.out.println("Distance: " + UnitConverter.DistInKM(source.getLat(), source.getLon(), dest.getLat(), dest.getLon()));
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
        this.length = UnitConverter.formatDistance(dest.getLengthToSource());
    }

    private void setRouteTime(Node dest) {
        // Get time in hours
        float time = dest.getTimeToSource() / 1000000 / 60;

        // Create formatted time string and use as computed time
        this.time = UnitConverter.formatTime(time);
    }

    /** Internal helper that creates the path of the found path between two nodes */
    private void createComputedPath(Node dest) {
        routeEdges = new ArrayList<>();
        Node node = dest;

        // Prepare drawing route path
        Path2D routePath = new Path2D.Float();
        Edge parentEdge = edges.get(node.getParentEdge());
        routePath.moveTo(node.getLon(), node.getLat());

        while(parentEdge != null) {
            if (parentEdge.getPath().length != 0) {
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
            }

            // Go to next matched path
            node = nodes.get(parentEdge.getTo(node));
            routeEdges.add(parentEdge);
            parentEdge = edges.get(node.getParentEdge());
        }

        this.routePath = routePath;

        // Now that the path has been computed, then compute textual navigation
        computeTextualNavigation();
    }

    /**
     * Use edges in route to create the textual navigation
     */
    private void computeTextualNavigation() {
        // Reset textualNav
        textualNavigation = new ArrayList<>();

        ArrayList<Edge> edges = routeEdges;

        // Reverse the edges of the route (we want to create the description from start to end)
        Collections.reverse(edges);

        // Add the start Address to the navigation text
        textualNavigation.add(new TextualElement(sourceAddress));

        // Create the start of the navigation, leading the user in the right direction of the first road
        Node fromNode = source;
        addNavigationStart(fromNode, routeEdges.get(0));

        // Prepare vector variables
        float x;
        float y;
        float[] vector1 = new float[2];
        float[] vector2 = new float[2];

        // Step-by-step navigation is only relevant if we have more than two edges.
        if (routeEdges.size() >= 2) {
            float length = 0;

            // Go through all edges and print navigation every time we reach an edge
            for (int i = 0; i < routeEdges.size() - 1; i++) {
                // Get the toNode of the current Edge
                Edge firstEdge = routeEdges.get(i);
                Node toNode = getNode(firstEdge.getTo(fromNode));

                // Create a vector for the current edge
                x = toNode.getLon() - fromNode.getLon();
                y = toNode.getLat() - fromNode.getLat();
                vector1[0] = x;
                vector1[1] = y;

                // Get the nodes of the second and and create a vector for this one.
                fromNode = toNode;
                Edge secondEdge = routeEdges.get(i+1);
                toNode = getNode(secondEdge.getTo(fromNode));

                x = toNode.getLon() - fromNode.getLon();
                y = toNode.getLat() - fromNode.getLat();
                vector2[0] = x;
                vector2[1] = y;

                // Get the angle between the two vectors and prepare adding a step to the navigation
                double angle = VectorMath.angle(vector1, vector2);
                length += routeEdges.get(i+1).getLength();
                String firstEdgeName = firstEdge.getName() == null ? "" : " ned ad " + firstEdge.getName();
                String secondEdgeName = secondEdge.getName() == null ? "" : " ned ad " + secondEdge.getName();

                if(angle < 135 && angle >= 45){
                    textualNavigation.add(new TextualElement("Drej til højre" + secondEdgeName, "/icons/arrow-right.png", UnitConverter.formatDistance(length)));
                    length = 0;
                } else if(angle < 45 && angle >= -45){
                    // If we're continuing straightforward, then we only want to add a step if the road name has changed!
                    if (!firstEdgeName.equals(secondEdgeName)) {
                        textualNavigation.add(new TextualElement("Fortsæt" + secondEdgeName, "/icons/arrow-up.png", UnitConverter.formatDistance(length)));
                        length = 0;
                    }
                } else if(angle < -45 && angle >= -135){
                    textualNavigation.add(new TextualElement("Drej til venstre" + secondEdgeName, "/icons/arrow-left.png", UnitConverter.formatDistance(length)));
                    length = 0;
                }

            }
        }
        textualNavigation.add(new TextualElement("Destinationen er nået", "/icons/locationIcon.png", null));

        // Add the destination Address to the navigation text
        textualNavigation.add(new TextualElement(destAddress));
    }

    /**
     * Calculate start direction and add text to navigation
     * @param from first node in route
     * @param edge the edge connecting the first node and the second node of the navigation
     */
    private void addNavigationStart(Node from, Edge edge) {
        float startPointX = from.getLon();
        float startPointY = from.getLat();

        Node to = getNode(edge.getTo(from));

        float endPointX = to.getLon();
        float endPointY = to.getLat();

        double compassReading = Math.toDegrees(Math.atan2(endPointX-startPointX, endPointY-startPointY));

        String[] directions = new String[] {"syd", "sydøst", "øst", "nordøst", "syd", "nordvest", "vest", "sydvest", "syd"};
        int directionIndex = (int) Math.round(compassReading / 45); // divide 360 degrees by the 8 directions = 45
        if (directionIndex < 0) {
            directionIndex = directionIndex + 8;
        }

        String length = UnitConverter.formatDistance(UnitConverter.DistInMM(startPointY, startPointX, endPointY, endPointX));
        textualNavigation.add(new TextualElement("Tag mod " + directions[directionIndex] + (edge.getName() != null ? " ad " + edge.getName() : ""), "/icons/arrow-up.png", length));
    }

    public void setSourceAndDest(Node s, Node d) {
        source = s;
        dest = d;
    }

    public Path2D getRoutePath() {
        return routePath;
    }

    public void resetRoute() {
        routePath = null;
        time = null;
        length = null;
        routeEdges = new ArrayList<>();
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

    public ArrayList<TextualElement> getTextualNavigation() {
        return textualNavigation;
    }

    public boolean didError() {
        return failed;
    }

    /** Serializes all data necessary to load and display the map */
    public void serialize() {
        new SerializeObject("graph/nodes", nodes);
        new SerializeObject("graph/edges", edges);
    }

    /** Internal helper that deserializses the Graph */
    public void deserialize() {
        try {
            // Setup thread callback
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Object.class;
            parameterTypes[1] = String.class;
            Method callback = Graph.class.getMethod("onThreadDeserializeComplete", parameterTypes);

            new DeserializeObject("graph/nodes", this, callback);
            new DeserializeObject("graph/edges", this, callback);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Callback to be called once a thread has finished deserializing a graph dependency */
    public void onThreadDeserializeComplete(Object deserializedObject, String name) {
        switch (name) {
            case "graph/nodes":
                this.nodes = (HashMap<Long, Node>) deserializedObject;
                break;

            case "graph/edges":
                this.edges = (HashMap<Integer, Edge>) deserializedObject;

        }
    }
}

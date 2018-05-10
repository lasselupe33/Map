package parsing;

import helpers.UnitConverter;
import helpers.structures.LongToNodeMap;
import helpers.structures.SimpleLongSet;
import model.*;
import model.graph.Edge;
import model.graph.Graph;
import model.graph.Node;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import view.LoadingScreen;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * Handle OSM data
 */
public class OSMHandler extends DefaultHandler {
    private LongToNodeMap idToNode = new LongToNodeMap(25);
    private Map<Long, OSMWay> idToWay = new HashMap<>();
    private HashMap<OSMNode, OSMWay> coastlines = new HashMap<>();
    private double lonFactor;
    private HashMap<String, String> postcodeToCity = new HashMap<>();

    // Models
    private OSMWay way;
    private MetaModel model;
    private MapModel mapModel;
    private AddressesModel addressesModel;
    private WayType type;
    private OSMRelation relation;

    // Fields for graph
    private Graph graph;
    private boolean isHighway = false;

    // Fields for parsing information on highways
    private int speedLimit;
    private boolean supportsCars;
    private boolean supportsBicycles;
    private boolean supportsPedestrians;

    // Views
    LoadingScreen loadingScreen;

    // Fields related to created addresses
    private Address currentAddress;
    private String street;
    private String house_no;
    private String postcode;
    private String city;

    // Fields directly related to ways
    private String name;

    // Loading related fields
    private boolean reachedAddress = false;
    private boolean reachedWays = false;
    private boolean reachedRelations = false;

    public OSMHandler(MetaModel m, MapModel mm, AddressesModel am, LoadingScreen ls, Graph g) {
        model = m;
        mapModel = mm;
        addressesModel = am;
        loadingScreen = ls;
        graph = g;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "bounds":
                parseCoordinates(attributes);
                break;
            case "node":
                parseNode(attributes);
                break;
            case "way":
                initializeWay(Long.parseLong(attributes.getValue("id")));
                break;
            case "relation":
                relation = new OSMRelation();
                type = null;
                break;
            case "member":
                OSMWay w = idToWay.get(Long.parseLong(attributes.getValue("ref")));
                if (w != null) {
                    relation.add(w);
                }
                break;
            case "tag":
                parseTag(attributes.getValue("k"), attributes.getValue("v"));
                break;
            case "nd":
                way.add(idToNode.get(Long.parseLong(attributes.getValue("ref"))));
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "node":
                createAddress();
                break;
            case "way":
                if (type == WayType.COASTLINE) {
                    handleCoastline();
                } else {
                    createWay(way);
                }
                break;
            case "relation":
                createRelation();
                break;
            case "osm":
                loadingScreen.updateProgress(81.881);
                convertCoastlinesToPath();
                loadingScreen.updateProgress(85.963);
                mapModel.createTrees();
                loadingScreen.updateProgress(91.345);
                addressesModel.createTree();
                addressesModel.setPostcodeToCity(postcodeToCity);
                loadingScreen.updateProgress(95.341);
                buildGraph();
                addHighwayNodeRefs();
                graph.finalizeNodes();
                //graph.flatten();
            default:
                break;
        }
    }

    /** Helper to be called when the parser reaches the coordinates of the given OSM-file */
    private void parseCoordinates(Attributes attributes) {
        // Get the coordinates from the file
        double minLat = Double.parseDouble(attributes.getValue("minlat"));
        double minLon = Double.parseDouble(attributes.getValue("minlon"));
        double maxLat = Double.parseDouble(attributes.getValue("maxlat"));
        double maxLon = Double.parseDouble(attributes.getValue("maxlon"));

        // Compute lonFactor to ensure map won't be shrunk on the x-axis.
        // NB: This is necessary due to the world being round, and this approach would be incorrect if parsing the whole
        // world, but it will work since only relatively small areas (Denmark as the largest) will be parsed.
        // This however also results in incorrect distances.
        double avgLat = minLat + (maxLat - minLat) / 2;
        lonFactor = Math.cos(avgLat / 180 * Math.PI);
        minLon *= lonFactor;
        model.setMinLon(minLon);
        maxLon *= lonFactor;
        model.setMaxLon(maxLon);

        // Ensure map won't be upside down and set coordinates
        maxLat = -maxLat;
        model.setMaxLat(maxLat);
        minLat = -minLat;
        model.setMinLat(minLat);
    }

    /** A helper that parses a single tag based on the key and value */
    private void parseTag(String key, String value) {
        switch (key) {
            case "name":
                name = value;
                break;
            case "maxspeed":
                try {
                    speedLimit = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    // Do nothing, simply continue with unset speedLimit
                }
                break;
            case "bicycle":
                supportsBicycles = value.equals("yes");
                break;
            case "motor_vehicle":
                supportsCars = value.equals("yes");
                break;
            case "foot":
                supportsPedestrians = value.equals("yes");
                break;
            case "highway":
                isHighway = true;
                parseHighway(value);
                break;
            case "natural":
                if (value.equals("water")) {
                    type = WayType.WATER;
                }
                if (value.equals("coastline")) {
                    type = WayType.COASTLINE;
                }
                if (value.equals("wood")) {
                    type = WayType.FORREST;
                }
                break;
            case "route":
                if (value.equals("ferry")) {
                    type = WayType.FERRY;
                }
                break;
            case "building":
                type = WayType.BUILDING;
                if (value.equals("church")) {
                    type = WayType.PLACE_OF_WORSHIP;
                }
                break;
            case "leisure":
                if (value.equals("park")) {
                    type = WayType.PARK;
                }
                if (value.equals("pitch")) {
                    type = WayType.PITCH;
                }
                if (value.equals("garden")) {
                    type = WayType.PARK;
                }
                if (value.equals("playground")) {
                    type = WayType.PLAYGROUND;
                }
                if (value.equals("swimming_pool")) {
                    type = WayType.SWIMMINGPOOL;
                }
                break;
            case "landuse":
                if (value.equals("forest")) {
                    type = WayType.FORREST;
                }
                if (value.equals("residential")) {
                    type = WayType.RESIDENTIAL;
                }
                if (value.equals("farmland")) {
                    type = WayType.FARMLAND;
                }
                if (value.equals("allotments")) {
                    type = WayType.ALLOMENTS;
                }
                if (value.equals("cemetery")) {
                    type = WayType.CEMETERY;
                }
                if (value.equals("grass")) {
                    type = WayType.GRASS;
                }
                if (value.equals("recreation_ground")) {
                    type = WayType.GRASS;
                }
                break;
            case "aeroway":
                if (value.equals("aerodrome")) {
                    type = WayType.AEROWAY;
                }
                if (value.equals("runway")) {
                    type = WayType.RUNWAY;
                }
                break;
            case "place":
                if (value.equals("island")) {
                    type = WayType.PLACE;
                }
                if (value.equals("square")) {
                    type = WayType.SQUARE;
                }
                break;
            case "amenity":
                if (value.equals("place_of_worship")) {
                    type = WayType.PLACE_OF_WORSHIP;
                }
                break;
            case "barrier":
                type = WayType.BARRIER;
                if (value.equals("hedge")) {
                    type = WayType.HEDGE;
                }
                break;
            case "waterway":
                if (value.equals("drain")) {
                    type = WayType.DRAIN;
                }
                break;
            case "man_made":
                if (value.equals("bridge")) {
                    type = WayType.MANMADEBRIDGE;
                } else if (value.equals("pier")) {
                    type = WayType.PIER;
                }
                break;
            case "bridge":
                if (value.equals("yes")) {
                    type = WayType.HIGHWAYBRIDGE;
                }
                break;
            case "addr:street":
                street = value;
                break;

            case "addr:housenumber":
                house_no = value;
                break;

            case "addr:postcode":
                postcode = value;
                break;

            case "addr:city":
                city = value;
            default:
                break;
        }
    }

    /** Internal helper that determines the type of way based on the highway tag */
    private void parseHighway(String tag) {
        switch (tag) {
            case "motorway":
            case "motorway_link":
            case "motorway_junction":
                type = WayType.MOTORWAY;
                speedLimit = 130;
                supportsCars = true;
                supportsBicycles = false;
                supportsPedestrians = false;
                break;
            case "trunk":
            case "trunk_link":
                type = WayType.TRUNK;
                speedLimit = 80;
                supportsCars = true;
                supportsBicycles = false;
                supportsPedestrians = false;
                break;
            case "primary":
            case "primary_link":
                type = WayType.HIGHWAY;
                speedLimit = 80;
                supportsCars = true;
                supportsBicycles = true;
                supportsPedestrians = true;
                break;
            case "secondary":
            case "secondary_link":
                type = WayType.SECONDARYROAD;
                speedLimit = 80;
                supportsCars = true;
                supportsBicycles = true;
                supportsPedestrians = true;
                break;
            case "tertiary":
            case "tertiary_link":
                type = WayType.TERTIARYROAD;
                speedLimit = 80;
                supportsCars = true;
                supportsBicycles = true;
                supportsPedestrians = true;
                break;
            case "residential":
            case "unclassified":
            case "living_street":
            case "road":
            case "turning_circle":
                type = WayType.ROAD;
                speedLimit = 50;
                supportsCars = true;
                supportsBicycles = true;
                supportsPedestrians = true;
                break;
            case "pedestrian":
                type = WayType.PEDESTRIAN;
                speedLimit = 5;
                supportsCars = false;
                supportsBicycles = false;
                supportsPedestrians = true;
                break;
            case "service":
                type = WayType.SERVICE;
                speedLimit = 50;
                supportsCars = true;
                supportsBicycles = true;
                supportsPedestrians = true;
                break;
            case "path":
                type = WayType.PATH;
                speedLimit = 5;
                supportsCars = false;
                supportsBicycles = true;
                supportsPedestrians = true;
                break;
            case "footway":
            case "steps":
            case "crossing":
                type = WayType.FOOTWAY;
                speedLimit = 5;
                supportsCars = false;
                supportsBicycles = false;
                supportsPedestrians = true;
                break;
            case "cycleway":
                type = WayType.CYCLEWAY;
                speedLimit = 18;
                supportsCars = false;
                supportsBicycles = true;
                supportsPedestrians = false;
                break;
            default:
                break;
        }
    }

    /** Internal helper that parses a node */
    private void parseNode(Attributes attributes) {
        // Get the lon and lat of the node
        float lon = (float) Double.parseDouble(attributes.getValue("lon"));
        float lat = (float) Double.parseDouble(attributes.getValue("lat"));
        long id = Long.parseLong(attributes.getValue("id"));

        // Convert to proper coordinates
        lon = (float) lonFactor * lon;
        lat = -lat;

        // Add address to node
        idToNode.put(new OSMNode(id, lon, lat));

        // Create temp address to be used when parsing address fields
        currentAddress = new Address(id, lon, lat);
    }

    /** Internal helper that creates an address */
    private void createAddress() {
        if (!reachedAddress) {
            loadingScreen.updateProgress(7.254);
            reachedAddress = true;
        }

        // Create address of node if possible, else this is a way-node, hence add it to the way-map
        if (street == null) {

            // Bail out if street doesn't exist.
            house_no = postcode = city = null;
            currentAddress = null;

            if (street == null) {
                return;
            }
        }

        // Create address
        currentAddress.setAddress(street, house_no, postcode);

        // Add city to map
        if (city != null) {
            postcodeToCity.put(postcode, city);
        }

        // Add address to data-model
        addressesModel.add(currentAddress);

        // Reset fields
        street = house_no = postcode = city = null;
    }

    /** Internal helper that initializes a way then a way-tag is reached */
    private void initializeWay(long id) {
        way = new OSMWay(id);
        type = null;
        speedLimit = 50;
        idToWay.put(id, way);
    }

    /** Internal helper that creates a way when called (i.e. when the parser reaches the end of a way */
    private void createWay(OSMWay way) {
        if (!reachedWays) {
            loadingScreen.updateProgress(49.278);
            reachedWays = true;
        }

        if (isHighway) {
            way.addWayInfo(name, speedLimit, supportsCars, supportsBicycles, supportsPedestrians);
            OSMNode from = way.getNodes().get(0);
            name = null;

            for (int i = 1; i < way.getNodes().size(); i++) {
                OSMNode to = way.getNodes().get(i);

                from.addRef(to.getId());
                from.addWayId(way.getId());
                to.addRef(from.getId());
                to.addWayId(way.getId());

                from = to;
            }
        }

        Path2D path = convertWayToPath(new Path2D.Float(), way);
        addElement(type, path, way.getNodes());

        // Reset field
        isHighway = false;
    }

    /** Internal helper that creates a relation when called (i.e. when the parser reaches the end of a relation */
    private void createRelation() {
        if (!reachedRelations) {
            loadingScreen.updateProgress(76.215);
            reachedRelations = true;
        }

        Path2D path = new Path2D.Float();

        for (OSMWay way : relation) {
            path = convertWayToPath(path, way);
        }
        addElement(type, path, way.getNodes());
    }

    /** Internal helper that converts a way into a path */
    private Path2D convertWayToPath(Path2D path, OSMWay way) {
        OSMNode node = way.get(0);
        path.moveTo(node.getLon(), node.getLat());

        for (int i = 1; i < way.size(); i++) {
            node = way.get(i);
            path.lineTo(node.getLon(), node.getLat());
        }

        return path;
    }

    /** Internal helper that handles parsing of a coastline (by stiching individual coastline-ways together */
    private void handleCoastline() {
        // stitch coastlines together
        // search for coastlines that can be merged with current way
        OSMWay before = coastlines.remove(way.from());
        OSMWay after = coastlines.remove(way.to());
        OSMWay merged = new OSMWay();

        // add these three paths together
        if (before != null) {
            merged.addAll(before.subList(0, before.size() - 1));
        }

        merged.addAll(way);

        if (after != null && after != before) {
            merged.addAll(after.subList(1, after.size()));
        }

        coastlines.put(merged.to(), merged);
        coastlines.put(merged.from(), merged);
    }

    /** Internal helper to be called once all coastlines have been parsed in order to convert them into paths */
    private void convertCoastlinesToPath() {
        Path2D path;
        OSMNode node;

        // convert all coastlines found to paths
        for (Map.Entry<OSMNode, OSMWay> coastline : coastlines.entrySet()) {
            OSMWay way = coastline.getValue();
            if (coastline.getKey() == way.from()) {
                path = new Path2D.Float();
                path.setWindingRule(Path2D.WIND_EVEN_ODD);
                node = way.get(0);
                path.moveTo(node.getLon(), node.getLat());

                for (int i = 1; i < way.size(); i++) {
                    node = way.get(i);
                    path.lineTo(node.getLon(), node.getLat());
                }

                addElement(WayType.COASTLINE, path, way.getNodes());
            }
        }
    }

    private void addElement(WayType type, Path2D path, ArrayList<OSMNode> nodes) {
        if (type == null) {return;}
        Rectangle2D rect = path.getBounds2D();
        switch (type) {
            case COASTLINE:
            case PLACE:
            case RESIDENTIAL:
            case FORREST:
            case FARMLAND:
            case WATER:
            case PITCH:
            case ALLOMENTS:
            case SQUARE:
            case BUILDING:
            case PARK:
            case PLAYGROUND:
            case CEMETERY:
            case PLACE_OF_WORSHIP:
            case AEROWAY:
            case GRASS:
            case MANMADEBRIDGE:
            case PIER:
            case SWIMMINGPOOL:
                mapModel.add(type, new MapElement((float) rect.getX(), (float) rect.getY(), path, type, true, nodes));
                break;

            case ROAD:
            case MOTORWAY:
            case HIGHWAY:
            case SECONDARYROAD:
            case TERTIARYROAD:
            case PEDESTRIAN:
            case SERVICE:
            case FOOTWAY:
            case PATH:
            case FERRY:
            case CYCLEWAY:
            case BARRIER:
            case HEDGE:
            case DRAIN:
            case RUNWAY:
            case TRUNK:
            case HIGHWAYBRIDGE:
            case RAILWAY:

                mapModel.add(type, new MapElement((float) rect.getX(), (float) rect.getY(), path, type, false, nodes));
                break;
            default:
                break;
        }
    }

    private void addHighwayNodeRefs() {
        for (WayType type : WayType.values()) {
            switch (type) {
                case SERVICE:
                case TRUNK:
                case ROAD:
                case TERTIARYROAD:
                case SECONDARYROAD:
                case HIGHWAY:
                case MOTORWAY:
                case CYCLEWAY:
                case FOOTWAY:
                case PEDESTRIAN:
                case PATH:
                    List<MapElement> list = mapModel.getMapElements(type);

                    for (MapElement element : list) {
                        SimpleLongSet tempIds = new SimpleLongSet();

                        for (Long id : element.getNodeIds()) {
                            if (idToNode.get(id).getRefs().size() != 2) {
                                tempIds.add(id);
                            }
                        }

                        element.updateNodes(tempIds);
                    }

                    break;
            }
        }
    }

    /**
     * Helper that builds a flattened wayGraph with only the required nodes once all node references have build.
     */
    private void buildGraph() {
        ArrayList<Long> nodeIds = idToNode.getIds();
        int counter = 0;

        // Go through all parsed nodes
        for (long nodeId : nodeIds) {
            // Store the node that is being currently worked on.
            OSMNode node = idToNode.get(nodeId);

            // As long as the node is a highway (has more than 0 wayIds), then we want to process and add it to the graph.
            if (node.getWayIds().size() != 0) {
                counter++;

                // If the current node has either 1 or more than 2 nodes, then we need to add it to the graph.
                // (Nodes with exactly to references can be removed since no intersections/endpoints will ever exist
                // there).
                SimpleLongSet refs = node.getRefs();
                if (refs.size() != 2) {
                    // Start from the currently found node
                    OSMNode from = node;

                    // Go through all of its neighbours and create required edges
                    for (Long neighborID : refs) {
                        createEdgeToNeighbor(from, neighborID);
                    }
                }
            }
        }

        System.out.println(graph.size());
        System.out.println(counter);
    }

    /** Internal helper that finds the first needed neighbor node of a node and creates an edge between the two. */
    private void createEdgeToNeighbor(OSMNode from, Long initialNeighborId) {
        // Get initial values
        Long wayId = null;
        OSMNode neighbor = idToNode.get(initialNeighborId);
        long prevNeighborId = neighbor.getId();
        ArrayList<Coordinates> path = new ArrayList<>();
        path.add(new Coordinates(from.getLon(), from.getLat()));
        path.add(new Coordinates(neighbor.getLon(), neighbor.getLat()));

        // Continue until we find a node that cannot be removed.
        while (true) {
            // Go through all the nodes...
            SimpleLongSet neighbourRefs = neighbor.getRefs();

            if (neighbourRefs.size() != 2 || (neighbourRefs.contains(from.getId()) && neighbourRefs.contains(prevNeighborId))) {
                break;
            }

            for (Long id : neighbourRefs.getSet()) {
                // ... and find the next node (i.e. not the previous node)
                if (id != prevNeighborId && id != from.getId()) {

                    // If we haven't stored the wayId yet, do so new.
                    if (wayId == null) {
                        wayId = getMatchingWayId(from, neighbor);
                    }

                    // Set the current neighbor to be the neighbor we just found.
                    prevNeighborId = neighbor.getId();
                    neighbor = idToNode.get(id);
                    path.add(new Coordinates(neighbor.getLon(), neighbor.getLat()));
                    break;
                }
            }
        }

        // If we're parsing two nodes that doesn't have any nodes in between, then the wayId haven't been stored yet..
        // therefore this should be done now.
        if (wayId == null) {
            wayId = getMatchingWayId(from, neighbor);
        }

        // We now have all the information required to create an edge, do so now!
        createEdge(from, neighbor, path, wayId);
    }

    /**
     * This method should be called once a from and to node has been found along with the id to the way they're related
     * to.
     */
    private void createEdge(OSMNode from, OSMNode to, ArrayList<Coordinates> path, Long wayId) {
        // Get the related way
        OSMWay way = idToWay.get(wayId);

        // Get the actual node already added to the graph
        Node convertedFrom = graph.getNode(from.getId());

        // If the OSMNode hasn't been converted and added to the graph, then do so now.
        if (convertedFrom == null) {
            convertedFrom = new Node(from.getId(), from.getLon(), from.getLat());
            graph.putNode(convertedFrom);
        }

        // ... Do the same with the "to"-node
        Node convertedTo = graph.getNode(to.getId());

        if (convertedTo == null) {
            convertedTo = new Node(to.getId(), to.getLon(), to.getLat());
            graph.putNode(convertedTo);
        }

        // Ensure that there doesn't already exist an edge between the two nodes.
        for (Integer edgeId : convertedFrom.getTempEdges()) {
            if (graph.getEdge(edgeId).getTo(convertedFrom) == convertedTo.getId()) {
               return;
            }
        }

        // Create the edge with the information of the nodes and the way connecting them
        Edge edge = new Edge(convertedFrom.getId(), convertedTo.getId(), path, way.getName(), getEdgeLength(path), way.getSpeedLimit(), way.supportsCars(), way.supportsBicycles(), way.supportPedestrians());

        // Add the edge to map and references to nodes
        int edgeId = graph.putEdge(edge);
        convertedFrom.addEdge(edgeId);
        convertedTo.addEdge(edgeId);
    }

    /** Helper that computes the actual length between two required graph nodes (Taking hidden nodes into account) */
    private float getEdgeLength(ArrayList<Coordinates> nodes) {
        float length = 0;
        Coordinates from = nodes.get(0);

        for (int i = 1; i < nodes.size(); i++) {
            Coordinates to = nodes.get(i);
            length += (float) UnitConverter.DistInMM(from.getX(), from.getY(), to.getX(), to.getY());

            from = to;
        }

        return length;
    }

    /**
     * Internal helper that returns the id of the way that two nodes are a part of.
     */
    private Long getMatchingWayId(OSMNode node1, OSMNode node2) {
        for (long fromWayId : node1.getWayIds().getSet()) {
            if (idToNode.get(node2.getId()).getWayIds().contains(fromWayId)) {
                return fromWayId;
            }
        }

        return null;
    }

    /** Internal helper that converts an OSMNode to a Node to be used in the wayGraph */
    /*
    private void addToGraph(OSMWay way) {
        ArrayList<OSMNode> nodes = way.getNodes();
        OSMNode from = nodes.get(0);

        for (int i = 1; i < nodes.size(); i++) {
            OSMNode to = nodes.get(i);

            // Determine length of edge between current node and prev node
            float length = (float) UnitConverter.inMM(from.getLat(), from.getLon(), to.getLat(), to.getLon());

            Node convertedFromNode = graph.getNode(from.getId());

            if (convertedFromNode == null) {
                convertedFromNode = new Node(from.getId(), from.getLon(), from.getLat());
                graph.putNode(convertedFromNode);
            }

            Node convertedToNode = graph.getNode(to.getId());

            if (convertedToNode == null) {
                convertedToNode = new Node(to.getId(), to.getLon(), to.getLat());
                graph.putNode(convertedToNode);
            }

            // Create edge between converted nodes and add the edge to them.
            Edge edge = new Edge(convertedFromNode.getId(), convertedToNode.getId(), length, speedLimit, supportsCars, supportsBicycles, supportsPedestrians);

            graph.getNode(convertedFromNode.getId()).addEdge(edge);
            graph.getNode(convertedToNode.getId()).addEdge(edge);

            // Prepare for next iteration in loop
            from = to;
        }
        isHighway = false;
    }
    */
}

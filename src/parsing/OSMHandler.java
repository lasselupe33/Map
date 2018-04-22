package parsing;

import helpers.structures.LongToOSMNodeMap;
import model.*;
import model.WayType;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import view.LoadingScreen;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class OSMHandler extends DefaultHandler {
    LongToOSMNodeMap idToNode = new LongToOSMNodeMap(25);
    Map<Long, OSMWay> idToWay = new HashMap<>();
    HashMap<OSMNode, OSMWay> coastlines = new HashMap<>();
    private double lonFactor;

    // Models
    OSMWay way;
    MetaModel model;
    MapModel mapModel;
    AddressesModel addressesModel;
    private WayType type;
    private OSMRelation relation;

    // Views
    LoadingScreen loadingScreen;

    // Fields related to created addresses
    private Address currentAddress;
    private String street;
    private String house_no;
    private String postcode;

    // Loading related fields
    private boolean reachedAddress = false;
    private boolean reachedWays = false;
    private boolean reachedRelations = false;

    public OSMHandler(MetaModel m, MapModel mm, AddressesModel am, LoadingScreen ls) {
        model = m;
        mapModel = mm;
        addressesModel = am;
        loadingScreen = ls;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "bounds":
                parseCoordinates(attributes);
                break;
            case "node":
                parseNode(attributes);
                break;
            case "way":
                way = new OSMWay();
                type = WayType.UNKNOWN;
                idToWay.put(Long.parseLong(attributes.getValue("id")), way);
                break;
            case "relation":
                relation = new OSMRelation();
                type = WayType.UNKNOWN;
                break;
            case "member":
                OSMWay w = idToWay.get(Long.parseLong(attributes.getValue("ref")));
                if (w != null) {
                    relation.add(w);
                }
                break;
            case "tag":
                switch (attributes.getValue("k")) {
                    case "highway":
                        type = WayType.ROAD;
                        if (attributes.getValue("v").equals("motorway")) {
                            type = WayType.MOTORWAY;
                        }
                        if (attributes.getValue("v").equals("motorway_link")) {
                            type = WayType.MOTORWAY;
                        }
                        if (attributes.getValue("v").equals("trunk_link")) {
                            type = WayType.MOTORWAY;
                        }
                        if (attributes.getValue("v").equals("primary")) {
                            type = WayType.HIGHWAY;
                        }
                        if (attributes.getValue("v").equals("secondary")) {
                            type = WayType.SECONDARYROAD;
                        }
                        if (attributes.getValue("v").equals("tertiary")) {
                            type = WayType.TERTIARYROAD;
                        }
                        if (attributes.getValue("v").equals("service")) {
                            type = WayType.SERVICE;
                        }
                        if (attributes.getValue("v").equals("path")) {
                            type = WayType.PATH;
                        }
                        if (attributes.getValue("v").equals("footway")) {
                            type = WayType.FOOTWAY;
                        }
                        if (attributes.getValue("v").equals("cycleway")) {
                            type = WayType.CYCLEWAY;
                        }
                        break;
                    case "natural":
                        if (attributes.getValue("v").equals("water")) {
                            type = WayType.WATER;
                        }
                        if (attributes.getValue("v").equals("coastline")) {
                            type = WayType.COASTLINE;
                        }
                        if (attributes.getValue("v").equals("wood")) {
                            type = WayType.FORREST;
                        }
                        break;
                    case "route":
                        if (attributes.getValue("v").equals("ferry")) {
                            type = WayType.FERRY;
                        }
                        break;
                    case "building":
                        type = WayType.BUILDING;
                        if (attributes.getValue("v").equals("church")) {
                            type = WayType.PLACE_OF_WORSHIP;
                        }
                        break;
                    case "leisure":
                        if (attributes.getValue("v").equals("park")) {
                            type = WayType.PARK;
                        }
                        if (attributes.getValue("v").equals("pitch")) {
                            type = WayType.PITCH;
                        }
                        if (attributes.getValue("v").equals("garden")) {
                            type = WayType.PARK;
                        }
                        if (attributes.getValue("v").equals("playground")) {
                            type = WayType.PLAYGROUND;
                        }
                        break;
                    case "landuse":
                        if (attributes.getValue("v").equals("forest")) {
                            type = WayType.FORREST;
                        }
                        if (attributes.getValue("v").equals("residential")) {
                            type = WayType.RESIDENTIAL;
                        }
                        if (attributes.getValue("v").equals("farmland")) {
                            type = WayType.FARMLAND;
                        }
                        if (attributes.getValue("v").equals("allotments")) {
                            type = WayType.ALLOMENTS;
                        }
                        if (attributes.getValue("v").equals("cemetery")) {
                            type = WayType.CEMETERY;
                        }
                        if (attributes.getValue("v").equals("grass")) {
                            type = WayType.GRASS;
                        }
                        break;
                    case "aeroway":
                        if (attributes.getValue("v").equals("aerodrome")) {
                            type = WayType.AEROWAY;
                        }
                        if (attributes.getValue("v").equals("runway")) {
                            type = WayType.RUNWAY;
                        }
                        break;
                    case "place":
                        if (attributes.getValue("v").equals("island")) {
                            type = WayType.PLACE;
                        }
                        if (attributes.getValue("v").equals("square")) {
                            type = WayType.PEDESTRIAN;
                        }
                        break;
                    case "amenity":
                        if (attributes.getValue("v").equals("place_of_worship")) {
                            type = WayType.PLACE_OF_WORSHIP;
                        }
                        break;
                    case "barrier":
                        type = WayType.BARRIER;
                        if (attributes.getValue("v").equals("hedge")) {
                            type = WayType.HEDGE;
                        }
                        break;
                    case "waterway":
                        if (attributes.getValue("v").equals("drain")) {
                            type = WayType.DRAIN;
                        }
                        break;
                    case "addr:street":
                        street = attributes.getValue("v");
                        break;

                    case "addr:housenumber":
                        house_no = attributes.getValue("v");
                        break;

                    case "addr:postcode":
                        postcode = attributes.getValue("v");
                        break;

                    default:
                        break;
                }
                break;
            case "nd":
                way.add(idToNode.get(Long.parseLong(attributes.getValue("ref"))));
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
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
                loadingScreen.updateProgress(84.881);
                convertCoastlinesToPath();

                loadingScreen.updateProgress(92.963);
                mapModel.createTrees();

                loadingScreen.updateProgress(96.345);
                addressesModel.createTree();
            default:
                break;
        }
    }

    /** Internal helper that parses a node */
    private void parseNode(Attributes attributes) {
        // Get the lon and lat of the node
        double lon = Double.parseDouble(attributes.getValue("lon"));
        double lat = Double.parseDouble(attributes.getValue("lat"));
        long id = Long.parseLong(attributes.getValue("id"));

        // Add node to map
        idToNode.put(id, lonFactor * lon, -lat);

        // Create temp address to be used when parsing address fields
        currentAddress = new Address(lonFactor * lon, -lat);
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

    /** Internal helper that creates an address */
    private void createAddress() {
        if (!reachedAddress) {
            loadingScreen.updateProgress(7.254);
            reachedAddress = true;
        }

        // Create address of node if possible
        if (street == null) {
            // Bail out if street doesn't exist.
            house_no = postcode = null;

            return;
        }

        currentAddress.setAddress(street, house_no, postcode);

        // Add address to data-model
        addressesModel.add(currentAddress);

        // Reset fields
        street = house_no = postcode = null;
    }

    /** Internal helper that creates a way when called (i.e. when the parser reaches the end of a way */
    private void createWay(OSMWay way) {
        if (!reachedWays) {
            loadingScreen.updateProgress(49.278);
            reachedWays = true;
        }

        Path2D path = convertWayToPath(new Path2D.Float(), way);
        addElement(type, path);
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
        addElement(type, path);
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

                addElement(WayType.COASTLINE, path);
            }
        }
    }

    private void addElement(WayType type, Path2D path) {
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
            case PEDESTRIAN:
            case BUILDING:
            case PARK:
            case PLAYGROUND:
            case CEMETERY:
            case PLACE_OF_WORSHIP:
            case AEROWAY:
            case GRASS:
                mapModel.add(type, new MapElement(rect.getX(), rect.getY(), path, type,  true));
                break;

            case ROAD:
            case MOTORWAY:
            case HIGHWAY:
            case SECONDARYROAD:
            case TERTIARYROAD:
            case SERVICE:
            case FOOTWAY:
            case PATH:
            case FERRY:
            case SUBWAY:
            case CYCLEWAY:
            case UNKNOWN:
            case BARRIER:
            case HEDGE:
            case DRAIN:
            case RUNWAY:
                mapModel.add(type, new MapElement(rect.getX(), rect.getY(), path, type, false));
                break;
            default:
                break;
        }
    }
}

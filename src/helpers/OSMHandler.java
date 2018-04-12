package helpers;

import model.Address;
import model.MainModel;
import model.MapElements.MapElement;
import model.MapModel;
import model.ZoomLevel;
import model.osm.OSMNode;
import model.osm.OSMRelation;
import model.osm.OSMWay;
import model.osm.OSMWayType;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.geom.Path2D;
import java.util.*;

public class OSMHandler extends DefaultHandler {
    LongToOSMNodeMap idToNode = new LongToOSMNodeMap(25);
    Map<Long, OSMWay> idToWay = new HashMap<>();
    HashMap<OSMNode, OSMWay> coastlines = new HashMap<>();
    OSMWay way;
    MainModel model;
    MapModel mapModel;
    private double lonFactor;
    private ZoomLevel level;
    private OSMWayType type;
    private OSMRelation relation;


    // Fields related to created addresses
    private Address currentAddress;
    private String street;
    private String house_no;
    private String postcode;

    public OSMHandler(MainModel m, MapModel mm) {
        model = m;
        mapModel = mm;
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
                type = OSMWayType.UNKNOWN;
                level = ZoomLevel.SIX;
                idToWay.put(Long.parseLong(attributes.getValue("id")), way);
                break;
            case "relation":
                relation = new OSMRelation();
                type = OSMWayType.UNKNOWN;
                level = ZoomLevel.SIX;
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
                        type = OSMWayType.ROAD;
                        if (attributes.getValue("v").equals("motorway")) {
                            type = OSMWayType.MOTORWAY;
                        }
                        if (attributes.getValue("v").equals("primary")) {
                            type = OSMWayType.HIGHWAY;
                        }
                        if (attributes.getValue("v").equals("secondary")) {
                            type = OSMWayType.SECONDARYROAD;
                        }
                        if (attributes.getValue("v").equals("tertiary")) {
                            type = OSMWayType.TERTIARYROAD;
                        }
                        if (attributes.getValue("v").equals("service")) {
                            type = OSMWayType.SERVICE;
                        }
                        if (attributes.getValue("v").equals("path")) {
                            type = OSMWayType.PATH;
                        }
                        if (attributes.getValue("v").equals("footway")) {
                            type = OSMWayType.FOOTWAY;
                        }
                        if (attributes.getValue("v").equals("cycleway")) {
                            type = OSMWayType.CYCLEWAY;
                        }
                        break;
                    case "natural":
                        if (attributes.getValue("v").equals("water")) {
                            type = OSMWayType.WATER;
                        } else if (attributes.getValue("v").equals("coastline")) {
                            type = OSMWayType.COASTLINE;
                        }
                        break;
                    case "route":
                        if (attributes.getValue("v").equals("ferry")) {
                            type = OSMWayType.FERRY;
                        }
                        break;
                    case "building":
                        type = OSMWayType.BUILDING;
                        if (attributes.getValue("v").equals("church")) {
                            type = OSMWayType.PLACE_OF_WORSHIP;
                        }
                        break;
                    case "leisure":
                        if (attributes.getValue("v").equals("park")) {
                            type = OSMWayType.PARK;
                        }
                        if (attributes.getValue("v").equals("pitch")) {
                            type = OSMWayType.PITCH;
                        }
                        if (attributes.getValue("v").equals("garden")) {
                            type = OSMWayType.PARK;
                        }
                        if (attributes.getValue("v").equals("playground")) {
                            type = OSMWayType.PLAYGROUND;
                        }
                        break;
                    case "landuse":
                        if (attributes.getValue("v").equals("forest")) {
                            type = OSMWayType.FORREST;
                        }
                        if (attributes.getValue("v").equals("residential")) {
                            type = OSMWayType.RESIDENTIAL;
                        }
                        if (attributes.getValue("v").equals("farmland")) {
                            type = OSMWayType.FARMLAND;
                        }
                        if (attributes.getValue("v").equals("allotments")) {
                            type = OSMWayType.ALLOMENTS;
                        }
                        if (attributes.getValue("v").equals("cemetery")) {
                            type = OSMWayType.CEMETERY;
                        }
                        break;
                    case "place":
                        if (attributes.getValue("v").equals("island")) {
                            type = OSMWayType.PLACE;
                        }
                        if (attributes.getValue("v").equals("square")) {
                            type = OSMWayType.PEDESTRIAN;
                        }
                        break;
                    case "amenity":
                        if (attributes.getValue("v").equals("place_of_worship")) {
                            type = OSMWayType.PLACE_OF_WORSHIP;
                        }
                        break;
                    case "barrier":
                        type = OSMWayType.BARRIER;
                        if (attributes.getValue("v").equals("hedge")) {
                            type = OSMWayType.HEDGE;
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
                if (type == OSMWayType.COASTLINE) {
                    handleCoastline();
                } else {
                    createWay(way);
                }
                break;
            case "relation":
                createRelation();
                break;
            case "osm":
                convertCoastlinesToPath();
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
        // Create address of node if possible
        if (street == null) {
            // Bail out if street doesn't exist.
            house_no = postcode = null;

            return;
        }

        currentAddress.setAddress(street, house_no, postcode);

        // Add address to data-model
        model.getAddresses().add(currentAddress);

        // Reset fields
        street = house_no = postcode = null;
    }

    /** Internal helper that creates a way when called (i.e. when the parser reaches the end of a way */
    private void createWay(OSMWay way) {
        Path2D path = convertWayToPath(new Path2D.Double(), way);

        addElement(type, path);
    }

    /** Internal helper that creates a relation when called (i.e. when the parser reaches the end of a relation */
    private void createRelation() {
        Path2D path = new Path2D.Double();

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
                path = new Path2D.Double();
                path.setWindingRule(Path2D.WIND_EVEN_ODD);
                node = way.get(0);
                path.moveTo(node.getLon(), node.getLat());

                for (int i = 1; i < way.size(); i++) {
                    node = way.get(i);
                    path.lineTo(node.getLon(), node.getLat());
                }

                addElement(OSMWayType.COASTLINE, path);
            }

        }
    }



    private void addElement(OSMWayType type, Path2D path) {
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
                mapModel.add(type, new MapElement(path, type,  true));
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
                mapModel.add(type, new MapElement(path, type,  false));
                break;

            default:
                break;
        }
    }

}

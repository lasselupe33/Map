package model.osm;

import helpers.LongToOSMNodeMap;
import model.MainModel;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.geom.Path2D;
import java.util.*;

public class OSMHandler extends DefaultHandler {
    private double minLat, minLon, maxLat, maxLon;
    private LongToOSMNodeMap idToNode = new LongToOSMNodeMap(25);
    private Map<Long, OSMWay> idToWay = new HashMap<>();
    private HashMap<OSMNode, OSMWay> coastlines = new HashMap<>();
    private OSMWay way;
    private MainModel model;
    private List<OSMWay> ways = new ArrayList<>();
    private double lonFactor;
    private OSMWayType type;
    private OSMRelation relation;

    public OSMHandler(MainModel m) {
        model = m;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "bounds":
                minLat = Double.parseDouble(attributes.getValue("minlat"));
                minLon = Double.parseDouble(attributes.getValue("minlon"));
                maxLat = Double.parseDouble(attributes.getValue("maxlat"));
                maxLon = Double.parseDouble(attributes.getValue("maxlon"));
                double avgLat = minLat + (maxLat - minLat) / 2;
                lonFactor = Math.cos(avgLat / 180 * Math.PI);
                minLon *= lonFactor;
                model.setMinLon(minLon);
                maxLon *= lonFactor;
                model.setMaxLon(maxLon);
                maxLat = -maxLat;
                model.setMaxLat(maxLat);
                minLat = -minLat;
                model.setMinLat(minLat);
                break;
            case "node":
                double lon = Double.parseDouble(attributes.getValue("lon"));
                double lat = Double.parseDouble(attributes.getValue("lat"));
                long id = Long.parseLong(attributes.getValue("id"));
                idToNode.put(id, lonFactor * lon, -lat);
                break;
            case "way":
                way = new OSMWay();
                type = OSMWayType.UNKNOWN;
                idToWay.put(Long.parseLong(attributes.getValue("id")), way);
                break;
            case "relation":
                relation = new OSMRelation();
                type = OSMWayType.UNKNOWN;
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
                        if (attributes.getValue("v").equals("primary")) {
                            type = OSMWayType.HIGHWAY;
                        }
                        if (attributes.getValue("v").equals("secondary")) {
                            type = OSMWayType.SECONDARYROAD;
                        }
                        if (attributes.getValue("v").equals("secondary")) {
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
                            type = OSMWayType.PARK;
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
        Path2D path = new Path2D.Double();
        OSMNode node;
        switch (qName) {
            case "way":
                if (type == OSMWayType.COASTLINE) {
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
                } else {
                    node = way.get(0);
                    path.moveTo(node.getLon(), node.getLat());
                    for (int i = 1; i < way.size(); i++) {
                        node = way.get(i);
                        path.lineTo(node.getLon(), node.getLat());
                    }


                    model.add(type, path);

                }
                break;
            case "relation":
                for (OSMWay way: relation) {
                    node = way.get(0);
                    path.moveTo(node.getLon(), node.getLat());
                    for (int i = 1; i < way.size(); i++) {
                        node = way.get(i);
                        path.lineTo(node.getLon(), node.getLat());
                    }
                }
                model.add(type, path);
                break;
            case "osm":
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
                        model.add(OSMWayType.COASTLINE, path);
                    }

                }
            default:
                break;
        }
    }

    public List<OSMWay> getListOfWays(){
        return ways;
    }
}

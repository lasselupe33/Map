package model.osm;

import helpers.LongToOSMNodeMap;
import model.MainModel;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;

public class OSMHandler extends DefaultHandler {
    private double minLat, minLon, maxLat, maxLon;
    LongToOSMNodeMap idToNode = new LongToOSMNodeMap(25);
    Map<Long, OSMWay> idToWay = new HashMap<>();
    HashMap<OSMNode, OSMWay> coastlines = new HashMap<>();
    OSMWay way;
    MainModel model;
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
                maxLon *= lonFactor;
                maxLat = -maxLat;
                minLat = -minLat;
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
                        break;
                    case "natural":
                        if (attributes.getValue("v").equals("water")) {
                            type = OSMWayType.WATER;
                        } else if (attributes.getValue("v").equals("coastline")) {
                            type = OSMWayType.COASTLINE;
                        }
                        break;
                    case "building":
                        type = OSMWayType.BUILDING;
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
}

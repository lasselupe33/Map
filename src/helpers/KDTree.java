package helpers;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.MapElements.MapElement;
import model.osm.OSMWay;
import model.osm.OSMWayType;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

import java.util.*;

public class KDTree implements Serializable {
    private double maxLat, minLat, maxLon, minLon;
    private Node[] roots;
    private static int amountOfElements = 0;

    public static class Comparators {
        static final Comparator<MapElement> X_COMPARATOR = Comparator.comparing(MapElement::getElementX);
        static final Comparator<MapElement> Y_COMPARATOR = Comparator.comparing(MapElement::getElementY);
    }

    public class Node implements Serializable {
        private List<MapElement> value;
        private double split;
        private Node leftChild, rightChild;

        Node(double spl){
            split = spl;
        }

        Node(List<MapElement> val){
            value = val;
        }
    }

    public KDTree (EnumMap<OSMWayType, List<MapElement>> map, double _maxLat, double _minLat, double _maxLon, double _minLon) {
        maxLat = _maxLat;
        minLat = _minLat;
        maxLon = _maxLon;
        minLon = _minLon;

        roots = new Node[OSMWayType.values().length];

        int i = 0;
        for (OSMWayType type : OSMWayType.values()) {
            amountOfElements += map.get(type).size();
            roots[i] = buildTree(map.get(type));
            i++;
        }

        System.out.println(amountOfElements);
    }

    /**
     * Method to be called when one wishes to begin creating a new tree
     */
    private Node buildTree(List<MapElement> list) {
        return buildTree(list, 0);
    }

    /**
     * Helper that recursively builds a tree.
     */
    private Node buildTree(List<MapElement> list, int depth){
        //System.out.println("Entering depth " + depth + " with list size: " + list.size());
        if (list.size() < 1000) return new Node(list);

        int median = list.size()/2;
        double split = 0;
        Line2D splitLine = null;


        int axis = axis(depth);
        switch (axis) {
            case 0:
                list.sort(Comparators.X_COMPARATOR);
                split = list.get(median).getElementX();
                splitLine = new Line2D.Double(new Point2D.Double(split, minLat), new Point2D.Double(split, maxLat));
                break;
            case 1:
                list.sort(Comparators.Y_COMPARATOR);
                split = list.get(median).getElementY();
                splitLine = new Line2D.Double(new Point2D.Double(minLon, split), new Point2D.Double(maxLon, split));
                break;
            default:
                break;
        }


        List<MapElement> listLeft = new ArrayList<>();
        List<MapElement> listRight = new ArrayList<>();


            for(int i = 0; i < list.size(); i++) {
                MapElement s = list.get(i);

                    if (splitLine.intersects(s.getBounds())) {
                        listLeft.add(s);
                        listRight.add(s);
                    } else if (i < median) {
                        listLeft.add(s);
                    } else {
                        listRight.add(s);
                    }

            }

        Node x = new Node(split);
        x.leftChild = buildTree(listLeft, depth + 1);
        x.rightChild = buildTree(listRight, depth + 1);

        //System.out.println("Leaving depth " + depth + " at axis = " + axis);
        return x;
    }


    // search the KD Tree
    public List<MapElement> searchTree(Point2D p0, Point2D p1, int level){
        int depth = 0;
        Node root = getRoot(level);
        if ( root == null ) return Collections.EMPTY_LIST;
        return searchTree(root, p0, p1, depth);
    }

    private List<MapElement> searchTree(Node x, Point2D p0, Point2D p1, int depth){
        List<MapElement> list = new ArrayList<>();

        if(x.value != null) return x.value;

        switch (axis(depth)) {
            case 0:
                if(p0.getX() < x.split && p1.getX() < x.split) list.addAll(searchTree(x.leftChild, p0, p1, depth + 1));
                else if (p0.getX() > x.split && p1.getX() > x.split) list.addAll(searchTree(x.rightChild, p0, p1, depth + 1));
                else {
                    list.addAll(searchTree(x.leftChild, p0, p1, depth + 1));
                    list.addAll(searchTree(x.rightChild, p0, p1, depth + 1));
                }
                break;
            case 1:
                if(p0.getY() < x.split && p1.getY() < x.split) list.addAll(searchTree(x.leftChild, p0, p1, depth + 1));
                else if (p0.getY() > x.split && p1.getY() > x.split) list.addAll(searchTree(x.rightChild, p0, p1, depth + 1));
                else {
                    list.addAll(searchTree(x.leftChild, p0, p1, depth + 1));
                    list.addAll(searchTree(x.rightChild, p0, p1, depth + 1));
                }
                break;
            default:
                break;
        }

        return list;
    }


    private Node getRoot(int level) {
        return roots[level];
    }


    // check if depth is even
    private int axis(int depth) {
        return depth % 2;
    }
}

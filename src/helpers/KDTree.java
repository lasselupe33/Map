package helpers;

import model.MainModel;
import model.MapElement;
import model.osm.OSMWayType;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KDTree {
    private MainModel model;
    private Node rootCoastline, rootWater, rootRoad, rootHighway, rootBuilding, rootUnknown;

    public static class Comparators {
        static final Comparator<MapElement> X_COMPARATOR = Comparator.comparing(MapElement::getElementX);
        static final Comparator<MapElement> Y_COMPARATOR = Comparator.comparing(MapElement::getElementY);
    }

    private class Node{
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

    public KDTree (MainModel m) {
        model = m;
        int depth = 0;

        rootCoastline = buildTree(rootCoastline, model.get(OSMWayType.COASTLINE), depth);
        rootWater = buildTree(rootWater, model.get(OSMWayType.WATER), depth);
        rootRoad = buildTree(rootRoad, model.get(OSMWayType.ROAD), depth);
        rootHighway = buildTree(rootHighway, model.get(OSMWayType.HIGHWAY), depth);
        rootBuilding = buildTree(rootBuilding, model.get(OSMWayType.BUILDING), depth);
        rootUnknown = buildTree(rootUnknown, model.get(OSMWayType.UNKNOWN), depth);
    }


    // creating the KD Tree and inserting data

    private Node buildTree(Node x, List<MapElement> list, int depth){
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
                splitLine = new Line2D.Double(new Point2D.Double(split, model.getMinLat()), new Point2D.Double(split, model.getMaxLat()));
                break;
            case 1:
                list.sort(Comparators.Y_COMPARATOR);
                split = list.get(median).getElementY();
                splitLine = new Line2D.Double(new Point2D.Double(model.getMinLon(), split), new Point2D.Double(model.getMaxLon(), split));
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

        x = new Node(split);
        x.leftChild = buildTree(x.leftChild, listLeft, depth + 1);
        x.rightChild = buildTree(x.rightChild, listRight, depth + 1);

        //System.out.println("Leaving depth " + depth + " at axis = " + axis);
        return x;
    }


    // search the KD Tree
    public List<MapElement> searchTree(Point2D p0, Point2D p1, OSMWayType type){
        int depth = 0;
        Node root = getRoot(type);
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


    private Node getRoot(OSMWayType type) {
        switch (type){
            case COASTLINE:
                return rootCoastline;
            case WATER:
                return rootWater;
            case ROAD:
                return rootRoad;
            case HIGHWAY:
                return rootHighway;
            case BUILDING:
                return rootBuilding;
            case UNKNOWN:
                return rootUnknown;
            default:
                break;
        }
        return null;
    }



    // check if depth is even
    private int axis(int depth) {
        return depth % 2;
    }

}
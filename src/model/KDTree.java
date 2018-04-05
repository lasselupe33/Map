package model;

import model.MapElements.MapElement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

public class KDTree {
    private static EnumMap<ZoomLevel, List<MapElement>> mapelements;
    private double maxLat, minLat, maxLon, minLon;
    private Node rootOne, rootTwo, rootThree, rootFour, rootFive, rootSix;

    public static class Comparators {
        static final Comparator<MapElement> X_COMPARATOR = Comparator.comparing(MapElement::getElementX);
        static final Comparator<MapElement> Y_COMPARATOR = Comparator.comparing(MapElement::getElementY);
        static final Comparator<MapElement> TYPE_COMPARATOR = Comparator.comparing(MapElement::getType);
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

    public KDTree (EnumMap map, double _maxLat, double _minLat, double _maxLon, double _minLon) {
        mapelements = map;
        maxLat = _maxLat;
        minLat = _minLat;
        maxLon = _maxLon;
        minLon = _minLon;

        int depth = 0;

        CalculateZoomLevel.calculateNumberOfElements();

        rootOne = buildTree(rootOne, mapelements.get(ZoomLevel.ONE), depth);
        rootTwo = buildTree(rootTwo, mapelements.get(ZoomLevel.TWO), depth);
        rootThree = buildTree(rootThree, mapelements.get(ZoomLevel.THREE), depth);
        rootFour = buildTree(rootFour, mapelements.get(ZoomLevel.FOUR), depth);
        rootFive = buildTree(rootFive, mapelements.get(ZoomLevel.FIVE), depth);
        rootSix = buildTree(rootSix, mapelements.get(ZoomLevel.SIX), depth);

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

        x = new Node(split);
        x.leftChild = buildTree(x.leftChild, listLeft, depth + 1);
        x.rightChild = buildTree(x.rightChild, listRight, depth + 1);

        //System.out.println("Leaving depth " + depth + " at axis = " + axis);
        return x;
    }


    // search the KD Tree
    public List<MapElement> searchTree(Point2D p0, Point2D p1, ZoomLevel type){
        int depth = 0;
        Node root = getRoot(type);
        if ( root == null ) return Collections.EMPTY_LIST;
        List<MapElement> list = searchTree(root, p0, p1, depth);

        return list;
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


    private Node getRoot(ZoomLevel type) {
        switch (type){
            case ONE:
                return rootOne;
            case TWO:
                return rootTwo;
            case THREE:
                return rootThree;
            case FOUR:
                return rootFour;
            case FIVE:
                return rootFive;
            case SIX:
                return rootSix;
            default:
                break;
        }
        return null;
    }



    // check if depth is even
    private int axis(int depth) {
        return depth % 2;
    }


    public static class CalculateZoomLevel {

        private static int zoomScale;

        static void calculateNumberOfElements(){

            for (ZoomLevel level : ZoomLevel.values()) {
                zoomScale += mapelements.get(level).size();
            }
            System.out.println("Number of elements: " + zoomScale);
            int length = String.valueOf(zoomScale).length();
            int firstDigit = Character.getNumericValue(("" + zoomScale).charAt(0));
            zoomScale = length * firstDigit;

        }

        public static int getScale(){
            return zoomScale/2;
        }
    }

}
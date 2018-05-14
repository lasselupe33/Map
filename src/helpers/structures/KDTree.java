package helpers.structures;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Coordinates;
import model.MapElement;


public class KDTree<Value extends Coordinates> implements Externalizable {
    private double maxLat, minLat, maxLon, minLon;
    private Node root;

    /**
     * Comparator used to compare coordinates x or y value
     */
    public static class Comparators {
        static final Comparator<Coordinates> X_COMPARATOR = Comparator.comparing(Coordinates::getX);
        static final Comparator<Coordinates> Y_COMPARATOR = Comparator.comparing(Coordinates::getY);
    }


    public static class Node<Value extends Coordinates> implements Externalizable {
        private List<Value> valueList;
        private double split;
        private Node leftChild, rightChild;

        public Node() {}
        public Node(double spl){
            split = spl;
        }
        public Node(List<Value> val){
            valueList = val;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(valueList);
            out.writeDouble(split);
            out.writeObject(leftChild);
            out.writeObject(rightChild);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            valueList = (List<Value>) in.readObject();
            split = in.readDouble();
            leftChild = (Node) in.readObject();
            rightChild = (Node) in.readObject();
        }
    }


    public KDTree() {}

    /**
     * Constructor used for MapElements
     */
    public KDTree (List<Value> list, double _maxLat, double _minLat, double _maxLon, double _minLon) {
        maxLat = _maxLat;
        minLat = _minLat;
        maxLon = _maxLon;
        minLon = _minLon;

        root = buildTree(list);
    }

    /**
     * Constructor used for Coordinates
     */
    public KDTree (List<Value> list) {
        root = buildTree(list);
    }

    /**
     * Method to be called when one wishes to begin creating a new tree
     */
    private Node buildTree(List<Value> list) {
        return buildTree(list, 0);
    }

    /**
     * Helper that recursively builds a tree.
     */
    private Node buildTree(List<Value> list, int depth){
        // If list is less than 1000 elements, store it in a new Node and return it
        if (list.size() < 1000) return new Node(list);

        int median = list.size()/2;
        double split = 0;
        Line2D splitLine = null;

        // Sorting the list and creates a split value for either x or y values,
        // based on the current depth
        switch (axis(depth)) {
            case 0:
                list.sort(Comparators.X_COMPARATOR);
                split = list.get(median).getX();
                splitLine = new Line2D.Double(new Point2D.Double(split, minLat), new Point2D.Double(split, maxLat));
                break;
            case 1:
                list.sort(Comparators.Y_COMPARATOR);
                split = list.get(median).getY();
                splitLine = new Line2D.Double(new Point2D.Double(minLon, split), new Point2D.Double(maxLon, split));
                break;
            default:
                break;
        }


        List<Value> listLeft = new ArrayList<>();
        List<Value> listRight = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Value s = list.get(i);
            if (s instanceof MapElement) {
                // If the shape can be found on both side of splitLine...
                if (splitLine.intersects(getBounds((MapElement) s))) {
                    // ... then add the element to both lists ...
                    listLeft.add(s);
                    listRight.add(s);

                // else sort the elements into liftLeft or listRight
                } else if (i < median) {
                    listLeft.add(s);
                } else {
                    listRight.add(s);
                }


            // If the list do NOT consist of MapElements, then use this
            } else {
                if (i < median) {
                    listLeft.add(s);
                } else {
                    listRight.add(s);
                }
            }
        }
        
        // Create new Node and continue the recursion
        Node x = new Node(split);
        x.leftChild = buildTree(listLeft, depth + 1);
        x.rightChild = buildTree(listRight, depth + 1);

        return x;
    }



    /**
     * Method to be called for when beginning searching in the tree
     */
    public List<Value> searchTree(Point2D p0, Point2D p1){
        return searchTree(root, p0, p1, 0);
    }

    /**
     * Helper that recursively search in a tree.
     */
    private List<Value> searchTree(Node n, Point2D p0, Point2D p1, int depth){

        List<Value> list = new ArrayList<>();

        // Return the list of values if it is not null
        if(n.valueList != null) {

            return n.valueList;
        }


        // Check if points are left or right to the split and go recursively down to the right child
        if(getValue(depth, p0) < n.split && getValue(depth, p1) < n.split) list.addAll(searchTree(n.leftChild, p0, p1, depth + 1));
        else if (getValue(depth, p0) > n.split && getValue(depth, p1) > n.split) list.addAll(searchTree(n.rightChild, p0, p1, depth + 1));

        // If neither, just go down both branches
        else {
            list.addAll(searchTree(n.leftChild, p0, p1, depth + 1));
            list.addAll(searchTree(n.rightChild, p0, p1, depth + 1));
        }

        return list;
    }

    /**
     * Method to find the nearest neightbour based on an x and y value
     */
    public Value nearestNeighbour(double px, double py){

        // Creates a point and searches in the tree
        Point2D p0 = new Point2D.Double(px, py);
        Point2D p1 = new Point2D.Double(px, py);
        List<Value> currentSearchList = searchTree(p0, p1);

        Value nearestNeighbour = null;
        double currentNeighbour = Double.MAX_VALUE;

        // Loop through the list and chooses the nearest neighbour from it
        for (Value val : currentSearchList) {
            double distanceTo = Math.hypot( px - val.getX(), py - val.getY());
            if ( distanceTo < currentNeighbour ) {
                nearestNeighbour = val;
                currentNeighbour = distanceTo;
            }
        }

        return nearestNeighbour;
    }

    private Rectangle2D getBounds(MapElement mapElement){
        return mapElement.getBounds();
    }

    /**
     * Helper that checks if depth is even
     */
    private int axis(int depth) {
        return depth % 2;
    }

    /**
     * Helper that returns an x oy y value for a coordinate
     */
    private double getValue(int depth, Point2D p) {
        if (axis(depth) == 0) return p.getX();
        else return p.getY();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(root);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        root = (Node) in.readObject();
    }
}

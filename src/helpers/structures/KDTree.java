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
    public KDTree (List<Value> list, double _maxLat, double _minLat, double _maxLon, double _minLon) {
        maxLat = _maxLat;
        minLat = _minLat;
        maxLon = _maxLon;
        minLon = _minLon;

        root = buildTree(list);
    }
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
        if (list.size() < 1000) return new Node(list);

        int median = list.size()/2;
        double split = 0;
        Line2D splitLine = null;

        int axis = axis(depth);
        switch (axis) {
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
                    if (splitLine.intersects(getBounds((MapElement) s))) {
                        listLeft.add(s);
                        listRight.add(s);
                    } else if (i < median) {
                        listLeft.add(s);
                    } else {
                        listRight.add(s);
                    }
                } else {
                    if (i < median) {
                        listLeft.add(s);
                    } else {
                        listRight.add(s);
                    }
                }
            }
        

        Node x = new Node(split);
        x.leftChild = buildTree(listLeft, depth + 1);
        x.rightChild = buildTree(listRight, depth + 1);

        //System.out.println("Leaving depth " + depth + " at axis = " + axis);
        return x;
    }




    // search the KD Tree
    public List<Value> searchTree(Point2D p0, Point2D p1){
        int depth = 0;
        return searchTree(root, p0, p1, depth);
    }

    private List<Value> searchTree(Node x, Point2D p0, Point2D p1, int depth){

        List<Value> list = new ArrayList<>();

        if(x.valueList != null) {

            return x.valueList;
        }

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

    public Value nearestNeighbour(double px, double py){
        Point2D p0 = new Point2D.Double(px, py);
        Point2D p1 = new Point2D.Double(px, py);
        List<Value> currentSearchList = searchTree(p0, p1);

        Value nearestNeighbour = null;
        double currentNeighbour = Double.MAX_VALUE;
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

    // check if depth is even
    private int axis(int depth) {
        return depth % 2;
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

package helpers;

import model.MainModel;
import model.MapElement;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KDTree {
    private Node root;
    private MainModel model;
    private int[] zoomValue = {30, 20, 10, 0};
    private int zoom, size, scale;

    public static class Comparators {
        static final Comparator<MapElement> X_COMPARATOR = Comparator.comparing(MapElement::getElementX);
        static final Comparator<MapElement> Y_COMPARATOR = Comparator.comparing(MapElement::getElementY);
        static final Comparator<MapElement> Z_COMPARATOR = Comparator.comparing(MapElement::getTypeZoomLevel);
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

    public KDTree (List<MapElement> list, MainModel m) {
        model = m;
        int index = 0;
        int depth = 0;
        root = buildTree(root, list, depth, index);
    }


    // creating the KD Tree and inserting data

    private Node buildTree(Node x, List<MapElement> list, int depth, int index){
        //System.out.println("Entering depth " + depth + " with list size: " + list.size());
        if (list.size() < 1000) return new Node(list);

        int median = list.size()/2;
        double split = 0;
        Line2D splitLine = null;


        int axis = axis(depth);
        switch (axis) {
            case 0:
                list.sort(Comparators.Z_COMPARATOR);
                break;
            case 1:
                list.sort(Comparators.X_COMPARATOR);
                split = list.get(median).getElementX();
                splitLine = new Line2D.Double(new Point2D.Double(split, model.getMinLat()), new Point2D.Double(split, model.getMaxLat()));
                break;
            case 2:
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

                if (axis == 0) {
                    if (i < median) {
                        listLeft.add(s);
                    } else {
                        listRight.add(s);
                    }
                } else {

                    if (splitLine.intersects(s.getBounds())) {
                        listLeft.add(s);
                        listRight.add(s);
                    } else if (i < median) {
                        listLeft.add(s);
                    } else {
                        listRight.add(s);
                    }
                }
            }

        x = new Node(split);
        x.leftChild = buildTree(x.leftChild, listLeft, depth + 1, index);
        x.rightChild = buildTree(x.rightChild, listRight, depth + 1, index);

        //System.out.println("Leaving depth " + depth + " at axis = " + axis);
        return x;
    }


    // search the KD Tree
    public List<MapElement> searchTree(Point2D p0, Point2D p1, int z){
        zoom = z;
        int index = 0;
        int depth = 0;
        return searchTree(root, p0, p1, depth, index);
    }

    private List<MapElement> searchTree(Node x, Point2D p0, Point2D p1, int depth, int index){

        List<MapElement> list = new ArrayList<>();

        if(x.value != null) return x.value;



        switch (axis(depth)) {
            case 1:
                if(p0.getX() < x.split && p1.getX() < x.split) list.addAll(searchTree(x.leftChild, p0, p1, depth + 1, index));
                else if (p0.getX() > x.split && p1.getX() > x.split) list.addAll(searchTree(x.rightChild, p0, p1, depth + 1, index));
                else {
                    list.addAll(searchTree(x.leftChild, p0, p1, depth + 1, index));
                    list.addAll(searchTree(x.rightChild, p0, p1, depth + 1, index));
                }
                break;
            case 2:
                if(p0.getY() < x.split && p1.getY() < x.split) list.addAll(searchTree(x.leftChild, p0, p1, depth + 1, index));
                else if (p0.getY() > x.split && p1.getY() > x.split) list.addAll(searchTree(x.rightChild, p0, p1, depth + 1, index));
                else {
                    list.addAll(searchTree(x.leftChild, p0, p1, depth + 1, index));
                    list.addAll(searchTree(x.rightChild, p0, p1, depth + 1, index));
                }
                break;
            case 0:

                if ( index >= zoomValue.length ) index = zoomValue.length-1;
                if (zoom < zoomValue[index++]) list.addAll(searchTree(x.leftChild, p0, p1, depth + 1, index));
                else {
                    list.addAll(searchTree(x.leftChild, p0, p1, depth + 1, index));
                    list.addAll(searchTree(x.rightChild, p0, p1, depth + 1, index));
                }

                break;
            default:
                break;
        }





        return list;
    }





    // check if depth is even
    private int axis(int depth) {
        return depth % 3;
    }

}

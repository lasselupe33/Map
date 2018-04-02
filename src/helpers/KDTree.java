package helpers;

import model.MainModel;
import model.MapElement;
import model.osm.OSMWayType;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class KDTree {
    private Node root1, root2, root3;
    private MainModel model;
    private int[] zoomValue = {0, 5, 10, 15};
    private int zoom;

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
        int depth = 0;
        List<MapElement> list1 = new ArrayList<>();
        List<MapElement> list2 = new ArrayList<>();
        List<MapElement> list3 = new ArrayList<>();

        list.sort(Comparators.Z_COMPARATOR);
        int tmp = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTypeZoomLevel().equals(OSMWayType.WATER)) break;
            list1.add(list.get(i));
            tmp = i;
        }

        for (int i = tmp+1; i < list.size(); i++) {
            if (list.get(i).getTypeZoomLevel().equals(OSMWayType.BUILDING)) break;
            list2.add(list.get(i));
            tmp = i;
        }

        for (int i = tmp+1; i < list.size(); i++) {
            list3.add(list.get(i));
        }

        root1 = buildTree(root1, list1, depth);
        root2 = buildTree(root2, list2, depth);
        root3 = buildTree(root3, list3, depth);
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
    public List<MapElement> searchTree(Point2D p0, Point2D p1, int zoom){
        int depth = 0;
        List<MapElement> list = new ArrayList<>();

        list.addAll(searchTree(root1, p0, p1, depth));
        if ( zoom > 20 ) list.addAll(searchTree(root2, p0, p1, depth));
        if ( zoom > 40 ) list.addAll(searchTree(root3, p0, p1, depth));
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





    // check if depth is even
    private int axis(int depth) {
        return depth % 2;
    }

}

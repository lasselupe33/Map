package helpers;

import model.MainModel;
import model.MapElements;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class KDTree {
    private Node root;
    private MainModel model;

    private class Node{
        private List<MapElements> value;
        private double split;
        private Node leftChild, rightChild;

        Node(double spl){
            split = spl;
        }

        Node(List<MapElements> val){
            value = val;
        }
    }




    // search the KD Tree
    public List<MapElements> searchTree(Point2D p0, Point2D p1){
        int depth = 0;
        return searchTree(root, p0, p1, depth);
    }

    private List<MapElements> searchTree(Node x, Point2D p0, Point2D p1, int depth){

        List<MapElements> list = new ArrayList<>();

        if(x.value != null) return x.value;

        if(isEven(depth)){

            if(p0.getX() < x.split && p1.getX() < x.split) list.addAll(searchTree(x.leftChild, p0, p1, depth + 1));
            else if (p0.getX() > x.split && p1.getX() > x.split) list.addAll(searchTree(x.rightChild, p0, p1, depth + 1));
            else {
                list.addAll(searchTree(x.leftChild, p0, p1, depth + 1));
                list.addAll(searchTree(x.rightChild, p0, p1, depth + 1));
            }

        } else {

            if(p0.getY() < x.split && p1.getY() < x.split) list.addAll(searchTree(x.leftChild, p0, p1, depth + 1));
            else if (p0.getY() > x.split && p1.getY() > x.split) list.addAll(searchTree(x.rightChild, p0, p1, depth + 1));
            else {
                list.addAll(searchTree(x.leftChild, p0, p1, depth + 1));
                list.addAll(searchTree(x.rightChild, p0, p1, depth + 1));
            }

        }

        return list;
    }





    // creating the KD Tree and inserting data
    public void createTree(List<MapElements> list, MainModel m){
        model = m;
        int depth = 0;
        root = buildTree(root, list, depth);
    }

    private Node buildTree(Node x, List<MapElements> list, int depth){
        //System.out.println("Entering depth " + depth + " with list size: " + list.size());
        if (list.size() < 1000) return new Node(list);

        SortingClass.sort(list, isEven(depth));

        int median = list.size()/2;
        double split;
        Line2D splitLine;
        if(isEven(depth)){
            split = list.get(median).getX();
            splitLine = new Line2D.Double(new Point2D.Double(split, model.getMinLat()), new Point2D.Double(split, model.getMaxLat()));
        } else {
            split = list.get(median).getY();
            splitLine = new Line2D.Double(new Point2D.Double(model.getMinLon(), split), new Point2D.Double(model.getMaxLon(), split));
        }

        List<MapElements> listLeft = new ArrayList<>();
        List<MapElements> listRight = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            MapElements s = list.get(i);
            if (splitLine.intersects(s.getBounds())){
                listLeft.add(s);
                listRight.add(s);
            } else if(i < median) {
                listLeft.add(s);
            } else {
                listRight.add(s);
            }
        }

        x = new Node(split);
        x.leftChild = buildTree(x.leftChild, listLeft, depth + 1);
        x.rightChild = buildTree(x.rightChild, listRight, depth + 1);

        //System.out.println("Leaving depth " + depth);
        return x;
    }

    // check if depth is even
    private boolean isEven(int depth) {
        return (depth % 2 == 0);
    }

}

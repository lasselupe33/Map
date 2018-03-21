package helpers;

import model.osm.OSMWay;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class KDTree {
    private Node root;

    private class Node{
        private List<Shape> value;
        private double split;
        private Node leftChild, rightChild;

        public Node(double spl){
            split = spl;
        }

        public Node(List<Shape> val){
            value = val;
        }
    }

    public void createTree(List<Shape> list){
        int depth = 0;
        root = buildTree(root, list, depth);
    }

    public Node buildTree(Node x, List<Shape> list, int depth){
        if (list.size() < 1000) return new Node(list);

        SortingClass.sort(list, isEven(depth));

        int median = list.size()/2;
        double split;
        Line2D splitLine;
        if(isEven(depth)){
            split = list.get(median).getX();
            splitLine = new Line2D.Double(new Point2D.Double(split, y0), new Point2D.Double(split, y1));
        } else {
            split = list.get(median).getY();
            splitLine = new Line2D.Double(new Point2D.Double(x0, split), new Point2D.Double(x1, split));
        }

        List<Shape> listLeft = new ArrayList<>();
        List<Shape> listRight = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            Shape s = list.get(i);
            if (splitLine.intersects(s.getBounds2D())){
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
        return x;
    }

    private boolean isEven(int depth) {
        return (depth % 2 == 0);
    }

}

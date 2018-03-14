package helpers;

import model.osm.OSMWay;

import java.util.ArrayList;
import java.util.List;

public class KDTree<Key extends Comparable<Key>, Value extends List<OSMWay>> {
    private OSMWay[] ways;
    private Node root;

    private class Node{
        private Key key;
        private Value value;
        private double split;
        private Node leftChild, rightChild;

        public Node(Key k, double spl, Value val, Node left, Node right){
            key = k; split = spl; value = val; leftChild = left; rightChild = right;
        }
    }

    /*public OSMWay get(Key key){
        return way;
    }*/

    public void buildTree(Key key, OSMWay val, ArrayList<OSMWay> OSMways){
        ways = new OSMWay[OSMways.size()];
        ways = OSMways.toArray(ways);

        int depth = 0; int lo = 0; int hi = ways.length-1;
        root = buildTree(root, key, val, depth, lo, hi);
    }

    public Node buildTree(Node x, Key key, OSMWay way, int depth, int lo, int hi){

        double split = split(depth, lo, hi);

        x.leftChild = buildTree(x.leftChild, key, way, depth + 1, lo, hi/2);
        x.rightChild = buildTree(x.rightChild, key, way, depth + 1, lo+(hi/2), hi);
        return x;
    }

    private double split(int depth, int lo, int hi){
        boolean even;
        if(depth % 2 == 0) even = true;
        else even = false;
        SortingClass.sort(ways, lo, hi, even);

        double split;
        if (even) split = ways[hi/2].getAvgLat();
        else split = ways[hi/2].getAvgLon();

        return split;
    }

}

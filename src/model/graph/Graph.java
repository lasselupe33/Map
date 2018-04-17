package model.graph;

import model.Coordinates;

import java.util.HashMap;

public class Graph {
    HashMap<Coordinates,Node> nodes = new HashMap<>();
    public Graph() {

    }

    public void addNode(Coordinates coords, Node node) {
        nodes.put(coords, node);
    }
}

package model.graph;

import helpers.structures.LongToNodeMap;
import model.MapModel;

import java.awt.geom.Path2D;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Graph {
    LongToNodeMap nodes;

    public Graph() {
        nodes = new LongToNodeMap(25);
    }

    public void putNode(Node node) {
        nodes.put(node.getId(), node.getLon(), node.getLat());
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public void computePath(Node source, Node dest) {
        System.out.println(source);
        System.out.println(dest);
        System.out.println(dest.getLon());
        System.out.println(dest.getLat());
        PriorityQueue<Node> pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) (a.getDistToSource() - b.getDistToSource()));
        source.setDistToSource(0);
        pq.add(source);
        ArrayList<Node> path = new ArrayList<>();

        /*
        // TEST PRINTING
        System.out.println("Source edges:" + source.getEdges().size());
        System.out.println("Dest edges: " + dest.getEdges().size());
        System.out.println(pq.size());
        */

        while (true) {
            if (pq.size() == 0) {
                break;
            }

            Node current = pq.remove();


            if (current == dest) {
                break;
            }


            current.setAddedToTree();
            path.add(current);

            //System.out.println("edges: " + current.getEdges().size());
            //System.out.println("id: " + current.getId());

            for (int i = 0; i < current.getEdges().size(); i++) {
                Edge edgeToNeighbour = current.getEdges().get(i);
                /*
                if (!edgeToNeighbour.supportsCars()) {
                    continue;
                }
                */
                Node neighbour = nodes.get(edgeToNeighbour.getTo(current).getId());

                if (!neighbour.addedToTree()) {
                    pq.add(neighbour);
                    neighbour.setAddedToTree();
                }

                if (neighbour.getDistToSource() > current.getDistToSource() + edgeToNeighbour.getLength()) {
                    neighbour.setDistToSource(current.getDistToSource() + edgeToNeighbour.getLength());
                    neighbour.setParent(current);
                }
            }
        }

        Path2D test = new Path2D.Float();
        Node node = dest;
        test.moveTo(node.getLon(), node.getLat());

        while(node.getParent() != null) {
            node = node.getParent();
            test.lineTo(node.getLon(), node.getLat());
        }

        MapModel.shortPath = test;

        /*
        for (Node n: path) {
            System.out.println(n.toKey());
        }
        System.out.println(path.size());
        System.out.println("");
        for (String ss : nodes.getIds()) {
            Node nn = nodes.get(ss);
            if(nn.getEdges().size() == 0) {System.out.println(nn.getEdges().size());}

        }
        */
    }
}

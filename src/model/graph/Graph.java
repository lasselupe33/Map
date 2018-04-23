package model.graph;

import helpers.LongToNodeMap;
import model.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Graph {
    LongToNodeMap nodes;

    public Graph() {}

    public void setNodes(LongToNodeMap map) {
        nodes = map;
    }

    public Node getNode(Long id) {
        return nodes.get(id);
    }

    public void computePath(Node source, Node dest) {
        PriorityQueue<Node> pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) (a.getDistToSource() - b.getDistToSource()));
        source.setDistToSource(0);
        pq.add(source);
        ArrayList<Node> path = new ArrayList<>();
        Node n = nodes.get(source.getId());
        System.out.println(n.getEdges().size());
        System.out.println(source.getEdges().size());
        System.out.println(pq.size());
        System.out.println("");

        n = nodes.get(18054776 );
        System.out.println(n.getEdges().size());


        while (true) {
            Node current = pq.remove();

            if (current == null || current == dest) {
                break;
            }

            current.setAddedToTree();
            path.add(current);

            System.out.println("edges: " + current.getEdges().size());

            for (int i = 0; i < current.getEdges().size(); i++) {
                Edge edgeToNeighbour = current.getEdges().get(i);
                Node neighbour = edgeToNeighbour.getTo();


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

        System.out.println(path.size());
    }
}

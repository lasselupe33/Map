package model.graph;

import helpers.structures.LongToNodeMap;
import model.MapModel;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Graph {
    LongToNodeMap nodes;
    private VehicleType type;
    private PriorityQueue<Node> pq;

    public Graph() {
        nodes = new LongToNodeMap(25);
        type = VehicleType.CAR;
    }

    public void putNode(Node node) {
        nodes.put(node.getId(), node.getLon(), node.getLat());
    }

    public Node getNode(long id) {
        return nodes.get(id);
    }

    public void setVehicleType(VehicleType type) {
        this.type = type;
    }

    public VehicleType getType() {
        return type;
    }

    public void resetVertexes() {
        for (Long id : nodes.getIds()) {
            Node n = (Node) nodes.get(id);
            n.setDistToSource(Float.POSITIVE_INFINITY);
            n.setParent(null);
        }
    }

    public void computePath(Node source, Node dest) {
        pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) (a.getDistToSource() - b.getDistToSource()));
        resetVertexes();
        source.setDistToSource(0);
        pq.add(source);

        ArrayList<Node> path = new ArrayList<>();

        while (pq.size() != 0) {
            Node current = pq.remove();

            if (current == dest) {
                break;
            }

            path.add(current);

            for (int i = 0; i < current.getEdges().size(); i++) {
                Edge edgeToNeighbour = current.getEdges().get(i);

                if (current == source) {
                    System.out.println(edgeToNeighbour.supportsCars());
                }
                switch(type) {
                    case CAR:
                        if (!edgeToNeighbour.supportsCars()) {
                            continue;
                        }
                        break;
                    case BICYCLE:
                        if (!edgeToNeighbour.supportsBicycles()) {
                            continue;
                        }
                        break;
                    case PEDESTRIAN:
                        if (!edgeToNeighbour.supportsPedestrians()) {
                            continue;
                        }
                        break;
                    default:
                        break;
                }

                Node neighbour = nodes.get(edgeToNeighbour.getTo(current).getId());

                if (neighbour.getDistToSource() > current.getDistToSource() + edgeToNeighbour.getLength()) {
                    neighbour.setDistToSource(current.getDistToSource() + edgeToNeighbour.getLength());
                    neighbour.setParent(current);
                    pq.add(neighbour);
                }
            }
        }

        Path2D test = new Path2D.Float();
        Node node = dest;
        test.moveTo(node.getLon(), node.getLat());
        System.out.println(dest.getParent());

        while(node.getParent() != null) {
            node = node.getParent();
            test.lineTo(node.getLon(), node.getLat());
        }

        MapModel.shortPath = test;
    }
}

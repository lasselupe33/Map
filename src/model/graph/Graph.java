package model.graph;

import helpers.GetDistance;
import helpers.structures.LongToNodeMap;
import model.MapModel;

import java.awt.geom.Path2D;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Graph {
    private LongToNodeMap nodes;
    private VehicleType type;
    private Path2D shortestPath = new Path2D.Float();

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

    public void computePath(Node source, Node dest) {
        for (Long id : nodes.getIds()) {
            Node n = nodes.get(id);
            n.setDistToSource(Float.POSITIVE_INFINITY);
            n.setEstimateToDest((float) GetDistance.inKM(n.getLat(), n.getLon(), dest.getLat(), dest.getLon()));
        }
        shortestPath = null;

        PriorityQueue<Node> pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) ((a.getDistToSource()+a.getEstimateToDest()) - (b.getDistToSource()+b.getEstimateToDest())));
        source.setDistToSource(0);
        pq.add(source);
        ArrayList<Node> path = new ArrayList<>();

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

            for (int i = 0; i < current.getEdges().size(); i++) {
                Edge edgeToNeighbour = current.getEdges().get(i);

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

        Path2D sp = new Path2D.Float();
        Node node = dest;
        sp.moveTo(node.getLon(), node.getLat());

        while(node.getParent() != null) {
            node = node.getParent();
            sp.lineTo(node.getLon(), node.getLat());
        }
        shortestPath = sp;
    }

    public Path2D getShortestPath() {
        return shortestPath;
    }
}

package model.graph;

import helpers.GetDistance;
import helpers.structures.LongToNodeMap;
import model.MapModel;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Graph {
    private LongToNodeMap nodes;
    private VehicleType type;
    private Path2D shortestPath = new Path2D.Float();
    private PriorityQueue<Node> pq;
    private RouteType routeType = RouteType.FASTEST;
    private Node source;
    private Node dest;

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

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public void resetVertexes(Node dest) {
        for (Long id : nodes.getIds()) {
            Node n = nodes.get(id);
            n.setDistToSource(Float.POSITIVE_INFINITY);
            n.setTimeToSource(Float.POSITIVE_INFINITY);
            n.setParent(null);
            n.setEstimateToDest((float) GetDistance.inKM(n.getLat(), n.getLon(), dest.getLat(), dest.getLon()));
        }
    }

    public void computePath(Node source, Node dest) {
        this.source = source;
        this.dest = dest;
        pq = new PriorityQueue<>(11, (Node a, Node b) -> (int) ((a.getDistToSource()+a.getEstimateToDest(routeType, type)) - (b.getDistToSource()+b.getEstimateToDest(routeType, type))));
        resetVertexes(dest);
        shortestPath = null;
        source.setDistToSource(0);
        source.setTimeToSource(0);
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

                if (routeType == RouteType.SHORTEST) {
                    if (neighbour.getDistToSource() > current.getDistToSource() + edgeToNeighbour.getLength()) {
                        neighbour.setDistToSource(current.getDistToSource() + edgeToNeighbour.getLength());
                        neighbour.setParent(current);
                        pq.add(neighbour);
                    }
                } else {
                    if (neighbour.getTimeToSource() > current.getTimeToSource() + edgeToNeighbour.getTime(type)) {
                        neighbour.setTimeToSource(current.getTimeToSource() + edgeToNeighbour.getTime(type));
                        neighbour.setParent(current);
                        pq.add(neighbour);
                    }
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

    public void setSourceAndDest(Node s, Node d) {
        source = s;
        dest = d;
    }

    public void recalculatePath() {
        if (source != null && dest != null) {
            computePath(source, dest);
        }
    }

    public Path2D getShortestPath() {
        return shortestPath;
    }

}

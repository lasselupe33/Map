package model;

import helpers.structures.SimpleLongSet;
import model.graph.Node;
import parsing.OSMNode;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * A mapElement is a single elements of the map to be drawn on the canvas, meaning that it contains a path.
 *
 * At the same time it contains the ids of the nodes it has been created by in order to successfully find nearest
 * nodes for navigation.
 */
public class MapElement extends Coordinates implements Externalizable {
    private Shape shape;
    private Rectangle2D r;
    private WayType type;
    private boolean shouldFill;
    private SimpleLongSet nodeIds;

    public MapElement() {}
    public MapElement(float x, float y, Shape s, WayType t, boolean sf, ArrayList<OSMNode> nodes){
        super(x, y);
        shape = s;
        type = t;
        shouldFill = sf;
        r = shape.getBounds2D();

        nodeIds = new SimpleLongSet();

        for (OSMNode n : nodes) {
            nodeIds.add(n.getId());
        }
    }

    public Rectangle2D getBounds(){
        if (r == null) {
            r = shape.getBounds2D();
        }

        return r;
    }

    public void updateNodes(SimpleLongSet ids) {
        nodeIds = ids;
    }

    public SimpleLongSet getNodeIds() {
            return nodeIds;
    }

    public WayType getType() { return type; }

    public boolean shouldFill() { return shouldFill; }

    public Shape getShape(){ return shape; }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeFloat(this.getX());
        out.writeFloat(this.getY());
        out.writeObject(shape);
        out.writeObject(type);
        out.writeBoolean(shouldFill);
        out.writeObject(nodeIds);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.setX(in.readFloat());
        this.setY(in.readFloat());
        shape = (Shape) in.readObject();
        type = (WayType) in.readObject();
        shouldFill = in.readBoolean();
        nodeIds = (SimpleLongSet) in.readObject();
    }
}

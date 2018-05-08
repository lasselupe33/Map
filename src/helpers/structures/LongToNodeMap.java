package helpers.structures;

import model.graph.Node;
import parsing.OSMNode;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class LongToNodeMap<Value extends OSMNode> implements Externalizable {
    private Node[] table;
    private int size;
    private int MASK;
    private ArrayList<Long> ids = new ArrayList<>();

    public LongToNodeMap() {}
    public LongToNodeMap(int capacity) {
        table = (Node[]) Array.newInstance(Node.class, 1 << capacity); // there are 2^{capacity} table cells
        MASK = table.length - 1;
    }

    public int size() {
        return size;
    }

    public ArrayList<Long> getIds() {
        return ids;
    }

    public void put(Value v) {
        int position = Long.hashCode(v.getId()) & MASK;
        table[position] = new Node(v, table[position]);
        ids.add(v.getId());
        size++;
    }

    public Value get(long id) {
        int position = Long.hashCode(id) & MASK;
        for (Node n = table[position]; n != null; n = n.next) {
            if (n.value.getId() == id) {
                return n.value;
            }
        }
        return null;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(table);
        out.writeInt(size);
        out.writeInt(MASK);
        out.writeObject(ids);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        table = (Node[]) in.readObject();
        size = in.readInt();
        MASK = in.readInt();
        ids = (ArrayList<Long>) in.readObject();
    }

    private class Node implements Serializable {
        Node next;
        Value value;

        public Node() {}
        public Node(Value v, Node n) {
            this.value = v;
            this.next = n;
        }
    }
}

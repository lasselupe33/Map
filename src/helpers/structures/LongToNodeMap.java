package helpers.structures;

import model.graph.Node;
import parsing.OSMNode;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LongToNodeMap<Value extends OSMNode> {
    private Node[] table;
    private int size;
    private int MASK;
    private ArrayList<Long> ids = new ArrayList<>();

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

    class Node {
        //long id;
        Node next;
        Value value;

        public Node(Value v, Node n) {
            this.value = v;
            this.next = n;
        }

    }
}

package helpers.structures;

import model.graph.Node;

import java.util.ArrayList;

public class LongToNodeMap {
    private N[] table;
    private int size;
    private int MASK;
    private ArrayList<Long> ids = new ArrayList<>();

    public LongToNodeMap(int capacity) {
        table = new N[1 << capacity]; // there are 2^{capacity} table cells
        MASK = table.length - 1;
    }

    public int size() {
        return size;
    }

    public ArrayList<Long> getIds() {
        return ids;
    }

    public void put(long id, float lon, float lat) {
        int position = Long.hashCode(id) & MASK;
        table[position] = new N(id, lon, lat, table[position]);
        ids.add(id);
        size++;
    }

    public N get(long id) {
        int position = Long.hashCode(id) & MASK;
        for (N n = table[position]; n != null; n = n.next) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    class N extends Node {
        //long id;
        N next;

        public N(long id, float lon, float lat, N n) {
            super(id, lon, lat);
            //this.id = id;
            this.next = n;
        }

    }
}

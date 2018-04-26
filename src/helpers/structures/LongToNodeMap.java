package helpers.structures;

import model.graph.Node;

import java.util.ArrayList;

public class LongToNodeMap {
    private ArrayList<String> ids = new ArrayList<>();
    private N[] table;
    private int size;
    int MASK;

    public LongToNodeMap(int capacity) {
        table = new N[1 << capacity]; // there are 2^{capacity} table cells
        MASK = table.length - 1;
    }

    public int size() {
        return size;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void put(String id, float lon, float lat) {
        int position = id.hashCode() & MASK;
        table[position] = new N(lon, lat, table[position]);
        size++;
        ids.add(id);
    }

    public N get(String id) {
        int position = id.hashCode() & MASK;
        for (N n = table[position]; n != null; n = n.next) {
            if (n.getId().equals(id)) {
                return n;
            }
        }
        return null;
    }

    class N extends Node {
        //long id;
        N next;

        public N(float lon, float lat, N n) {
            super(lon, lat);
            //this.id = id;
            this.next = n;
        }

    }
}

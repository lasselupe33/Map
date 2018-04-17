package helpers;

import model.graph.Node;

public class LongToNodeMap {
    private N[] table;
    int MASK;

    public LongToNodeMap(int capacity) {
        table = new N[1 << capacity]; // there are 2^{capacity} table cells
        MASK = table.length - 1;
    }

    public void put(long id, double lon, double lat) {
        int position = Long.hashCode(id) & MASK;
        table[position] = new N(id, lon, lat, table[position]);
    }

    public N get(long id) {
        int position = Long.hashCode(id) & MASK;
        for (N n = table[position]; n != null; n = n.next) {
            if (n.id == id) {
                return n;
            }
        }
        return null;
    }

    class N extends Node {
        long id;
        N next;

        public N(long id, double lon, double lat, N n) {
            super(lon, lat);
            this.id = id;
            this.next = n;
        }

    }
}

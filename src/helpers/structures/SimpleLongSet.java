package helpers.structures;

import java.util.Iterator;

public class SimpleLongSet implements Iterable<Long> {
    private long[] set;
    private int size;

    public SimpleLongSet() {
        set = new long[1];
        size = 0;
    }

    public void add(long value) {
        // Bail out if value already exists
        if (contains(value)) {
            return;
        }

        // Resize array if filled
        if (size == set.length) {
            resize();
        }

        set[size++] = value;
    }

    public void resize() {
        long[] tempSet = new long[size * 2];

        for (int i = 0; i < set.length; i++) {
            tempSet[i] = set[i];
        }

        set = tempSet;
    }

    public long[] getSet() {
        return set;
    }

    public boolean contains(long value) {
        for (long elm : set) {
            if (elm == value) {
                return true;
            }
        }

        return false;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<Long> iterator() {
        Iterator<Long> it = new Iterator<>() {
            private int currIndex = 0;

            @Override
            public boolean hasNext() {
                return currIndex < size;
            }

            @Override
            public Long next() {
                return set[currIndex++];
            }
        };

        return it;
    }
}

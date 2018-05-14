package helpers.structures;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;

/**
 * Naive and simple set of long's.
 * This is used in order to avoid using java.util.HashSet which has excessive memory usage, since it utilizes
 * HashMaps and more under the hood..
 *
 * NB: This implementation isn't very efficient, with most methods being O(N) (where N is the size of the array),
 * however this structure is usually only used to contain around at max 20 elements, and therefore isn't an issue.
 */
public class SimpleLongSet implements Iterable<Long>, Externalizable {
    private long[] set;
    private int size;

    public SimpleLongSet() {
        set = new long[1];
        size = 0;
    }

    /**
     * Add to the set
     * @param value value of item to add
     */
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

    /**
     * Helper that doubles the size of the array if the old array reached a point where it couldn't contain any more
     * elements.
     */
    public void resize() {
        long[] tempSet = new long[size * 2];

        for (int i = 0; i < set.length; i++) {
            tempSet[i] = set[i];
        }

        set = tempSet;
    }

    /**
     * Get long set
     * @return set
     */
    public long[] getSet() {
        return set;
    }

    /**
     * Check if the set contains an item with the given value
     * @param value to check for
     * @return true if value is en set; else false
     */
    public boolean contains(long value) {
        for (long elm : set) {
            if (elm == value) {
                return true;
            }
        }

        return false;
    }

    /**
     * get size of set
     * @return size
     */
    public int size() {
        return size;
    }

    /**
     * Get iterator over set
     * @return iterator
     */
    @Override
    public Iterator<Long> iterator() {
        Iterator<Long> it = new Iterator<Long>() {
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        out.writeObject(set);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        size = in.readInt();
        set = (long[]) in.readObject();
    }
}

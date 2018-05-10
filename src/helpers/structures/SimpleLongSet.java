package helpers.structures;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;

public class SimpleLongSet implements Iterable<Long>, Externalizable {
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

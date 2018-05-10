package helpers.structures;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleLongSetTest {
    @Test
    public void add() {
        SimpleLongSet set = new SimpleLongSet();
        set.add(1023);

        assertEquals(set.getSet()[0], 1023);
    }

    @Test
    public void resize() {
        SimpleLongSet set = new SimpleLongSet();


        set.add(1);
        assertEquals(set.getSet().length, 1);

        set.add(2);
        assertEquals(set.getSet().length, 2);

        set.add(3);
        set.add(4);
        assertEquals(set.getSet().length, 4);

        set.add(5);
        assertEquals(set.getSet().length, 8);
    }

    @Test
    public void contains() {
        SimpleLongSet set = new SimpleLongSet();

        set.add(1);

        assertTrue(set.contains(1));
        assertFalse(set.contains(2));
    }

    @Test
    public void size() {
        SimpleLongSet set = new SimpleLongSet();

        assertEquals(set.size(), 0);

        set.add(1);
        set.add(2);
        set.add(3);

        assertEquals(set.size(), 3);
    }
}
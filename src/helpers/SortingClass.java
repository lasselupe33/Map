package helpers;

import model.osm.OSMWay;

import java.util.Random;


public class SortingClass {

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    /*public static void sort(OSMWay[] a, boolean even) {
        shuffleArray(a);
        sort(a, 0, a.length - 1, even);
        assert isSorted(a);
    }*/

    // quicksort the subarray from a[lo] to a[hi]
    public static void sort(OSMWay[] a, int lo, int hi, boolean even) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi, even);
        sort(a, lo, j-1, even);
        sort(a, j+1, hi, even);
        //assert isSorted(a, lo, hi);
    }

    // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
    // and return the index j.
    private static int partition(OSMWay[] a, int lo, int hi, boolean even) {
        int j;
        if(even) j = partLat(a, lo, hi);
        else j = partLon(a, lo, hi);
        // put partitioning item v at a[j]
        exch(a, lo, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }

    private static int partLon(OSMWay[] a, int lo, int hi){
        int i = lo;
        int j = hi + 1;
        OSMWay v = a[lo];
        while (true) {

            // find item on lo to swap
            while (lessLon(a[++i], v)) {
                if (i == hi) break;
            }

            // find item on hi to swap
            while (lessLon(v, a[--j])) {
                if (j == lo) break;      // redundant since a[lo] acts as sentinel
            }

            // check if pointers cross
            if (i >= j) break;

            exch(a, i, j);
        }
        return j;
    }

    private static int partLat(OSMWay[] a, int lo, int hi){
        int i = lo;
        int j = hi + 1;
        OSMWay v = a[lo];
        while (true) {

            // find item on lo to swap
            while (lessLat(a[++i], v)) {
                if (i == hi) break;
            }

            // find item on hi to swap
            while (lessLat(v, a[--j])) {
                if (j == lo) break;      // redundant since a[lo] acts as sentinel
            }

            // check if pointers cross
            if (i >= j) break;

            exch(a, i, j);
        }
        return j;
    }

    /**
     * Rearranges the array so that {@code a[k]} contains the kth smallest key;
     * {@code a[0]} through {@code a[k-1]} are less than (or equal to) {@code a[k]}; and
     * {@code a[k+1]} through {@code a[n-1]} are greater than (or equal to) {@code a[k]}.
     *
     * @param  a the array
     * @param  k the rank of the key
     * @return the key of rank {@code k}
     * @throws IllegalArgumentException unless {@code 0 <= k < a.length}
     */
    public static OSMWay select(OSMWay[] a, int k, boolean even) {
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException("index is not between 0 and " + a.length + ": " + k);
        }
        shuffleArray(a);
        int lo = 0, hi = a.length - 1;
        while (hi > lo) {
            int i = partition(a, lo, hi, even);
            if      (i > k) hi = i - 1;
            else if (i < k) lo = i + 1;
            else return a[i];
        }
        return a[lo];
    }



    private static void shuffleArray(OSMWay[] array)
    {
        int index;
        OSMWay temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean lessLon(OSMWay v, OSMWay w) {
        if (v == w) return false;   // optimization when reference equals
        return v.getAvgLon() < w.getAvgLon();
    }

    private static boolean lessLat(OSMWay v, OSMWay w) {
        if (v.getAvgLat() == w.getAvgLat()) return false;   // optimization when reference equals
        return v.getAvgLat() < w.getAvgLat();
    }

    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    /*private static boolean isSorted(OSMWay[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(OSMWay[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }*/


}

package helpers;

import model.MapElements;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class SortingClass {


    public static List<MapElements> sort(List<MapElements> list, boolean even) {
        MapElements[] a = new MapElements[list.size()];
        a = list.toArray(a);
        shuffleArray(a);
        sort(a, 0, a.length - 1, even);
        return Arrays.asList(a);
    }

    // quicksort the subarray from a[lo] to a[hi]
    public static void sort(MapElements[] a, int lo, int hi, boolean even) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi, even);
        sort(a, lo, j-1, even);
        sort(a, j+1, hi, even);
        //assert isSorted(a, lo, hi);
    }

    // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
    // and return the index j.
    private static int partition(MapElements[] a, int lo, int hi, boolean even) {
        int i = lo;
        int j = hi + 1;
        MapElements v = a[lo];
        while (true) {

            // find item on lo to swap
            while (less(a[++i], v, even)) {
                if (i == hi) break;
            }

            // find item on hi to swap
            while (less(v, a[--j], even)) {
                if (j == lo) break;      // redundant since a[lo] acts as sentinel
            }

            // check if pointers cross
            if (i >= j) break;

            exch(a, i, j);
        }

        // put partitioning item v at a[j]
        exch(a, lo, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }



    private static void shuffleArray(MapElements[] array)
    {
        int index;
        MapElements temp;
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
    private static boolean less(MapElements v, MapElements w, boolean even) {
        if(even) {
            if (v == w) return false;   // optimization when reference equals
            return v.getX() < w.getX();
        } else {
            if (v == w) return false;   // optimization when reference equals
            return v.getY() < w.getY();
        }
    }

    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }




}

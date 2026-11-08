package city;

import java.util.List;

public interface StringSearch {
    /** Start indices of all (also overlapping) occurrences of p in t, increasing. */
    List<Integer> search(String t, String p);

    /** Counter: number of character comparisons t[i] vs p[j] in the last search() call. */
    long comparisons();

    String name();

    static void check(String t, String p) {
        if (p == null || t == null) throw new IllegalArgumentException("null argument");
        if (p.isEmpty()) throw new IllegalArgumentException("empty pattern");
    }
}

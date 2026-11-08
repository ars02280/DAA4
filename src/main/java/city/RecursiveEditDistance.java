package city;

/**
 * Version A: plain recursion, no memory.
 * State:      d(i, j) = edit distance of the first i chars of a and the first j chars of b.
 * Transition: if a[i-1] == b[j-1] then d(i-1, j-1)
 *             else 1 + min(d(i-1, j) [delete], d(i, j-1) [insert], d(i-1, j-1) [replace]).
 * Base case:  d(i, 0) = i and d(0, j) = j.
 */
public class RecursiveEditDistance implements EditDistance {
    private long calls;

    @Override
    public int distance(String a, String b) {
        calls = 0;
        return d(a, b, a.length(), b.length());
    }

    private int d(String a, String b, int i, int j) {
        calls++; // counter: one unit of work = one call (one subproblem evaluation)
        if (i == 0) return j;
        if (j == 0) return i;
        if (a.charAt(i - 1) == b.charAt(j - 1)) return d(a, b, i - 1, j - 1);
        return 1 + Math.min(d(a, b, i - 1, j), Math.min(d(a, b, i, j - 1), d(a, b, i - 1, j - 1)));
    }

    @Override public long ops() { return calls; }
    @Override public long memCells() { return 0; } // only the call stack
    @Override public String name() { return "recursive"; }
}

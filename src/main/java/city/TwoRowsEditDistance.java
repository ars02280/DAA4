package city;

/**
 * Version C: same recurrence as B, but only the previous and the current row are kept.
 * State:      prev[j] / cur[j] = edit distance of the first i-1 / i chars of a and the first j chars of b.
 * Transition: cur[j] = prev[j-1] if chars equal, else 1 + min(prev[j], cur[j-1], prev[j-1]).
 * Base case:  row 0 is prev[j] = j; every new row starts with cur[0] = i.
 * Extra memory O(m) instead of O(n*m).
 */
public class TwoRowsEditDistance implements EditDistance {
    private long cells, mem;

    @Override
    public int distance(String a, String b) {
        int n = a.length(), m = b.length();
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        mem = 2L * (m + 1);
        cells = 0;
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            for (int j = 1; j <= m; j++) {
                cells++; // counter: one cell computed (same unit as the table version)
                if (a.charAt(i - 1) == b.charAt(j - 1)) cur[j] = prev[j - 1];
                else cur[j] = 1 + Math.min(prev[j], Math.min(cur[j - 1], prev[j - 1]));
            }
            int[] tmp = prev; prev = cur; cur = tmp;
        }
        return prev[m];
    }

    @Override public long ops() { return cells; }
    @Override public long memCells() { return mem; }
    @Override public String name() { return "two-rows"; }
}

package city;

/**
 * Version B: bottom-up table of (n+1) x (m+1) cells, time O(n*m), memory O(n*m).
 * State:      t[i][j] = edit distance of the first i chars of a and the first j chars of b.
 * Transition: t[i][j] = t[i-1][j-1] if chars equal, else 1 + min(t[i-1][j], t[i][j-1], t[i-1][j-1]).
 * Base case:  t[i][0] = i and t[0][j] = j.
 */
public class TableEditDistance implements EditDistance {
    private long cells, mem;

    @Override
    public int distance(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] t = new int[n + 1][m + 1];
        mem = (long) (n + 1) * (m + 1);
        cells = 0;
        for (int i = 0; i <= n; i++) t[i][0] = i;
        for (int j = 0; j <= m; j++) t[0][j] = j;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                cells++; // counter: one table cell computed
                if (a.charAt(i - 1) == b.charAt(j - 1)) t[i][j] = t[i - 1][j - 1];
                else t[i][j] = 1 + Math.min(t[i - 1][j], Math.min(t[i][j - 1], t[i - 1][j - 1]));
            }
        }
        return t[n][m];
    }

    @Override public long ops() { return cells; }
    @Override public long memCells() { return mem; }
    @Override public String name() { return "table"; }
}

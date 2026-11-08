package city;

/** One interface for all Edit Distance versions, so the experiment is a single loop. */
public interface EditDistance {
    int distance(String a, String b);

    /** Counter: number of subproblem evaluations (calls or table cells) in the last distance() call. */
    long ops();

    /** Counter: number of int cells allocated for DP storage in the last distance() call. */
    long memCells();

    String name();
}

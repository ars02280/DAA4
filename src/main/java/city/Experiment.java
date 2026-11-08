package city;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** Runs the whole experiment. Usage: java city.Experiment [results/results.csv]. Fixed seed -> same counters every run. */
public class Experiment {
    static final long SEED = 42;
    static final String ALPHA = "ACGT";

    // ---------- generators ----------
    static String random(Random r, int n, int alphabet) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(ALPHA.charAt(r.nextInt(alphabet)));
        return sb.toString();
    }

    static String repeat(char c, int n) {
        char[] a = new char[n];
        Arrays.fill(a, c);
        return new String(a);
    }

    static long median(long[] v) {
        long[] s = v.clone();
        Arrays.sort(s);
        return s[s.length / 2];
    }

    public static void main(String[] args) throws IOException {
        Path out = Path.of(args.length > 0 ? args[0] : "results/results.csv");
        Files.createDirectories(out.getParent());
        try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(out))) {
            w.println("family,algorithm,input,n,m,time_ms,ops,mem_cells,result");
            editDistance(w);
            search(w);
        }
        System.out.println("written " + out);
    }

    // ---------- Edit Distance ----------
    static void editDistance(PrintWriter w) {
        List<EditDistance> all = List.of(new RecursiveEditDistance(), new TableEditDistance(), new TwoRowsEditDistance());
        // recursive is exponential on 'disjoint', so it only gets tiny sizes; DP versions get large ones
        int[] smallSizes = {3, 6, 9, 12};
        int[] bigSizes = {250, 500, 1000, 2000, 4000};
        for (String input : new String[]{"random", "identical", "disjoint"}) {
            for (int n : smallSizes) run(w, all, input, n, 3);
            for (int n : bigSizes) run(w, all.subList(1, 3), input, n, 5); // table and two-rows only
        }
    }

    static String[] pair(String input, int n) {
        Random r = new Random(SEED + n);
        switch (input) {
            case "random":    return new String[]{random(r, n, 4), random(r, n, 4)};
            case "identical": String s = random(r, n, 4); return new String[]{s, s};
            case "disjoint":  return new String[]{repeat('A', n), repeat('B', n)}; // no equal chars: worst case for recursion
            default: throw new IllegalArgumentException(input);
        }
    }

    static void run(PrintWriter w, List<EditDistance> algs, String input, int n, int reps) {
        String[] ab = pair(input, n);
        int expected = -1;
        for (EditDistance ed : algs) {
            long[] t = new long[reps];
            int res = 0;
            for (int k = 0; k < reps; k++) {
                long t0 = System.nanoTime();
                res = ed.distance(ab[0], ab[1]);
                t[k] = System.nanoTime() - t0;
            }
            if (expected == -1) expected = res;
            else if (expected != res) throw new IllegalStateException("versions disagree on " + input + " n=" + n);
            w.printf("edit,%s,%s,%d,%d,%.4f,%d,%d,%d%n", ed.name(), input, n, n, median(t) / 1e6, ed.ops(), ed.memCells(), res);
        }
        w.flush();
    }

    // ---------- String search ----------
    static void search(PrintWriter w) {
        int m = 100;
        int[] sizes = {20_000, 40_000, 80_000, 160_000, 320_000};
        for (String input : new String[]{"random", "worst-naive", "all-match"}) {
            for (int n : sizes) {
                Random r = new Random(SEED + n);
                String text, pat;
                switch (input) {
                    case "random":      text = random(r, n, 4); pat = random(r, m, 4); break;
                    case "worst-naive": text = repeat('A', n); pat = repeat('A', m - 1) + "C"; break; // almost matches at every shift
                    default:            text = repeat('A', n); pat = repeat('A', m); break;          // match at every shift
                }
                int expectedCount = -1;
                for (StringSearch s : List.of(new NaiveSearch(), new KmpSearch())) {
                    long[] t = new long[5];
                    int count = 0;
                    for (int k = 0; k < 5; k++) {
                        long t0 = System.nanoTime();
                        count = s.search(text, pat).size();
                        t[k] = System.nanoTime() - t0;
                    }
                    if (expectedCount == -1) expectedCount = count;
                    else if (expectedCount != count) throw new IllegalStateException("search disagrees " + input);
                    w.printf("search,%s,%s,%d,%d,%.4f,%d,0,%d%n", s.name(), input, n, m, median(t) / 1e6, s.comparisons(), count);
                }
            }
        }
        w.flush();
    }
}

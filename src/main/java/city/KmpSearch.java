package city;

import java.util.ArrayList;
import java.util.List;

public class KmpSearch implements StringSearch {
    private long cmp;

    /** lps[i] = length of the longest proper prefix of p[0..i] that is also its suffix. */
    public static int[] lps(String p) {
        int[] lps = new int[p.length()];
        int len = 0;
        for (int i = 1; i < p.length(); ) {
            if (p.charAt(i) == p.charAt(len)) lps[i++] = ++len;
            else if (len > 0) len = lps[len - 1];
            else lps[i++] = 0;
        }
        return lps;
    }

    @Override
    public List<Integer> search(String t, String p) {
        StringSearch.check(t, p);
        cmp = 0;
        List<Integer> res = new ArrayList<>();
        int n = t.length(), m = p.length();
        if (m > n) return res;
        int[] lps = lps(p);
        int i = 0, j = 0; // i never moves backwards
        while (i < n) {
            cmp++; // counter: one character comparison (search phase only; LPS build is <= 2m extra)
            if (t.charAt(i) == p.charAt(j)) {
                i++; j++;
                if (j == m) { res.add(i - m); j = lps[j - 1]; }
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return res;
    }

    @Override public long comparisons() { return cmp; }
    @Override public String name() { return "kmp"; }
}

package city;

import java.util.ArrayList;
import java.util.List;

public class NaiveSearch implements StringSearch {
    private long cmp;

    @Override
    public List<Integer> search(String t, String p) {
        StringSearch.check(t, p);
        cmp = 0;
        List<Integer> res = new ArrayList<>();
        int n = t.length(), m = p.length();
        for (int s = 0; s + m <= n; s++) {
            int j = 0;
            while (j < m) {
                cmp++; // counter: one character comparison
                if (t.charAt(s + j) != p.charAt(j)) break;
                j++;
            }
            if (j == m) res.add(s);
        }
        return res;
    }

    @Override public long comparisons() { return cmp; }
    @Override public String name() { return "naive"; }
}

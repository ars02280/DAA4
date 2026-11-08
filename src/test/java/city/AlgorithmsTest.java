package city;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class AlgorithmsTest {
    private final List<EditDistance> eds = List.of(new RecursiveEditDistance(), new TableEditDistance(), new TwoRowsEditDistance());
    private final List<StringSearch> searches = List.of(new NaiveSearch(), new KmpSearch());

    @Test void editReferenceValues() {
        for (EditDistance ed : eds) {
            assertEquals(3, ed.distance("kitten", "sitting"), ed.name());
            assertEquals(2, ed.distance("flaw", "lawn"), ed.name());
            assertEquals(5, ed.distance("intention", "execution"), ed.name());
            assertEquals(3, ed.distance("", "abc"), ed.name());
            assertEquals(3, ed.distance("abc", ""), ed.name());
            assertEquals(0, ed.distance("abc", "abc"), ed.name());
            assertEquals(0, ed.distance("", ""), ed.name());
        }
    }

    @Test void editVersionsAgreeOnRandomStrings() {
        Random r = new Random(1);
        for (int k = 0; k < 100; k++) {
            String a = Experiment.random(r, r.nextInt(8), 3), b = Experiment.random(r, r.nextInt(8), 3);
            int expected = eds.get(0).distance(a, b);
            assertEquals(expected, eds.get(1).distance(a, b));
            assertEquals(expected, eds.get(2).distance(a, b));
        }
    }

    @Test void lpsReferenceArrays() {
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3}, KmpSearch.lps("AAACAAAA"));
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4}, KmpSearch.lps("ABABCABAB"));
    }

    @Test void searchReferenceMatches() {
        for (StringSearch s : searches) {
            assertEquals(List.of(10), s.search("ABABDABACDABABCABAB", "ABABCABAB"), s.name());
            assertEquals(List.of(0, 1, 2, 3), s.search("AAAAA", "AA"), s.name());
            assertEquals(List.of(0, 2, 4), s.search("ABABABA", "ABA"), s.name());
            assertEquals(List.of(), s.search("ABC", "XYZ"), s.name());
        }
    }

    @Test void naiveAndKmpAgreeOnRandomTexts() {
        Random r = new Random(2);
        for (int k = 0; k < 100; k++) {
            String t = Experiment.random(r, 1 + r.nextInt(60), 2), p = Experiment.random(r, 1 + r.nextInt(5), 2);
            assertEquals(searches.get(0).search(t, p), searches.get(1).search(t, p));
        }
    }

    @Test void searchEdgeCases() {
        for (StringSearch s : searches) {
            assertThrows(IllegalArgumentException.class, () -> s.search("abc", ""));
            assertEquals(List.of(), s.search("ab", "abc"));
        }
    }
}

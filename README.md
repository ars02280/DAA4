# City Search & Spell-Check

Edit Distance (recursive, table, two rows) and string search (Naive, KMP) with counters, a reproducible experiment, plots and a report.

## Layout
- `src/main/java/city` – algorithms, counters (inside each class), generator + `Experiment`
- `src/test/java/city` – JUnit 5 tests
- `results/results.csv`, `results/plots/*.png` – experiment output
- `plots.py` – draws the plots from the CSV (needs Python 3 + matplotlib)
- `REPORT.md` – design card, plots, prediction vs result, short answers

## Build and test (Maven, Java 17+)
```
mvn test
```

## Run the experiment (fixed seed 42, same counters every time)
```
mvn -q compile
java -cp target/classes city.Experiment results/results.csv
python3 plots.py
```
Without Maven: `javac -d out src/main/java/city/*.java && java -cp out city.Experiment`.
The experiment takes about 10 seconds.

## AI-use acknowledgement
The code, experiment and report draft were produced with Claude (Anthropic) and the standard textbook algorithms
(Wagner–Fischer edit distance, Knuth–Morris–Pratt). The numbers in the report come from `results/results.csv`.
Check the code and conclusions yourself before submitting.

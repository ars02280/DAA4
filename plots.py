import csv, collections
import matplotlib; matplotlib.use("Agg")
import matplotlib.pyplot as plt

rows = list(csv.DictReader(open("results/results.csv")))
def series(fam, alg, inp, key):
    r = [x for x in rows if x["family"]==fam and x["algorithm"]==alg and x["input"]==inp]
    return [int(x["n"]) for x in r], [float(x[key]) for x in r]

def save(fig, name, note):
    fig.text(0.5, 0.005, note, ha="center", fontsize=8, style="italic")
    fig.tight_layout(rect=(0,0.04,1,1)); fig.savefig("results/plots/"+name, dpi=150); plt.close(fig)

# 1. recursive: calls vs n, log y
fig, ax = plt.subplots(figsize=(6,4.2))
for inp in ["disjoint","random","identical"]:
    n, y = series("edit","recursive",inp,"ops"); ax.plot(n, y, "o-", label=inp)
ax.set_yscale("log"); ax.set_xlabel("string length n (characters)"); ax.set_ylabel("recursive calls (log scale)")
ax.set_title("Recursive Edit Distance: calls vs n"); ax.legend(title="input")
save(fig, "1_recursive_calls.png", "Calls explode exponentially on 'disjoint' but stay linear on 'identical'.")

# 2. table vs two rows: time vs n (random), and memory
fig, (a1, a2) = plt.subplots(1, 2, figsize=(10,4.2))
for alg in ["table","two-rows"]:
    n, y = series("edit",alg,"random","time_ms"); a1.plot(n, y, "o-", label=alg)
    n, y = series("edit",alg,"random","mem_cells"); a2.plot(n, y, "o-", label=alg)
a1.set_xlabel("string length n (characters)"); a1.set_ylabel("median time (ms)"); a1.set_title("DP time vs n (random input)"); a1.legend()
a2.set_yscale("log"); a2.set_xlabel("string length n (characters)"); a2.set_ylabel("int cells allocated (log scale)"); a2.set_title("DP memory vs n"); a2.legend()
save(fig, "2_dp_time_memory.png", "Both DP versions do the same work (n*m cells); two rows needs ~n/2 times less memory.")

# 3. search comparisons per text char (ratio), worst-naive input
fig, ax = plt.subplots(figsize=(6,4.2))
for alg in ["naive","kmp"]:
    for inp, st in [("worst-naive","-"),("random","--")]:
        n, y = series("search",alg,inp,"ops"); ax.plot(n, [c/k for c,k in zip(y,n)], "o"+st, label=f"{alg}, {inp}")
ax.set_yscale("log"); ax.set_xlabel("text length n (characters)"); ax.set_ylabel("comparisons per text character (log scale)")
ax.set_title("String search: comparisons / n (pattern length m=100)"); ax.legend(fontsize=8)
save(fig, "3_search_ratio.png", "Naive costs ~m per character on the worst input; KMP stays below 2 on every input.")

# 4. search time, worst input
fig, ax = plt.subplots(figsize=(6,4.2))
for alg in ["naive","kmp"]:
    n, y = series("search",alg,"worst-naive","time_ms"); ax.plot(n, y, "o-", label=alg)
ax.set_xlabel("text length n (characters)"); ax.set_ylabel("median time (ms)"); ax.set_title("Search time on worst-naive input"); ax.legend()
save(fig, "4_search_time.png", "Wall-clock time on the worst input follows the comparison counts: naive grows ~m times faster.")

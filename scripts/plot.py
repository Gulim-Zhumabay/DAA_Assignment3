import csv
import os
from collections import defaultdict
import matplotlib.pyplot as plt

data = defaultdict(list)
with open("results/tables/results.csv") as f:
    for row in csv.DictReader(f):
        data[row["algorithm"]].append(
            (int(row["v"]), int(row["e"]), int(row["avg_time_ns"]) / 1000))


def plot(index, xlabel, filename):
    plt.figure(figsize=(8, 5))
    for algo, points in data.items():
        xs = [p[index] for p in points]
        ys = [p[2] for p in points]
        plt.plot(xs, ys, marker="o", label=algo)
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel(xlabel)
    plt.ylabel("time (microseconds)")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.savefig(filename, dpi=150, bbox_inches="tight")
    plt.close()


os.makedirs("results/plots", exist_ok=True)
plot(0, "vertices (V)", "results/plots/time_vs_vertices.png")
plot(1, "edges (E)", "results/plots/time_vs_edges.png")
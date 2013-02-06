using System;
using System.Collections.Generic;
using System.Text;

class ShoppingPlan {
    public string Process(string input) {
        return "";
    }

    double MAX = 1e12;
    bool[] perish;
    int N, M, NN;
    double[, ,] dMin;
    int[,] storeGoods;
    double[,] dis;
    int igas;
    public string Process2(string[] items, string[] stores, int gas) {
        N = items.Length;
        M = stores.Length;
        NN = 1 << N;
        igas = gas;
        perish = new bool[N];
        for (int i = 0; i < N; i++) {
            if (items[i].EndsWith("!")) {
                perish[i] = true;
                items[i] = items[i].Substring(0, items[i].Length - 1);
            }
        }

        dMin = new double[NN, M + 1, 2];
        for (int i = 0; i < NN; i++) {
            for (int j = 0; j < M + 1; j++) {
                for (int k = 0; k < 2; k++) {
                    dMin[i, j, k] = -1;

                }
            }
        }

        dMin[NN - 1, M, 0] = 0;
        dMin[NN - 1, M, 1] = 0;

        Dictionary<string, int> oGoods = new Dictionary<string, int>();
        for (int i = 0; i < N; i++) {
            oGoods.Add(items[i], i);
        }

        storeGoods = new int[M, N];

        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                storeGoods[i, j] = 999999999;
            }
        }

        int[] x = new int[M];
        int[] y = new int[M];

        for (int i = 0; i < M; i++) {
            string[] s = stores[i].Split(' ');
            x[i] = int.Parse(s[0]);
            y[i] = int.Parse(s[1]);
            for (int j = 2; j < s.Length; j++) {
                string[] s2 = s[j].Split(':');
                storeGoods[i, oGoods[s2[0]]] = int.Parse(s2[1]);
            }
        }

        dis = new double[M + 1, M + 1];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                dis[i, j] = ddis(x[i], y[i], x[j], y[j]);
            }
            dis[i, M] = ddis(x[i], y[i], 0, 0);
            dis[M, i] = dis[i, M];
        }

        double dRet = findMin(0, M, 0);
        return string.Format("{0:0.0000000}", dRet);

    }

    public double ddis(int x1, int y1, int x2, int y2) {
        return Math.Sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private double findMin(int goods, int loc, int p) {
        if (dMin[goods, loc, p] >= 0) return dMin[goods, loc, p];
        dMin[goods, loc, p] = MAX;

        double best = MAX;
        for (int i = 0; i < N; i++) if ((1 << i & goods) == 0) {
                for (int j = 0; j < M; j++) {
                    if (p == 0 || j == loc) {
                        double travelCost = 0;
                        if (j != loc) travelCost = dis[j, loc] * igas;
                        double goodCost = storeGoods[j, i];
                        double totCost = travelCost + goodCost;
                        int newp = p | (perish[i] ? 1 : 0);
                        best = Math.Min(best, findMin(goods + (1 << i), j, newp) + totCost);
                    }
                }
            }
        if (loc != M) {
            best = Math.Min(best, findMin(goods, M, 0) + dis[loc, M] * igas);
        }
        dMin[goods, loc, p] = best;
        return best;
    }

    public string[] ProcessMulti(string input) {
        return new string[] { };
    }
}

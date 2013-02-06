#include <cstdio>
#include <cstdlib>
#include <vector>
#include <string>
#include <map>
#include <algorithm>
#include <queue>
#include <cmath>

using namespace std;

vector<string> stokens(string s, string sep = " \n\r\t") {
	vector<string> res;
	int start, end = 0;
	while ((start = s.find_first_not_of(sep, end)) != string::npos) {
		end = s.find_first_of(sep, start);
		res.push_back(s.substr(start, end-start));
	}
	return res;
}


class Store {
public:
Store() {}
Store(string s, int N, map<string, int> items) {
	price.resize(N, -1);
	vector<string> st = stokens(s, " :\n");
	x = atoi(st[0].c_str());
	y = atoi(st[1].c_str());
	stk.resize(st.size()/2-1);
	int j = 0;
	for (int i = 2; i < st.size(); i += 2) {
		price[items[st[i]]] = atoi(st[i+1].c_str());
		stk[j++] = items[st[i]];
	}
}
double dist(int xp, int yp) const { return sqrt((x-xp)*(x-xp) + (y-yp)*(y-yp)); }
double dist(const Store &s) const { return sqrt((x-s.x)*(x-s.x) + (y-s.y)*(y-s.y)); }
int x, y;
vector<int> price, stk;
};


class Stock {
public:
Stock() {};
Stock(double sp, int m, int l) {
	spent = sp;
	mask = m;
	loc = l;
	bct = 0;
	for (int i = 0; i < 16; i++) if ((m>>i)&1) bct++; 
}
Stock(double sp, int m, int l, int b, int c) {
	spent = sp;
	mask = m;
	loc = l;
	bct = b;
	cf = c;
}
bool operator<(const Stock &s) const {
	return spent > s.spent;
}
bool operator>(const Stock &s) const {
	return spent < s.spent;
}

double spent;
int bct, loc, cf;
int mask;
};

double bcost[51][1<<15];

#define EPS 1e-10

void doit(int cn, int gp, const vector<int> &per, vector<Store> &sts) {
	int N = per.size(), S = sts.size();
	int all = (1<<N)-1;

	for (int s = 0; s < S; s++) for (int n = 0; n < 1<<N; n++)
		bcost[s][n] = 1e100;

	double ub = 0.0;
	vector<double> chp(N, 1e100);
	for (int i = 1; i < S; i++) {
		double gc = sts[i].dist(0, 0)*2*gp;
		for (int j = 0; j < sts[i].stk.size(); j++) {
			int p = sts[i].stk[j];
			double tc = gc+sts[i].price[p];
			if (tc < chp[p]) chp[p] = tc;
		}
	}
	for (int i = 0; i < N; i++) ub += chp[i];
	bcost[0][all] = ub;

	for (int i = 1; i < S; i++) {
		for (int j = 0; j < sts[i].stk.size(); j++) {
			int p = sts[i].stk[j];
			if (sts[i].price[p] > chp[p]) {
				sts[i].stk.erase(sts[i].stk.begin()+j--);
			}
		}
	}

	bcost[0][0] = 0.0;
	priority_queue<Stock> pq;
	pq.push(Stock(0.0, 0, 0, 0, -1));

	while (!pq.empty()) {
		Stock s = pq.top();
		pq.pop();

//printf("Examining Stock(%.7lf, %d, %d, %d, %d)\n", s.spent, s.mask, s.loc, s.bct, s.cf);

		if (bcost[0][all] <= s.spent) continue;
		if (bcost[s.loc][s.mask] < s.spent) continue;
		if (s.loc) {
			const Store &T = sts[s.loc];
			vector<int> stk;
			for (int i = 0; i < T.stk.size(); i++)
				if (!((s.mask>>T.stk[i])&1))
					stk.push_back(T.stk[i]);

			if (stk.empty()) continue;
			int n = 1<<stk.size();
			for (int m = 1; m < n; m++) {
				double cost = s.spent;
				int nm = s.mask, pr = 0, nct = s.bct;
				for (int i = 0; m>>i; i++) if (((m>>i)&1)) {
					cost += T.price[stk[i]];
					nm |= 1<<stk[i];
					nct++;
					if (per[stk[i]]) pr++;
				}
				if (bcost[s.loc][nm] < cost+EPS) continue;

				if (pr) {
					double gc = T.dist(0, 0)*gp + cost;
					if (bcost[0][nm] > gc+EPS) {
						bcost[0][nm] = gc;
						pq.push(Stock(gc, nm, 0, nct, s.loc));
//printf(" Push Stock(%.7lf, %d, %d, %d, %d)\n", gc, nm, 0, nct, s.loc);
					}
				} else {
					bcost[s.loc][nm] = cost;
					for (int i = 0; i < S; i++) {
						if (s.loc == i) continue;
						double gc = sts[i].dist(T)*gp +
								cost;
						if (bcost[i][nm] > gc+EPS) {
							bcost[i][nm] = gc;
							pq.push(Stock(gc, nm,
								i, nct, s.loc));
//printf(" Push Stock(%.7lf, %d, %d, %d, %d)\n", gc, nm, i, nct, s.loc);
						}
					}
				}
			}
		} else {
			for (int i = 1; i < S; i++) {
				double gc = sts[i].dist(0, 0)*gp + s.spent;
				if (bcost[i][s.mask] > gc+EPS) {
					bcost[i][s.mask] = gc;
					pq.push(Stock(gc, s.mask, i, s.bct, 0));
//printf(" Push Stock(%.7lf, %d, %d, %d, %d)\n", gc, s.mask, i, s.bct, 0);
				}
			}
		}
	}


	printf("Case #%d: %.7lf\n", cn, bcost[0][all]);
}

int main() {
	int N;
	scanf("%d", &N);
	for (int i = 0; i < N; i++) {
		int ni, ns, pg;
		scanf(" %d %d %d ", &ni, &ns, &pg);
		map<string, int> items;
		vector<int> perish(ni, 0);
		char sz[10000];
		for (int j = 0; j < ni; j++) {
			scanf(" %9999s ", sz);
			string s(sz);
			if (s[s.size()-1] == '!') {
				perish[j] = 1;
				s.resize(s.size()-1);
			}
			items[s] = j;
		}

		vector<Store> sts(ns+1);
		sts[0] = Store("0 0", ni, items);
		for (int j = 1; j <= ns; j++) {
			fgets(sz, 9999, stdin);
			sts[j] = Store(sz, ni, items);
		}

		doit(i+1, pg, perish, sts);
	}
	return 0;
}


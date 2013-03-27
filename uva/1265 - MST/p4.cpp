#include <map>
#include <set>
#include <list>
#include <cmath>
#include <queue>
#include <stack>
#include <bitset>
#include <vector>
#include <cstdio>
#include <string>
#include <sstream>
#include <cstdlib>
#include <cstring>
#include <iostream>
#include <algorithm>
using namespace std;
#define PB push_back
#define MP make_pair
#define SZ(v) ((int)(v).size())
#define abs(x) ((x) > 0 ? (x) : -(x))
typedef long long LL;

const int maxn = 5005;
const int inf = 0x3f3f3f3f;
struct Edge {
	int a, b, c;
	bool operator < (const Edge &pt) const {
		return c > pt.c;
	}
}es[12500005];
int n, m;
int p[maxn], rank[maxn];
int A[maxn][maxn], B[maxn][maxn];

int find(int x) {
	return p[x] == x ? x : p[x] = find(p[x]);
}

int main(int argc, char const *argv[])
{
	int Test;
	scanf("%d", &Test);
	while (Test--) {
		scanf("%d%d", &n, &m);
		for (int i = 1; i <= n; i++) {
			p[i] = i; rank[i] = 1;
			for (int j = 1; j <= n; j++) {
				A[i][j] = inf;
				B[i][j] = 0;
			}
		}

		for (int i = 0; i < m; i++) {
			int a, b, c;
			scanf("%d%d%d", &a, &b, &c);
			es[i].a = a; es[i].b = b; es[i].c = c;
			A[a][b] = A[b][a] = B[a][b] = B[b][a] = c;
		}

		sort(es, es + m);
		int ret = 0;
		for (int i = 0; i < m; i++) {
			int a = es[i].a, b = es[i].b;
			int pa = find(a), pb = find(b);
			if (pa != pb) {
				p[pb] = pa;
				rank[pa] += rank[pb];
				int inside = inf, outside = 0;
				for (int j = 1; j <= n; j++) {
					A[pa][j] = min(A[pa][j], A[pb][j]);
					B[pa][j] = max(B[pa][j], B[pb][j]);
					if (pa == find(j)) {
						inside = min(inside, A[pa][j]);
					} else {
						outside = max(outside, B[pa][j]);
					}
				}
				if (inside > outside) ret += rank[pa];
			}
		}
		printf("%d\n", ret);
	}
	return 0;
}
#include <string>
#include <vector>
#include <map>
#include <cstdlib>
#include <cstring>
#include <cassert>
#include <set>
#include <iostream>
#include <sstream>
#include <cstddef>
#include <algorithm>
#include <utility>
#include <iterator>
#include <numeric>
#include <list>
#include <complex>
#include <cstdio>

using namespace std;

typedef vector<int> vi;
typedef vector<string> vs;
typedef long long ll;
typedef complex<double> pnt;
typedef pair<int, int> pii;

#define RA(x) (x).begin(), (x).end()
#define FE(i, x) for (typeof((x).begin()) i = (x).begin(); i != (x).end(); i++)
#define SZ(x) ((int) (x).size())

template<class T>
void splitstr(const string &s, vector<T> &out)
{
    istringstream in(s);
    out.clear();
    copy(istream_iterator<T>(in), istream_iterator<T>(), back_inserter(out));
}

template<class T> T gcd(T a, T b) { return b ? gcd(b, a % b) : a; }

#define MOD 9901

static vector<vector<int> > edges;
static vector<bool> seen;

static int fact[305];

static int dfs(int x, int p, int depth)
{
    if (seen[x])
        return depth;
    else
    {
        seen[x] = true;
        for (size_t i = 0; i < edges[x].size(); i++)
        {
            int trg = edges[x][i];
            if (trg != p)
                return dfs(trg, x, depth + 1);
        }
        return -1;
    }
}

static int solve(int n, vector<pii> use)
{
    edges.clear();
    edges.resize(n);
    seen.clear();
    seen.resize(n, false);

    for (size_t i = 0; i < use.size(); i++)
    {
        edges[use[i].first].push_back(use[i].second);
        edges[use[i].second].push_back(use[i].first);
    }

    for (int i = 0; i < n; i++)
        if (edges[i].size() > 2)
            return 0;

    int comps = 0;
    for (int i = 0; i < n; i++)
    {
        if (edges[i].size() == 1 && !seen[i])
        {
            dfs(i, -1, 0);
            comps++;
        }
    }

    for (int i = 0; i < n; i++)
    {
        if (edges[i].size() == 2 && !seen[i])
        {
            int depth = dfs(i, -1, 0);
            if (depth == n && comps == 0)
                return 2;
            else
                return 0;
        }
    }

    return (fact[n - use.size() - 1] * (1LL << comps)) % MOD;
}

int main()
{
    int cases;
    cin >> cases;

    fact[0] = 1;
    for (int i = 1; i <= 301; i++)
        fact[i] = (fact[i - 1] * i) % MOD;

    for (int cas = 0; cas < cases; cas++)
    {
        int n, k;
        cin >> n >> k;
        vector<pii> forbid(k);
        for (int i = 0; i < k; i++)
        {
            cin >> forbid[i].first >> forbid[i].second;
            forbid[i].first--;
            forbid[i].second--;
        }
        int ans = 0;
        for (int i = 0; i < (1 << k); i++)
        {
            vector<pii> cur;
            for (int j = 0; j < k; j++)
                if (i & (1 << j))
                    cur.push_back(forbid[j]);
            int sub = solve(n, cur);
            
            cout << "combin " << i << " sub " << sub << endl;
            
            if (cur.size() & 1)
                ans = (ans + MOD - sub) % MOD;
            else
                ans = (ans + sub) % MOD;
        }
        ans = (ans * 4951) % MOD;
        printf("Case #%d: %d\n", cas + 1, ans);
    }
    return 0;
}

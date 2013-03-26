#include <cctype>
#include <climits>
#include <cmath>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <algorithm>
#include <bitset>
#include <deque>
#include <fstream>
#include <iostream>
#include <list>
#include <map>
#include <queue>
#include <set>
#include <sstream>
#include <stack>
#include <string>
#include <utility>
#include <vector>
using namespace std;

typedef long long LL;
typedef vector< int > VI;
typedef vector< VI > VVI;
typedef vector< LL > VLL;
typedef vector< VLL > VVLL;
typedef vector< double > VD;
typedef vector< string > VS;
typedef pair< int, int > II;
typedef vector< II > VII;
typedef vector< VII > VVII;
typedef istringstream ISS;
typedef ostringstream OSS;

#define A(x)  (x).begin(), (x).end()
#define SZ(x) (int)(x).size()
#define F(i, a, b)  for (int i=(a); i<(b); ++i)
#define R(i, n)  F(i, 0, n)
#define FE(i, a, b)  for (int i=(a); i<=(b); ++i)
#define RE(i, n)  FE(i, 0, n)
#define FD(i, a, b)  for (int i=(a); i>=(b); --i)
#define RD(i, n) FD(i, n, 0)
#define FSZ(i, a, v)  F(i, a, SZ(v))
#define RSZ(i, v)  R(i, SZ(v))
#define FDSZ(i, a, v) FD(i, SZ(v)-1, a)
#define RDSZ(i, v) RD(i, SZ(v)-1)
#define FA(i, a, x)  for (__typeof((x).begin()) i=(a); i!=(x).end(); ++i)
#define RA(i, x)  FA(i, (x).begin(), x)
#define FDA(i, a, x)  for (__typeof((x).rbegin()) i=(x).rbegin(); i!=(a); ++i)
#define RDA(i, x)  FDA(i, (x).rend(), x)
#define P(v, x) ((v).find(x) != (v).end())
#define PO(v, x) (find(A(v), x) != (v).end())
#define UN(a) sort(A(a)), (a).erase(unique(A(a)), (a).end())
#define CL(a, b) memset(a, b, sizeof(a))
#define MP(a, b)  make_pair(a, b)
#define CAST(x, type) *({ OSS oss; oss << (x); ISS iss(oss.str()); static type _cast_ret; iss >> _cast_ret; &_cast_ret; })
#define pb push_back
#define pf push_front
#define X first
#define Y second

template<class T> inline void checkmin(T &a, T b) { if (b<a) a=b; }
template<class T> inline void checkmax(T &a, T b) { if (b>a) a=b; }
template<class T> inline void print(T A[], int n) { cout<<"{"; for (int i=0; i<n; i++) { cout<<A[i]; if (i+1<n) cout<<", ";} cout<<"}"<<endl; }
template<class T> inline void print(vector<T> A, int n=-1) { if (n==-1) n=A.size(); cout<<"{"; for (int i=0; i<n; i++) { cout<<A[i]; if (i+1<n) cout<<", "; } cout<<"}"<<endl; }
template<class T> inline void print(T A) { cout<<"{"; RA(i, A) { cout<<*i<<", "; } cout<<"}"<<endl; }
template<class T> inline T gcd(T a, T b) { while (b != 0) { T r=a%b; a=b; b=r; } return a; }


int cell(const string& s, map<string, int>& mp)
{
    if (P(mp, s)) return mp[s];
    if (s == "A") { mp[s] = 0; return 0; }
    if (SZ(s) > 2) {
	if (s.substr(SZ(s)-2) == "AB") {
	    int x = cell(s.substr(0, SZ(s)-2), mp);
	    if (x != 3) { mp[s] = 1; return 1; }
	}
	if (s[0] == 'B' && s[SZ(s)-1] == 'A') {
	    int x = cell(s.substr(1, SZ(s)-2), mp);
	    if (x != 3) { mp[s] = 2; return 2; }
	}
    }
    mp[s] = 3;
    return 3;
}

int main()
{
    int n;
    string s;
    map<string, int> mp;
    cin >> n;
    R(i, n) {
	cin >> s;
	int res = cell(s, mp);
	switch (res) {
	case 0: { cout << "SIMPLE" << endl; break; }
	case 1: { cout << "FULLY-GROWN" << endl; break; }
	case 2: { cout << "MUTAGENIC" << endl; break; }
	case 3: { cout << "MUTANT" << endl; break; }
	}
    }
}
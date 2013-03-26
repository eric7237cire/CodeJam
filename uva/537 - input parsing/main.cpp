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
#define P(v, x) (v.find(x) != v.end())
#define PO(v, x) (find(A(v), x) != v.end())
#define UN(a) sort(A(a)), a.erase(unique(A(a)), a.end())
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

#include <iomanip>

void intelligence(string s)
{
    int a = s.find("=", 0);
    int b = s.find("=", a+1);
    int p = 0;
    int u = 0;
    int i = 0;
    switch (s[a-1]) {
    case 'P': p = 1; break;
    case 'U': u = 1; break;
    case 'I': i = 1; break;
    }
    switch (s[b-1]) {
    case 'P': p = 1; break;
    case 'U': u = 1; break;
    case 'I': i = 1; break;
    }
    // cout <<a<<" "<<b<<endl;
    // cout << p<<" "<<u<<" "<<i<<endl;
    bool flag = false;
    if (!u || !i) if (s[a-1] == 'P') flag = true;

    string x;
    while (isdigit(s[++a]) || s[a] == '.') x += s[a];
    double n1 = CAST(x, double);
    if (s[a] == 'm') n1 /= 1000;
    else if (s[a] == 'k') n1 *= 1000;
    else if (s[a] == 'M') n1 *= 1000000;

    string y;
    while (isdigit(s[++b]) || s[b] == '.') y += s[b];
    double n2 = CAST(y, double);
    if (s[b] == 'm') n2 /= 1000;
    else if (s[b] == 'k') n2 *= 1000;
    else if (s[b] == 'M') n2 *= 1000000;
    // cout << n1 <<" "<<n2<<endl;
    cout.setf(ios_base::fixed);
    cout << setprecision(2);
    double n3;
    if (!p) {
	n3 = n1*n2;
	cout << "P=" << n3 << "W" << endl;
    } else if (!u) {
	if (flag) n3 = n1/n2;
	else n3 = n2/n1;
	cout << "U=" << n3 << "V" << endl;
    } else {
	if (flag) n3 = n1/n2;
	else n3 = n2/n1;
	cout << "I=" << n3 << "A" << endl;
    }
}
    

int main()
{
    string s;
    int n;
    getline(cin, s);
    n = CAST(s, int);
    R(i, n) {
	getline(cin, s);
	cout << "Problem #" << i+1 << endl;
	// cout << s<<endl;
	intelligence(s);
	cout << endl;
    }
}
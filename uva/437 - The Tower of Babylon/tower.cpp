#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <numeric>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<bool> vb;
typedef vector<vb> vvb;
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef vector<pair<int,int> > vii;
typedef vector<vii> vvii;
typedef pair<uint,uint> uu;
typedef map<string, int> msi;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os << setw(5) << vec[i];
    }
    return os;
}

struct block {
    int x,y,z;
	block(int xx, int yy, int zz) : x(xx), y(yy), z(zz) {}
};

bool operator<(const block& a,const block& b)
{
    if(a.x!= b.x) return (a.x>b.x);
    else  if(a.y!= b.y) return (a.y>b.y);
    else return (a.z>b.z);
}

int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	int n;
	int t = 0;
	while(1 == scanf("%d", &n) && n > 0)
	{
		vector<block> seq;

		FOR(i, 0, n)
		{
			int a, b, c;
			scanf("%d%d%d", &a, &b, &c);
			seq.pb( block(a,b,c) );
			seq.pb( block(b,a,c) );
			seq.pb( block(a,c,b) );
			seq.pb( block(b,c,a) );
			seq.pb( block(c, a,b) );
			seq.pb( block(c,b,a) );
		}

		sort( all(seq) );

		vi dp(6*n, 0);
		int maxLen = 0;
		FOR(i, 0, seq.size())
		{
			dp[i] = seq[i].z;

			FOR(j, 0, i)
			{
				if ( seq[j].x > seq[i].x && seq[j].y > seq[i].y ) 
				{
					dp[i] = max(dp[i], dp[j] + seq[i].z);
				}
			}

			maxLen = max(maxLen, dp[i]);
		}

		printf("Case %d: maximum height = %d\n", ++t, maxLen);
	}

	return 0;
}

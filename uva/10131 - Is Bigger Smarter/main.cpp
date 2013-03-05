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
        os << vec[i] << endl;
    }
    return os;
}

struct elephant 
{
	int W;
	int S;
	int index;

	elephant(int w, int s, int i) : W(w), S(s), index(i) 
	{
	}
};

bool operator<( const elephant& b1, const elephant& b2)
{
	if (b1.W != b2.W)
		return b1.W < b2.W;

	return b1.S > b2.S;
}

bool isBefore( const elephant& b1, const elephant& b2)
{
	return b1.W < b2.W && b1.S > b2.S;
}

int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input2.txt","r",stdin);
#endif

	
	
	vector<elephant> seq;
	int w, s;
	int i = 0;
	while(2 == scanf("%d%d", &w, &s ))
	{
		seq.pb( elephant(w, s, ++i) );
	}
		
	int N = seq.size();
	vi dp(N);
	    	    
	sort(all(seq));

	FOR(i, 0, N)
	{
		dp[i] = 1;

		FOR(j, 0, i)
		{
			if ( !isBefore(seq[j], seq[i]) )
				continue;

			dp[i] = max(dp[i], 1 + dp[j]);
		}
	}
			
		
	int max = *max_element(all(dp));
	cout << max << endl;

	int cur = max;
	elephant last (numeric_limits<int>::max(), numeric_limits<int>::min(), -1);

	vi ans; 
	for(int i = seq.size() - 1; i >= 0; --i)
	{
		if (dp[i] == cur && isBefore(seq[i], last))
		{
			last = seq[i];	
			cur --;
			ans.pb(seq[i].index);
			//printf("%d %d %d\n", last.W, last.S, last.index);
			
		}
	}
	
	reverse(all(ans));
    cout << ans;
	

	return 0;
}

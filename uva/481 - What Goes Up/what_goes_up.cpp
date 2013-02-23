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

int main2()
{
	int size = 4;
	int ints[] = {9, 8, 5, 2};
	
	int* lb = lower_bound( ints, ints+size, 1, greater<int>() );

	cout << *lb << endl;
	return 0;
}

int main() 
{
#ifndef ONLINE_JUDGE
	
	freopen ("input.txt","r",stdin);
#endif
	
	
	int n;
	vi seq;

	while(1 == scanf("%d", &n))
	{
		seq.pb(n);
	}

	vi dp(seq.size(), 0);

	int globalMax = 0;

	for(int dpIdx = 0; dpIdx < seq.size(); ++dpIdx)
	{
		int curMax = 1;

		for(int j = dpIdx - 1; j >= 0; --j)
		{
			if (seq[j] >= seq[dpIdx])
				continue;

			int newSeqLen = 1 + dp[j];

			curMax = max(curMax, newSeqLen);
		}

		dp[dpIdx] = curMax;

		globalMax = max(globalMax, curMax);
	}

	cout << globalMax << endl;
	cout << "-" << endl;

	int last = numeric_limits<int>::max();
	int cur = globalMax;

	vi lis;

	for(int i = seq.size() - 1; i >= 0; --i)
	{
		if (dp[i] == cur && seq[i] < last)
		{
			lis.pb(seq[i]);
			--cur;
			last = seq[i];
		}
	}

	reverse(all(lis));

	cout << lis;

	//cout << "Seq " << seq << endl;
	//cout << dp << endl;
	

	return 0;
}
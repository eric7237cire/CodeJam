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
        os << setw(5) << vec[i] ;
    }
    return os;
}


int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	
	
	int N;
	

	while(1 == scanf("%d", &N))
	{
		vi seq1(N);

		FOR(i, 0, N)
		{
			scanf("%d", &seq1[i]);			
		}

		vi seq2( seq1.rbegin(), seq1.rend() );
				
		vi dp1(N);
		vi dp2(N);

		
		vi lis2; 
				
		for(int choice = 0; choice <= 1; ++choice)
		{
			vi lis;
			vi& dp = choice == 0 ? dp1 : dp2;
			vi& seq = choice == 0 ? seq1 : seq2;

			dp[0] = 1;
			lis.pb(seq[0]);

			for(int dpIdx = 1; dpIdx < seq.size(); ++dpIdx)
			{
				//Eveything before it is strictly less than seq[dpIdx]
				vi::iterator it = lower_bound(all(lis), seq[dpIdx]);

				int lisLen = 1+distance(lis.begin(), it);

				if (it == lis.end())
				{
					lis.pb(seq[dpIdx]);
				} else {
					//Found an element 
					*it = seq[dpIdx];
				}
					
				dp[dpIdx] = lisLen;
			}
		}

		//cout << seq1 << endl;
		//cout << dp1 << endl;
		//cout << dp2 << endl;
		
		int ans = 0;
		for(int dpIdx = 0; dpIdx < N; ++dpIdx)
		{
			ans = max(ans, 2 * min(dp1[dpIdx], dp2[N - dpIdx - 1]) - 1);
		}

		
		cout << ans << endl;
		

	}

	

	return 0;
}

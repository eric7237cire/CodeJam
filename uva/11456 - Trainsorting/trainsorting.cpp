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
		os << setw(5) << vec[i];
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

	int T;
	scanf("%d", &T);

	FOR(t, 0, T)
	{
		int N;
		
		scanf("%d", &N);
		
		vi seq(N);

		if (N <= 0) {
			cout << 0 << endl;
			continue;
		}
		
		FOR(i, 0, N)
		{
			scanf("%d", &seq[i]);			
		}

		/**
		The real trick to the whole problem.

		We need to start the train somewhere
		and reversing the sequence will mean
		lcs and lds are the sequences 
		we can make by attaching to the begining
		and end of a train.
		
		*/
		reverse(all(seq));
		
		vi lis;
		vi lds; 

		lis.pb(seq[0]);
		lds.pb(seq[0]);
		int ans = 1;

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

		
			it = lower_bound(all(lds), seq[dpIdx], greater<int>());

			int ldsLen = 1+distance(lds.begin(), it);

			if (it == lds.end())
			{
				lds.pb(seq[dpIdx]);
			} else {
				//Found an element 
				*it = seq[dpIdx];
			}

			#ifndef ONLINE_JUDGE
			cout << dpIdx << endl;
			printf("LIS len %d of %d lds len %d of %d.  possible %d\n", lisLen, lis.size(), ldsLen, lds.size(),
				lisLen + ldsLen  - 1);
					cout << "LIS array " << lis << endl;
					cout << "LDS array " << lds << endl;
			#endif

			ans = max(ans, lisLen + ldsLen  - 1);
		}
		

#ifndef ONLINE_JUDGE
		cout << "LIS array " << lis << endl;
		cout << "LDS array " << lds << endl;
#endif

		
		cout << ans << endl;
		

	}

	return 0;
}

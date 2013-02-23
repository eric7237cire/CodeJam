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


int main() 
{
#ifndef ONLINE_JUDGE

	freopen ("input.txt","r",stdin);
#endif

	int T;
	scanf("%d", &T);

	FORE(t, 1, T)
	{
		int N;
		
		scanf("%d", &N);
		
		vi vecH(N);
		vi vecW(N);

		FOR(i, 0, N)
		{
			scanf("%d", &vecH[i]);
		}

		FOR(i, 0, N)
		{
			scanf("%d", &vecW[i]);
		}

		vi dpInc(N, 0);
		vi dpDec(N, 0); 

		
		for(int dpIdx = 0; dpIdx < N; ++dpIdx)
		{
			dpInc[dpIdx] = vecW[dpIdx];
			dpDec[dpIdx] = vecW[dpIdx];

			for(int j = 0; j < dpIdx; ++j)
			{
				if (vecH[j] < vecH[dpIdx])
					dpInc[dpIdx] = max(dpInc[dpIdx], vecW[dpIdx] + dpInc[j]);

				if (vecH[j] > vecH[dpIdx])
					dpDec[dpIdx] = max(dpDec[dpIdx], vecW[dpIdx] + dpDec[j]);
			}
		}

#ifndef ONLINE_JUDGE
		cout << "LIS array " << dpInc << endl;
		cout << "LDS array " << dpDec << endl;
#endif

		int maxInc = *max_element(all(dpInc));
		int maxDec = *max_element(all(dpDec));

		if (maxInc >= maxDec)
		{
			printf("Case %d. Increasing (%d). Decreasing (%d).\n", t, maxInc, maxDec);
		} else {
			printf("Case %d. Decreasing (%d). Increasing (%d).\n", t, maxDec, maxInc);
		}

	}

	return 0;
}

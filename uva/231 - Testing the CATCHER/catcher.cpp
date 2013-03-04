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

int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	int T;
	scanf("%d", &T);

	FORE(t, 1, T)
	{
		int M, C; //M=Money C=Types of garments
		scanf("%d%d", &M, &C);
		
		vvi modelPrices(C);

		vb dp(M+1, false);

		FOR(c, 0, C)
		{
			int K;
			scanf("%d", &K);

			modelPrices[c].resize(K);

			FOR(k, 0, K)
			{
				scanf("%d", &modelPrices[c][k]);

				//Init first row
				if (c == 0 &&  modelPrices[c][k] <= M)
				{
					dp[ modelPrices[c][k] ]  = true;
				}
			}
		}

		bool hasSolution = true;

		vb dpNext(M+1, false);

		for(int c = 1; c < C && hasSolution; ++c)
		{
			dpNext.assign(M+1, false); 
			hasSolution = false;

			FOR(m, 0, M)
			{
				if (dp[m] == false)
					continue;
								
				FOR(k, 0, modelPrices[c].size())
				{
					if (modelPrices[c][k] + m > M)
						continue;

					hasSolution = dpNext[m+modelPrices[c][k]] = true;
				}
			}

			dp = dpNext;
		}

		if (!hasSolution)
		{
			cout << "no solution" << endl;
		}  else {
			int m = M;
			for(; m >= 0 && !dp[m]; --m) ;
			cout << m << endl;
		}		
	}

	return 0;
}

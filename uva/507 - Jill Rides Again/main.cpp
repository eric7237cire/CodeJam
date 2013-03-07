#include "stdio.h"
#include <algorithm>
//#include <vector>
//#include <cstring>
#include <limits>
#include <cstdlib>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

//typedef vector<int> vi;

int main()
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	char buf[128];
	
	int dp[20000];
	int p[20000];
	gets(buf);
	int T = atoi(buf);

	FOR(t, 0, T)
	{
		int n;
		gets(buf);
		n = atoi(buf) - 1;
		
		// dp[i] means max value of path ending at vertex [i]
		// p[i] path length of max value path 

		int a;

		
		gets(buf);
		a = atoi(buf);
		
		dp[0] = max(0, a);
		p[0] = a > 0 ? 1 : 0; //never any zero value paths

		int best;
		int bestIdx;
		if (a > 0) {
			best = dp[0];
			bestIdx = 0;
		} else {
			bestIdx = -1;
			best = 0;
		}

		for(int i = 1; i < n; ++i)
		{
			gets(buf);
			a = atoi(buf);

			if (dp[i-1] + a >= 0)
			{
				dp[i] = dp[i-1] + a;
				p[i] = p[i-1] + 1;

				if (dp[i] > best || (dp[i] == best && p[i] > p[bestIdx])) 
				{
					bestIdx = i;
					best = dp[i];
				}
			} else {
				dp[i] = 0;
				p[i] = 0;
			}

		}	

		if (bestIdx == -1) {
			printf("Route %d has no nice parts\n", t+1);
		} else {
			int stop1 = bestIdx - p[bestIdx];
			int stop2 = bestIdx;

			//index  0  1  2  3
			//stop 1   2  3  4  5
			//so 3 ; len = 3,  is between 3-3+2 == 2 and 3+2 == 5

			printf("The nicest part of route %d is between stops %d and %d\n", t+1, stop1+2, stop2+2);
		}

	}
}

#if 0
int mainBruteForce()
{


	int T;
	scanf("%d", &T);
	FOR(t, 0, T)
	{
		int n;
		scanf("%d", &n);

		vi stops(n-1); 
		vi sums(n - 1, 0);
		for(int s = 0; s < stops.size(); ++s)
		{
			scanf("%d", &stops[s]);
			if (s == 0) {
				sums[s] = stops[s];
			} else {
				sums[s] = sums[s-1] + stops[s];
			}
		}

		int maxVal = numeric_limits<int>::min();
		int stop1 = -1;
		int stop2 = -1;

		for(int i = 0; i < sums.size(); ++i)
		{
			for(int j = i; j < sums.size(); ++j)
			{
				int sum = sums[j] - ( i > 0 ? sums[i-1] : 0 );

				if (sum > maxVal || (sum == maxVal && j-i > stop2 - stop1)
					|| (sum == maxVal && j-i == stop2 - stop1 && i < stop1 ) )
				{
					maxVal = sum;
					stop1 = i;
					stop2 = j;
				}
			}
		}

		if (maxVal <= 0) {
			printf("Route %d has no nice parts\n", t+1);
		} else {
			//index  0  1  2  3
			//stop 1   2  3  4  5
			//so 1 to 3 is between 2 and 5

			printf("The nicest part of route %d is between stops %d and %d\n", t+1, stop1+1, stop2+2);
		}

	}

	return 0;
}
#endif
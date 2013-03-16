#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <limits>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int V[100];

//Number of coins  / bills
int N; 

const int SIZE_DP = 20000;
int dp[SIZE_DP];

//value targetted
int target;

int T;

int main()
{
	scanf("%d", &T);
	while(T--)
	{
		scanf("%d", &target);
		scanf("%d", &N);
		
		FOR(i, 0, N)
		{
			scanf("%d", &V[i]);			
		}
		
		//printf("Target is %d %d  ", target, target * 5);
		
		fill(dp, dp + SIZE_DP, numeric_limits<int>::max());
		
		dp[0] = 0;
				
        FOR(i, 0, N)
		{
			for(int amt = SIZE_DP - 1 - V[i]; amt >= 0; --amt)
			{
				if (dp[amt] == numeric_limits<int>::max())
					continue;
				
				int nextAmt = amt + V[i];
				
				dp[nextAmt] = min(dp[nextAmt], dp[amt] + 1);
					/*printf("amt %d coinIdx %d num of coins %d Setting %d to %d\n", 
						amt, i, j,
					amt + j * V[i], dpNext[amt + j * V[i]]);*/
				
            }
		}
		
		int minAmt = numeric_limits<int>::max();
		int minCoins = numeric_limits<int>::max();
		
		for(int amt = target; amt < SIZE_DP; ++amt)
		{
			//printf("amt %d i %d  dp[amt] %d \n", amt, i, dpShop[amt]);
			if (dp[amt] == numeric_limits<int>::max())
				continue;
			
			minAmt = amt;
			minCoins = dp[amt];
			break;
		}
				
		printf("%d %d\n", minAmt, minCoins);
	}
	
	return 0;
}
#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
#include <limits>
//#include <string>

#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
typedef unsigned long long ull;
 
//int V[] = {200, 100, 50, 20, 10, 5};
int V[] = { 1, 2, 4, 10, 20, 40};
int C[6];
int N = 6;

const int SIZE_DP = 1000;
int dp[SIZE_DP];
int dpNext[SIZE_DP];

int dpShop[SIZE_DP];
int dpShopNext[SIZE_DP];

int main()
{
	int d1, d2;
	while(true)
	{
		int ok = 0;
		FOR(i, 0, N)
		{
			scanf("%d", &C[i]);
			ok |= C[i];
			//printf("Count idx %d = %d\n", i, C[i]);
		}
		
		if (!ok)
			break;
			
		int d1, d2;
		float f;
		//scanf("%d.%d", &d1, &d2);
		scanf("%f", &f);
		//printf("Float read %f  ", f);
		//int target = d1 * 100 + d2;
		f += 0.001;
		int target = (int) ( f * 100);
		target /= 5;
		
		//printf("Target is %d %d  ", target, target * 5);
		
		fill(dp, dp + SIZE_DP, numeric_limits<int>::max());
		fill(dpNext, dpNext + SIZE_DP, numeric_limits<int>::max());
		fill(dpShop, dpShop + SIZE_DP, numeric_limits<int>::max());
		fill(dpShopNext, dpShopNext + SIZE_DP, numeric_limits<int>::max());
        dpNext[0] = dp[0] = 0;
		dpShopNext[0] = dpShop[0] = 0;
		
        FOR(i, 0, N)
		{
			for(int amt = 0; amt < SIZE_DP; ++amt)
			{
				//printf("amt %d i %d  dp[amt] %d \n", amt, i, dp[amt]);
				if (dp[amt] == numeric_limits<int>::max())
					continue;
				
				for (int j = 1; j <= C[i] && amt + j * V[i] < SIZE_DP; ++j) 
				{					
					dpNext[amt + j * V[i]] =
						min(dpNext[amt + j * V[i]], 
							dp[amt] + j);
					/*printf("amt %d coinIdx %d num of coins %d Setting %d to %d\n", 
						amt, i, j,
					amt + j * V[i], dpNext[amt + j * V[i]]);*/
				}
            }
			
			memcpy(dp, dpNext, sizeof(dp));
			
			for(int amt = 0; amt < SIZE_DP; ++amt)
			{
				//printf("amt %d i %d  dp[amt] %d \n", amt, i, dpShop[amt]);
				if (dpShop[amt] == numeric_limits<int>::max())
					continue;
				
				for (int j = 1; amt + j * V[i] < SIZE_DP; ++j) 
				{					
					dpShopNext[amt + j * V[i]] =
						min(dpShopNext[amt + j * V[i]], 
							dpShop[amt] + j);
					/*printf("amt %d coinIdx %d num of coins %d Setting %d to %d\n", 
						amt, i, j,
					amt + j * V[i], dpShopNext[amt + j * V[i]]);*/
				}
            }
			
			memcpy(dpShop, dpShopNext, sizeof(dpShop));
			
        }
	
		
		int minCoinsUsed = numeric_limits<int>::max();
		
		for(int amt = target; amt < SIZE_DP; ++amt)
		{
			int toGive = dp[amt];
			int toGet = dpShop[amt -target];
			
			if (toGive == numeric_limits<int>::max() || toGet == numeric_limits<int>::max())
				continue;
				
			int coinsUsed = toGive + toGet;
			minCoinsUsed = min(minCoinsUsed, coinsUsed);
			/*printf("amt %d Giving takes %d coins %d from shop %d coins.  Coins used %d\n", 
				amt, toGive, amt - target, toGet, coinsUsed);
				*/
		}
		printf("%3d\n", minCoinsUsed);
	}
	
	return 0;
}
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

const int MAX_NCOIN= 40;
 
int V1[MAX_NCOIN];
int V2[MAX_NCOIN];

const int MAX_AMT = 300;
int nCoins;
int dp[MAX_AMT+1][MAX_AMT+1];
int dpNext[MAX_AMT+1][MAX_AMT+1];

int T;
//goal 
int S;


int main()
{
	scanf("%d", &T);
	
	while(T--)
	{
		scanf("%d%d", &nCoins, &S);
		
		FOR(i, 0, nCoins)
		{
			scanf("%d%d", &V1[i], &V2[i]);
		}
		
		for(int amt1 = 0; amt1 <= MAX_AMT; ++amt1)
		{
			for(int amt2 = 0; amt2 <= MAX_AMT; ++amt2)
			{
				dp[amt1][amt2] = numeric_limits<int>::max();
			}
		}
		
		//dest source
		memcpy(dpNext, dp, sizeof(dp));
		
		dpNext[0][0] = dp[0][0] = 0;
        		
		FOR(coinIdx, 0, nCoins)
		{
			for(int amt1 = 0; amt1 <= MAX_AMT; ++amt1)
			{
				for(int amt2 = 0; amt2 <= MAX_AMT; ++amt2)
				{
				//printf("amt %d i %d  dp[amt] %d \n", amt, i, dp[amt]);
					if (dp[amt1][amt2] == numeric_limits<int>::max())
						continue;
				
					for (int coinsUsed = 1; true ; ++coinsUsed) 
					{
						int nextAmt1 = amt1 + coinsUsed * V1[coinIdx];
						int nextAmt2 = amt2 + coinsUsed * V2[coinIdx];
						
						if (nextAmt1 > MAX_AMT || nextAmt2 > MAX_AMT )
							break;
						
						dpNext[nextAmt1][nextAmt2] = 
						min(dpNext[nextAmt1][nextAmt2], 
							dp[amt1][amt2] + coinsUsed);
					/*printf("amt %d coinIdx %d num of coins %d Setting %d to %d\n", 
						amt, i, j,
					amt + j * V[i], dpNext[amt + j * V[i]]);*/
					}
				}
            }
			
			//Copy next to dp
			memcpy(dp, dpNext, sizeof(dp));
			
			
			
        }
	
		
		int minCoinsUsed = numeric_limits<int>::max();
		
		int S2 = S * S;
		
		for(int amt1 = 0; amt1 <= MAX_AMT; ++amt1)
		{
			for(int amt2 = 0; amt2 <= MAX_AMT; ++amt2)
			{
				if (S2 != amt1 * amt1 + amt2 * amt2)
					continue;
					
				minCoinsUsed = min(minCoinsUsed, dp[amt1][amt2]);
			}
		}
		
		if (minCoinsUsed == numeric_limits<int>::max())
			printf("not possible\n", minCoinsUsed);
		else
			printf("%d\n", minCoinsUsed);
	
	}
	
	return 0;
}
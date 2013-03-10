#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
#include <limits>

//#include <string>

#include <stdlib.h>
//#include <ctype.h>
#include <cassert>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

typedef unsigned int uint; 

char buf[1024];

const int items = 200;
const int maxSpace = 10000;

int W[items];
//total weight
int TW[items];


int maxIdx;
int capacity;

int memo[items][1 + maxSpace];
int used[items+1][1 + maxSpace];

int value(int idx, int left)
{
	//Considered all the coins
	if (idx == maxIdx || left == 0)
		return 0;
		
	if (memo[idx][left] != -1)
	{
		return memo[idx][left];
	}
		
	//These calculations are if car is put on right side
	int weightUsedLeft = capacity - left; 
	int weightUsedRight = TW[idx] - weightUsedLeft;
	
	//printf("idx %d left %d total %d weight Used %d not used %d\n",idx, left, TW[idx], weightUsedLeft, weightUsedRight);
	assert(weightUsedRight >= 0);
		
	int valIfSkip = 0; 
	int valIfUse =  0; 
	
	if (W[idx] <= left)
	{
		//printf("Testing use idx %d left %d total %d weight Used %d not used %d\n", idx, left, TW[idx], weightUsedLeft, weightUsedRight);
		valIfUse = 1 + value(1+idx, left - W[idx] );
	}
	
	if (weightUsedRight <= capacity)
	{
		//printf("Testing not use idx %d left %d total %d weight Used %d not used %d\n", idx, left, TW[idx], weightUsedLeft, weightUsedRight);
		
		valIfSkip = 1 + value(1+idx, left);
	}
	
	if (valIfUse >= valIfSkip && valIfUse > 0) {
		//printf("Used idx memo[%d] left %d  used %d \n", idx, left,  usedIfUse);
		used[idx][left] = 1;
	} else {
		used[idx][left] = 0;
	}
	
	memo[idx][left] = max(valIfSkip, valIfUse);
	 
	//printf("memo[%d] left %d = %d  used %d \n", idx, left, memo[idx][left], usedItems);
	return memo[idx][left];
}

int main()
{
#if !ONLINE_JUDGE && _MSC_VER && !__INTEL_COMPILER
	freopen ("input.txt","r",stdin);
#endif
	int T;
	scanf("%d", &T);
	while(T--)
	{
		scanf("%d", &capacity);
		//printf("%d\n", capacity);
		capacity *= 100;
		
		memset(memo, -1, sizeof memo);
		memset(used, 0, sizeof used);
		
		int w; 
		int idx = 0;
		
		while(1 == scanf("%d", &w) && w != 0)
		{
			//anything beyond 200 is not usable, but keep reading the data anyway
			if (idx >= 200)
				continue;
				
			W[idx] = w;
			
			TW[idx] = W[idx] + (idx == 0 ? 0 : TW[idx-1] );
			++idx;
		}
		
		maxIdx = idx;
			
		//printf("MaxIdx %d capacity %d\n", maxIdx, capacity);
		int ans = value(0, capacity);
		
		assert(ans <= maxIdx);
		
		//printf("max %d Ans %d\n", maxValue, ans);
		printf("%d\n", ans );
		
		int weightLeft = capacity;
		
		FOR(i, 0, ans)
		{
			if (used[i][weightLeft])
			{
				weightLeft  -= W[i];
				//printf("Used %d.  Cap left %d\n", W[i], weightLeft);
				printf("port\n");
			} else {
				
				//printf("Not Used %d.  Cap left %d\n",  W[i], weightLeft);
				printf("starboard\n");
			}
				
		}
		
		if (T > 0)
			printf("\n");
	}

	return 0;
}
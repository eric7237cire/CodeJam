#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
#include <limits>

//#include <string>

#include <stdlib.h>
//#include <ctype.h>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

typedef unsigned int uint; 

char buf[1024];

const int items = 100;
const int maxSpace = 10000;
int V[items];
int W[items];
int TotalW[items];


int maxIdx;

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
		
		
	//Can't use item
	if (W[idx] > left)
		return value(1+idx, left);
		
	int valIfSkip = value(1+idx, left);
	int valIfUse =  V[idx] + value(1+idx, left - W[idx] );
		
	if (valIfUse >= valIfSkip) {
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
		//while(2 == scanf("%d %d", &t, &w))
		memset(memo, -1, sizeof memo);
		memset(used, 0, sizeof used);
		
		
		scanf("%d", &maxIdx);
		
		FOR(i, 0, maxIdx)
		{
			int v, w;
			scanf("%d %d", &v, &w);
			W[i] = w;
			V[i] = v;
		}
			
		int capacity = 10;
		int ans = value(0, capacity);
		
		//printf("max %d Ans %d\n", maxValue, ans);
		printf("%d\n", ans );
		
		int weightLeft = capacity;
		
		FOR(i, 0, maxIdx)
		{
			if (used[i][weightLeft])
			{
				weightLeft  -= W[i];
				printf("Used %d %d.  Cap left %d\n", V[i], W[i], weightLeft);
			} else {
			
				printf("Not Used %d %d.  Cap left %d\n", V[i], W[i], weightLeft);
			}
				
		}
		
		if (T > 1)
			printf("\n");
	}

	return 0;
}
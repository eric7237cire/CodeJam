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

const int items = 30;
const int maxSpace = 1000;
int V[items];
int W[items];
int depth[items];
char* next;

int maxIdx;

int memo[items][1 + maxSpace];
int used[items][1 + maxSpace];

int value(int idx, int left, int& usedItems)
{
	//Considered all the coins
	if (idx == maxIdx || left == 0)
		return 0;
		
	if (memo[idx][left] != -1)
	{
		usedItems |= used[idx][left];
		return memo[idx][left];
	}
		
		
	//Can't use item
	if (W[idx] > left)
		return value(1+idx, left, usedItems);
		
	int usedIfSkip = usedItems;
	int usedIfUse = usedItems;
	int valIfSkip = value(1+idx, left, usedIfSkip);
	int valIfUse =  V[idx] + value(1+idx, left - W[idx], usedIfUse );
	
	if (valIfUse >= valIfSkip) {
		//printf("Used idx memo[%d] left %d  used %d \n", idx, left,  usedIfUse);
		usedItems |= usedIfUse | 1 << idx; 
	} else {
		usedItems |= usedIfSkip;
	}
	
	memo[idx][left] = max(valIfSkip, valIfUse);
	used[idx][left] = usedItems;
	//printf("memo[%d] left %d = %d  used %d \n", idx, left, memo[idx][left], usedItems);
	return memo[idx][left];
}

int NumberOfSetBits(int i)
{
    i = i - ((i >> 1) & 0x55555555);
    i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
    return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
}

int main()
{
#if !ONLINE_JUDGE && _MSC_VER && !__INTEL_COMPILER
	freopen ("input.txt","r",stdin);
#endif
	int t, w;
	bool isFirst = true;
	while(2 == scanf("%d %d", &t, &w))
	{
		memset(memo, -1, sizeof memo);
		
		if (isFirst) {
			isFirst = false;
		} else {
			fputs("\n", stdout);
		}
		
		scanf("%d", &maxIdx);
		
		FOR(i, 0, maxIdx)
		{
			int d, v;
			scanf("%d %d", &d, &v);
			W[i] = 3 * w * d;
			depth[i] = d;
			V[i] = v;
		}
			
		int usedItems = 0;
		int ans = value(0, t, usedItems);
		
		usedItems = used[0][t];
		
		int usedTreasure = NumberOfSetBits(usedItems);
		
		//printf("max %d Ans %d\n", maxValue, ans);
		printf("%d\n", ans );
		printf("%d\n", usedTreasure );
		
		FOR(i, 0, maxIdx)
		{
			if (  (1 << i & usedItems) == 0)
				continue;
				
			printf("%d %d\n", depth[i], V[i]);
		}
	}

	return 0;
}
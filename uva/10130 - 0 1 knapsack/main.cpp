#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <limits>
#include <stdlib.h>

using namespace std;

const int items = 1000;
const int maxSpace = 30;
int V[items];
int W[items];

//local max items for test case
int maxIdx;

// memo[item index][max weight] means using items 0..item index inclusive, the most weight that can be used <= max weight
int memo[items][1 + maxSpace];

int value(int idx, int left)
{
	if (idx == maxIdx || left == 0)
		return 0;
		
	if (memo[idx][left] != -1)
		return memo[idx][left];
		
	//Can't use item
	if (W[idx] > left)
		return value(1+idx, left);
			
	return memo[idx][left] = max( value(1+idx, left), //skipped item
	V[idx] + value(1+idx, left - W[idx]) //used item
	);
}

int main()
{
	int T;
	scanf("%d", &T);
	while(T--)
	{
		memset(memo, -1, sizeof memo);

		scanf("%d", &maxIdx);
		
		for(int i = 0; i < maxIdx; ++i)
		{
			int p, w;
			scanf("%d%d", &p, &w);
			W[i] = w;
			V[i] = p;
		}
		
		int G;
		scanf("%d", &G);
		
		int ans = 0;
		
		while(G--)
		{
			int MW;
			scanf("%d", &MW);
			ans += value(0, MW);
		}		
				
		printf("%d\n", ans );		
		if (T > 1)
			printf("\n");
	}

	return 0;
}
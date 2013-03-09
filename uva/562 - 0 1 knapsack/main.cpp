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
int V[100];
int W[100];
char* next;

int maxIdx;

int memo[100][500*100+1];

int value(int idx, int left)
{
	//Considered all the coins
	if (idx == maxIdx || left == 0)
		return 0;
		
	if (memo[idx][left] != -1)
		return memo[idx][left];
		
		
	//Can't use item
	if (W[idx] > left)
		return value(1+idx, left);
		
	
	memo[idx][left] = max( value(1+idx, left),
		V[idx] + value(1+idx, left - W[idx] ));
	//printf("memo[%d] left %d = %d\n", idx, left, memo[idx][left]);
	return memo[idx][left];
}

int main()
{
#if !ONLINE_JUDGE && _MSC_VER && !__INTEL_COMPILER
	freopen ("input.txt","r",stdin);
#endif
	gets(buf);
	int T = atoi(buf);
	
	while(T--)
	{
		memset(memo, -1, sizeof memo);
		
		gets(buf);
		maxIdx = atoi(buf);
		
		int total = 0;
		
		char * next = gets(buf);
		FOR(m, 0, maxIdx)
		{
			W[m] = V[m] = strtol(next, &next, 10);
			total += V[m];
		}
	
		int maxValue = total / 2;
		
		int ans = value(0, maxValue);
		//printf("max %d Ans %d\n", maxValue, ans);
		printf("%d\n", (total - ans) - ans );
	}

	return 0;
}
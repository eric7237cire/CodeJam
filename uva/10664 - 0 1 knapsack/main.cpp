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

const int MAXN = 200;
const int MAXW = 200;

char buf[1024];

int W[MAXN];

int N;

int memo[MAXN][1 + MAXW];

int possible(int idx, int left)
{
	//printf("idx %d left %d N %d\n", idx, left, N);
	if (left == 0)
		return 1;
		
	if (idx == N)
		return 0;
		
	if (memo[idx][left] != -1)
		return memo[idx][left];
			
		
	//Can't use item
	if (W[idx] > left)
		return possible(1+idx, left);
		
	int ifSkip = possible(1+idx, left);
	int ifUse =  possible(1+idx, left - W[idx]);
	
	return memo[idx][left] = ifSkip || ifUse;
	
	//printf("memo[%d] left %d = %d  used %d \n", idx, left, memo[idx][left], usedItems);	
}

int main()
{

	
	gets(buf);
	int T = atoi(buf);
	
	while(T--)
	{
		memset(memo, -1, sizeof memo);
		
		gets(buf);
		
		N = 0;
		char* next = buf;
		int v;
		
		int sum = 0; 
		while( (v = strtol(next, &next, 10)) != 0)
		{
			//printf("%d sum %d\n", v, sum);
			sum += (W[N++] = v);
		}
		
		if (sum % 2 == 1) 
		{
			puts("NO");
			continue;
		}
		
		
		int ans = possible(0, sum / 2);
		
		puts( ans ? "YES" : "NO" );
		
	}

	return 0;
}
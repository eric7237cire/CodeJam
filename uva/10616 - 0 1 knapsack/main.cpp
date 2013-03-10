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

typedef long long ll;

//global maximums
const int MAXN = 200;
const int MAXQ = 10;
const int MAXM = 10;
const int MAXD = 20;

ll V[MAXN];

//problem maxs
int N;
int D;

/*
	# of ways = memo[index up to][numbers remaining to use][cur remainder]
*/
ll memo[MAXN+1][MAXM][MAXD+1];

ll count(int idx, int nRem, int rem)
{
	//base case, count 1 if current remainder is divisible by target 
	if (nRem == 0)
	{
		return memo[idx][nRem][rem] = (rem + MAXD) % D == 0 ? 1 : 0;
	}
	
	if (idx == N)
		return memo[idx][nRem][rem] = 0;
		
	if (memo[idx][nRem][rem] != -1)
		return memo[idx][nRem][rem];
	
		
	ll countIfSkip = ::count(1+idx, nRem, rem);
	
	int newRem = (V[idx] + rem) % MAXD;
	
	ll countIfUse =  ::count(1+idx, nRem - 1, newRem);
		
	return memo[idx][nRem][rem] = countIfSkip + countIfUse;
	//printf("memo[%d] left %d = %d  used %d \n", idx, left, memo[idx][left], usedItems);
	
}


int main()
{
#if !ONLINE_JUDGE && _MSC_VER && !__INTEL_COMPILER
	freopen ("input.txt","r",stdin);
#endif
	int Q;
	int t = 0;
	
	int M;
	while(2 == scanf("%d %d", &N, &Q) && (N || Q))
	{
		FOR(i, 0, N)
		{
			int v;
			scanf("%d", &v);
			V[i] = v;
		}
			
		printf("SET %d:\n", ++t);
		FOR(q, 0, Q)
		{
			scanf("%d %d", &D, &M);
			memset(memo, -1, sizeof memo);
			
			printf("QUERY %d: %lld\n", q+1, ::count(0, M, 0) );
		}
		
	}

	return 0;
}
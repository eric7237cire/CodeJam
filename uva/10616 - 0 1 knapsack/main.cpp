#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
#include <limits>

//#include <string>

#include <stdlib.h>
#include <cassert>
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
	//printf("D= %d MAXD = %d\n", D, MAXD);
	assert(rem >= 0 && rem < MAXD);
	//base case, count 1 if current remainder is divisible by target 
	if (nRem == 0)
	{
		memo[idx][nRem][rem] = rem % D == 0 ? 1 : 0;
		
		//printf("%d MAXD %d %d idx %d nRem %d rem %d = %d  \n",		D, MAXD, (rem + MAXD) % D,			idx, nRem, rem, memo[idx][nRem][rem]);
		return memo[idx][nRem][rem];
	}
	
	if (idx == N)
		return memo[idx][nRem][rem] = 0;
		
	if (memo[idx][nRem][rem] != -1)
		return memo[idx][nRem][rem];
	
		
	ll countIfSkip = ::count(1+idx, nRem, rem);
	
	int newRem = (V[idx] + rem) % D;
	if (newRem < 0)
		newRem += D;
	
	ll countIfUse =  ::count(1+idx, nRem - 1, newRem);
	
	//printf("idx %d nRem %d rem %d = skip %lld use %lld\n",	idx, nRem, rem, countIfSkip, countIfUse);
	
	return memo[idx][nRem][rem] = countIfSkip + countIfUse;
	
	
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
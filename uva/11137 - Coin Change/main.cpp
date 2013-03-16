#include "stdio.h"
#include <algorithm>
//#include <vector>
//#include <cstring>
//#include "string.h"
//#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <sstream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

typedef long long ll; 
typedef unsigned long long ull;
 
int V[] = {
1, 8, 27, 
64, 125, 36 * 6,
 49 * 7, 64 * 8, 81 * 9,
1000, 
11*11*11, 
12*12*12, 
13*13*13,
14*14*14,
15*15*15,
16*16*16,
17*17*17,
18*18*18,
19*19*19,
20*20*20,
21*21*21
};

const int TYPES = 21;

ll dp[10001][TYPES];

int N;

ll count(int value, int coin)
{
	if (value < 0 )
		return 0;
		
	ll& res = dp[value][coin];
	
	if (res >= 0)
		return res;
	
	//Special because coin 0 is 1	
	if (value == 0 || coin == 0)
		return res = 1;
		
	return res = ::count(value - V[coin], coin) + ::count(value, coin - 1);	
	
}

int main()
{
	
	FOR(i, 0, 10001) FOR(j, 0, TYPES)
		dp[i][j] = -1;
	
	
	while(1 == scanf("%d", &N))
	{
		ll ans = ::count(N, TYPES - 1);
		
		printf("%lld\n", ans);
	}
	
	return 0;
}
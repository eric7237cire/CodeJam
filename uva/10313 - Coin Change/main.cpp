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
 
//int V[] = {10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5};
int V[301]  ;

ll dp[301][301];

int nInput;
int input[4];

ll count(int nCoins, int nSum)
{
	//assert(nCoins < 301 && nCoins >= 0);
	assert(nSum < 301 && nSum >= 0);

	ll& res = dp[nCoins][nSum];
	
	if (res >= 0)
		return res;
	
	//printf("count nCoins %d  nSum %d\n", nCoins, nSum);
	
	
	if (nCoins == 0 && nSum == 0)
		return res = 1;
		
	if (nCoins == 0 || nCoins > 300)
		return res = 0;
	
	res = 0;
	for(int coinVal = 1; coinVal <= nSum; ++coinVal)
	{
		res += ::count(nCoins - 1, nSum - coinVal);	
	}
	if (res < 0)
		printf("nCoins %d nSum %d res %lld\n", nCoins, nSum, res);
	assert(res >= 0);
	
	return res;
}

int main()
{
	
	FOR(i, 0, 301) FOR(j, 0, 301)
		dp[i][j] = -1;
	
	printf("%lld\n", ::count(1, 6));
	printf("%lld\n", ::count(2, 6));
	printf("%lld\n", ::count(3, 6));
	printf("%lld\n", ::count(4, 6));
	printf("%lld\n", ::count(5, 6));
	printf("%lld\n", ::count(6, 6));
	return 0;
	for(string line; getline(cin, line); )
	{
		nInput = 0;
		cout << "Line is " << line << endl;
		istringstream iss(line);
		
		while( iss >> input[nInput] )
		{
			printf("read nInput %d = %d\n", nInput, input[nInput]);
			++nInput;
		}
		
		int lb = 1;
		int ub = 300;
		
		if (nInput == 2)
		{
			ub = input[1];
		} 
		else if (nInput == 3)
		{
			lb = input[1];
			ub = input[2];
		}
		
		
		ll ans = 0;
		for(int nCoins = lb; nCoins <= ub; ++nCoins)
		{
			ans += ::count(nCoins, input[0]);		
		}
	
		printf("%lld\n", ans);
	}
	
	return 0;
}
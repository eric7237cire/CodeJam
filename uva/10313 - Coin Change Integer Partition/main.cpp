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

   
	
ll count(int value, int coin)
{
	if (value < 0)
		return 0;
		
	//assert(nCoins < 301 && nCoins >= 0);
	assert(coin < 301 && coin >= 0);

	ll& res = dp[value][coin];
	
	if (res >= 0)
		return res;
	
	//printf("count nCoins %d  nSum %d\n", nCoins, nSum);
	
	
	if (value == 0 && coin == 0 )
		return res = 1;
		
	if (value > 0 && coin == 0)
		return res = 0;
	
	res = ::count(value - coin, coin) + ::count(value, coin - 1);	
	
	//if (res < 0)
		//printf("nCoins %d nSum %d res %lld\n", nCoins, nSum, res);
	//assert(res >= 0);
	
	return res;
}
/*
ops, sorry, I just realized my explanation didn't make sense... I left out the most important detail.  The number of ways of making $i with coins of value $j or less is the same as the # of ways of making $i with at most j coins. This is a property of integer partitions (splitting a number into a sum of smaller numbers), which is basically what we're dealing with. i.e., the number of partitions of n with <= k parts (in the sum) is equal to the number of partitions of n where each part is <= k. 

The proof is to draw the partition as what's called a Ferrer's diagram. Let's take 1+1+1+3+3+4 = 13 as an example: 

XXXX 
XXX 
XXX 
X 
X 
X 

Well, if you transpose this (like a matrix) you get: 

XXXXXX 
XXX 
XXX 
X 

Which is the partition 6+3+3+1. It isn't hard to see that, in general, if all of the parts in the sum are <= j, then the transpose has <= j parts. So the ways[i,j] is all you need. For the sample input I just do this: 

6 
ways[6,6] = 1 

6 3 
ways[6,3] = 7 

6 2 5 
ways[6,5] - ways[6,1] = 10 - 1 = 9 

6 1 6 
ways[6,6] - ways[6,0] = 11 - 0 = 11 

I hope this helps. 
[/b]

Similar techniques can be employed to establish, for example, the following equalities:
The number of partitions of n into no more than k parts is the same as the number of partitions of n into parts no larger than k.
The number of partitions of n into no more than k parts is the same as the number of partitions of n + k into exactly k parts.
*/
int main()
{
	
	FOR(i, 0, 301) FOR(j, 0, 301)
		dp[i][j] = -1;
	
	/*printf("%lld\n", ::count(1, 6));
	printf("%lld\n", ::count(2, 6));
	printf("%lld\n", ::count(2, 6));
	printf("%lld\n", ::count(4, 6));
	printf("%lld\n", ::count(5, 6));
	printf("%lld\n", ::count(6, 6));*/
	//return 0;
	for(string line; getline(cin, line); )
	{
		nInput = 0;
		//cout << "Line is " << line << endl;
		istringstream iss(line);
		
		while( iss >> input[nInput] )
		{
			//printf("read nInput %d = %d\n", nInput, input[nInput]);
			++nInput;
		}
		
		int lb = 1;
		int ub = 300;
		
		ll ans = 0;
		
		if (nInput == 1)
		{
			ans = ::count(input[0], 300);
		}
		if (nInput == 2)
		{
			ans = ::count(input[0], min(300, input[1]) );
		} 
		else if (nInput == 3)
		{
			lb = input[1];
			ub = input[2];
			ans += ::count(input[0], min(300, ub));
			if (lb > 1)
				ans -= ::count(input[0], min(300, lb - 1));
			//for(int nCoins = lb; nCoins <= ub; ++nCoins)
				//ans += ::count(input[0], nCoins);
		}
		
		
		printf("%lld\n", ans);
	}
	
	return 0;
}
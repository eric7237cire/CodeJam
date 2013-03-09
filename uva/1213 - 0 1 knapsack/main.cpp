#include "stdio.h"
//#include <algorithm>
#include <vector>
#include <cstring>
//#include "string.h"
#include <limits>
#include <cmath>
//#include <string>

#include <stdlib.h>
//#include <ctype.h>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

typedef unsigned int uint; 

//char buf[1024];

const int maxItemsLeft = 14;
const int maxPrimeIndex = 190;
const int maxSpace = 1150;
int V[maxPrimeIndex];


int maxIdx;

//  memo[ using prime index up to ] [ of items remaining to use ] [ value left ]
int memo[ maxPrimeIndex ][ maxItemsLeft ][1 + maxSpace];


void generatePrimes(int n, vector<unsigned int>& primes) 
{
	vector<bool> isPrime(n+1, true); 
	
	isPrime[0] = 0;
	isPrime[1] = 0;

	primes.clear();

	//Since we are eliminating via prime factors, a factor is at most sqrt(n)
	unsigned int upperLimit = static_cast<unsigned int>(sqrt(n));

	for(unsigned int i = 2; i <= upperLimit; ++i) {
		if (!isPrime[i]) {
			continue;
		}

		//Loop through all multiples of the prime factor i.  Start with i*i, because the rest
		//were already covered by previous factors.  Ex, i == 7, we start at 49 because 7*2 through 7*6 
		//we already covered by previous prime factors.
		for(int j = i * i; j <= n; j += i) {
			isPrime[j] = false;
		}
	}

	for(int i = 0; i <= n; ++i) {
	    if (isPrime[i])
	        primes.push_back(static_cast<unsigned int>(i));
	}

}

int countWays(int idx, int itemsLeft, int nLeft)
{
	//Considered all the coins
	if (idx == maxIdx )
		return 0;
		
	if (itemsLeft == 0 && nLeft > 0)
		return 0;
		
	if (memo[idx][itemsLeft][nLeft] != -1)
	{
		return memo[idx][itemsLeft][nLeft];
	}
		
		
	//Can't use item
	if (V[idx] > nLeft)
		return countWays(1+idx, itemsLeft, nLeft);
		
	if (V[idx] == nLeft)
	{
		if (itemsLeft == 1)
			return memo[idx][itemsLeft][nLeft] = 1;
		else 
			//Can't use since 
			return memo[idx][itemsLeft][nLeft] = countWays(1+idx, itemsLeft, nLeft);
	}	
	
	int countWaysIfSkip = countWays(1+idx, itemsLeft, nLeft);
	int countWaysIfUse = countWays(1+idx, itemsLeft - 1, nLeft - V[idx] );
		
	return memo[idx][itemsLeft][nLeft] = countWaysIfSkip + countWaysIfUse;
	
	//printf("memo[%d] left %d = %d  used %d \n", idx, left, memo[idx][left], usedItems);
	
}


int main()
{
#if _MSC_VER && !__INTEL_COMPILER
//	freopen ("input.txt","r",stdin);
#endif
	int n, k;
	
	vector<uint> primes;
	generatePrimes( maxSpace, primes );
	
	memset(memo, -1, sizeof memo);
	
	maxIdx = primes.size();
	
	for(int i = 0; i < primes.size(); ++i)
	{
		V[i] = primes[i];
	}
	
	while(2 == scanf("%d %d", &n, &k) && (n != 0 || k != 0))
	{
		
		
		
		
		/*
		for(int i = 0; i < primes.size(); ++i)
		{
			if (primes[i] > n) 
			{
				maxIdx = i;
				break;
			}
			
			//printf("%d maxIdx %d\n", i, maxIdx);
	//		V[i] = primes[i];
		}
		*/
		
		//printf("maxIdx %d primes size %d\n", maxIdx, primes.size());
		
		
		
			
		int ans = countWays(0, k, n);
		
		printf("%d\n", ans );
		
	}

	return 0;
}


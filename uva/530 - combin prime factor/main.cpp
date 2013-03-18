#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <vector>
#include <stdlib.h>
#include <cmath>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
typedef vector<int> vi;
 
typedef unsigned long long ull;

//2147483648
//210000000

const int limit = 1000;
ull comb[limit][limit];

const int MAX_N = 100000;
bool isPrime[MAX_N+1];

vector<int> primes;

void generatePrimes( ) 
{
	fill(isPrime, isPrime + MAX_N + 1, true);
	isPrime[0] = false;
	isPrime[1] = false;
	
	primes.clear();

	//Since we are eliminating via prime factors, a factor is at most sqrt(n)
	int upperLimit = static_cast<int>(sqrt(MAX_N));

	for(int i = 2; i <= upperLimit; ++i) {
		if (!isPrime[i]) {
			continue;
		}

		//Loop through all multiples of the prime factor i.  Start with i*i, because the rest
		//were already covered by previous factors.  Ex, i == 7, we start at 49 because 7*2 through 7*6 
		//we already covered by previous prime factors.
		for(int j = i * i; j <= MAX_N; j += i) {
			isPrime[j] = false;
		}
	}

	for(int i = 0; i <= MAX_N; ++i) {
	    if (isPrime[i])
	        primes.push_back(i);
	}

}
	
void addFactors(vector<int>& primePowers, int n, int add)
{
	for(int pIdx = 0; pIdx < primes.size() && primes[pIdx] <= n; ++pIdx)
	{
		while( n % primes[pIdx] == 0 )
		{
			primePowers[pIdx] += add;
			n /= primes[pIdx];
		}		
		
	}

}
	
/*
for(int n = 0; n < limit; ++n)
		for(int k = 0; k < limit; ++k)
        {
            if (n<k)
                comb[n][k] =0;
            else if (n==k || k==0 )
                comb[n][k] = 1;
            else
                comb[n][k] = (comb[n-1][k] + comb[n-1][k-1]);
        }
		*/

int main()
{
	generatePrimes();
	
	int n, k;
	while(scanf("%d%d", &n, &k) == 2 && (n || k))
	{
		vi primePowers(primes.size(), 0);
		
		//Find largest denom, either k! or (n-k)!
		int denom1 = k;
		int denom2 = n-k;
		
		int maxDenom = max(k, n-k);
		int minDenom = min(k, n-k);
		
		for(int i = maxDenom+1; i <= n; ++i)
			addFactors(primePowers, i, 1);
		
		for(int i = 1; i <= minDenom; ++i)
			addFactors(primePowers, i, -1);
			
		ull ans = 1;
		
		for(int i = 0; i < primePowers.size(); ++i)
		{
			if (primePowers[i] == 0)
				continue;
				
			while (primePowers[i] > 0)
			{
				ans *= primes[i];
				--primePowers[i];
			}
			
			assert(primePowers[i] == 0);
			
			//Can't have any negatives as a prime would mean the nChoose k would not be an integer
			
		}
		printf( "%llu\n", ans  );
		
	}
	

	return 0;
}
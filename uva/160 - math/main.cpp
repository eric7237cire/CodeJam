#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <vector>
#include <cassert>
#include <cmath>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
const int MAX_N = 50;
int V[MAX_N];
int N;

#if 0
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
		vector<unsigned int> p;
		generatePrimes(100, p);
		
		for(int i = 0; i < p.size(); ++i)
			printf("%d, ", p[i]);
		
		printf("%d\n", p.size());
#endif

int primes[25] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};

int total[25] = {0};

void addFactors(int n)
{
	for(int pIdx = 0; pIdx < 25; ++pIdx)
	{
		while( n % primes[pIdx] == 0 )
		{
			total[pIdx] ++;
			n /= primes[pIdx];
		}
	}

}

int main()
{
	while( 1 == scanf("%d", &N) && N )
	{
		fill(total, total + N, 0);

		for(int i = 1; i <= N; ++i)
		{
			addFactors(i);
		}
		
		printf("%3d! =", N);
		
		for(int pIdx = 0; pIdx < 25 && total[pIdx]; ++pIdx)
		{
			printf("%3d", total[pIdx]);
			if (pIdx == 24 || total[pIdx+1] == 0)
				printf("\n");
			else if (pIdx == 14)
				printf("\n      ");
		}
		
		if (total[0] == 0)
			printf("\n");
		//
		
		//return 0;
		//printf("Case %d: %d\n", t, s);

	}
	
	
	return 0;
}
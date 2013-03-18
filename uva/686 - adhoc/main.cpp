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
 
const int MAX_N = 1000000;
bool isPrime[MAX_N+1];
int N;

template<typename T>
void generatePrimes(int n, vector<T>& primes) 
{
	fill(isPrime, isPrime + MAX_N + 1, true);
	isPrime[0] = false;
	isPrime[1] = false;
	
	
	
	primes.clear();

	//Since we are eliminating via prime factors, a factor is at most sqrt(n)
	T upperLimit = static_cast<T>(sqrt(n));

	for(T i = 2; i <= upperLimit; ++i) {
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
	        primes.push_back(static_cast<T>(i));
	}

}
		

int main()
{
	vector<int> p;
	
	generatePrimes(MAX_N, p);
	
	while( 1 == scanf("%d", &N) && N )
	{
		for(int i = 0; i < p.size(); ++i)
		{
			int other = N - p[i];
			if (isPrime[other])
			{
				printf("%d = %d + %d\n", N, p[i], other);
				break;
			}
		}
		
	}
	
	
	return 0;
}
#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>
#include <cmath>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int N;

int main()
{
	puts("PERFECTION OUTPUT");
	while(scanf("%d", &N) == 1 && N)
	{
		
		int sum = 0;
        int num = N;
        int upperLimit = (int) ( sqrt(N) + 1e-6 );
        upperLimit = min(upperLimit, N - 1);
		
		for(int factor = 1; factor <= upperLimit; ++factor) {
			if (num % factor == 0) {
				sum += factor;
				int otherFactor = num / factor;
				
				if (otherFactor != factor && otherFactor != num)
					sum += otherFactor;
			}
		}
				
		printf("%5d  ", N);
		if (sum > num)
			puts("ABUNDANT");
		else if (sum < num)
			puts("DEFICIENT");
		else	
			puts("PERFECT");
	}
	
	puts("END OF OUTPUT");

	return 0;
}
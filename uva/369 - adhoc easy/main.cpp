#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>
#include <cmath>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
typedef unsigned long long ull;

int N, M;
//2147483648
//210000000

unsigned int comb[101][101];

int main()
{
	for(int n = 0; n <= 100; ++n)
		for(int k = 0; k <= 100; ++k)
        {
            if (n<k)
                comb[n][k] =0;
            else if (n==k || k==0 )
                comb[n][k] = 1;
            else
                comb[n][k] = (comb[n-1][k] + comb[n-1][k-1]);
        }
	
	while(scanf("%d%d", &N, &M) == 2 && (N || M))
	{
		
		printf( "%d things taken %d at a time is %u exactly.\n", N, M, comb[N][M] );
		
	}
	

	return 0;
}
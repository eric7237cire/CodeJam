#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>
#include <cmath>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
const int MAX_N = 50;
int V[MAX_N];
int N;

int FindDiagonal(int n)
{
    --n;
	return (1+sqrt(8*n+1))/2;
}

int main()
{
	
	while(1 == scanf("%d", &N))
	{
		int d = FindDiagonal(N);
		
		int numBefore = d * (d-1) / 2;
		
		//odd start d/1  even start  1 / d
		int pos = N - numBefore -  1;
		
		//printf("N= %d Diag %d num before %d pos=%d\n", N, d, numBefore, pos);

		printf("TERM %d IS ", N);
		
		
		
		if (d % 2 == 0)
		    printf("%d/%d\n", 1 + pos, d - pos);
		else
		    printf("%d/%d\n", d - pos, 1 + pos);
	}
	
	
	return 0;
}
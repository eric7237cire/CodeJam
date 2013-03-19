#include "stdio.h"

typedef unsigned long long ull;

int N;

int main()
{
	
	while(1 == scanf("%d", &N))
	{
		ull ans = ( (ull) N * N * (N+1) * (N+1) ) / 4;
		
		printf("%llu\n", ans);
	}
	return 0;
}
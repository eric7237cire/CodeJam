//STARTCOMMON
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

typedef unsigned long long ull;
//STOPCOMMON

#include "stdio.h"

//1000000000
//2147483648

ull arithSum(int N, int a1, int d)
{
	return (ull)N * (2ULL*a1 + (N-1) *d) / 2;
}

int N;
int main() {

	while(1 == scanf("%d", &N))
	{
		int row = (N+1) / 2;
		
		ull S = arithSum(row, 1, 2);
		
		//printf("N=%d row=%d S = %llu\n", N, row, S);
		
		ull lastInRow = S * 2 - 1;
		
		ull sumLast3 = lastInRow * 3 - 2 - 4;
		
		printf("%llu\n", sumLast3);
		
	}
	return 0;
}

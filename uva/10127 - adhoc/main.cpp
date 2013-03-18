#include "stdio.h"

int N;

int main()
{
	while(1 == scanf("%d", &N))
	{
		int div = 1;
		int digits = 1;
		
		while( div % N != 0 )
		{
			div *= 10;
			++div;
			div %= N;
			++digits;
			//printf("Div %llu N %d\n", div, N);			
		}

		printf("%d\n", digits);
	}
	
	
	return 0;
}
#include "stdio.h"

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
 
int N;
//2147483648
//2000000000
int sumDigits(int n)
{
	int sum = 0;
	while(n)
	{
		sum += n % 10;
		n /= 10;
	}
	
	return sum;
}

int main()
{
	
	
	while(scanf("%d", &N) && N)
	{
		int lastN = 0;
		while( lastN != N )
		{
			lastN = N;
			N = sumDigits(N);
		}
		printf("%d\n", N);		
	}

	return 0;
}
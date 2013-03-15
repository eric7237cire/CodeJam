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

int B, P, M;

int modular_pow(ull base, int exponent, int modulus)
{
    int result = 1;
    while (exponent > 0)
	{
        if (exponent % 2 == 1)
           result = (result * base) % modulus;
		   
        exponent = exponent >> 1;
        base = (base * base) % modulus;
	}
    return result;
}

int main()
{
	
	while(scanf("%d%d%d", &B, &P, &M) == 3)
	{
		if (0 == B)
		{
			printf("0\n" );
			continue;
		}
		printf("%d\n", modular_pow(B, P, M) );
		
	}
	

	return 0;
}
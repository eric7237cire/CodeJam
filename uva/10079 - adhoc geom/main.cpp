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

int N;
//2147483648
//210000000
int main()
{
	
	while(scanf("%d", &N) == 1 && N >= 0)
	{
		printf( "%llu\n", (ull) N * (N+1) / 2 + 1 );
		
	}
	

	return 0;
}
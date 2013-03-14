#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

int N;
 
int f91(int a)
{
	return a <= 100 ? f91(f91(a + 11)) : a - 10;
}

int main()
{
	while(1==scanf("%d", &N) && N)
	{
		printf("f91(%d) = %d\n", N, f91(N));		
	}

	return 0;
}
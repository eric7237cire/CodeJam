#include "stdio.h"
#include <algorithm>
#include <cmath>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

typedef unsigned long long ull;

ull N;
 
int main()
{
	
	while(1 == scanf("%d", &N) && N)
	{
		double root = sqrt(N);
		ull rootInt = (ull) (root + .5);
		if (rootInt * rootInt == N)
			puts("yes");
		else	
			puts("no");
	}
	
	return 0;
}
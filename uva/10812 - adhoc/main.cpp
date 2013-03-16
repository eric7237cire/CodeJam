#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
const int MAX_N = 50;
int V[MAX_N];
int N;

int main()
{
	int T;
	scanf("%d", &T);
	
	for(int t = 1; t <= T; ++t)
	{
		int sum, diff;
		scanf("%d%d", &sum, &diff);
		
		/*
			larger + smaller = sum
			larger - smaller = diff 
			
			sum - larger = larger - diff 
			sum + diff = 2 * larger
			
		*/
		
		bool ok = true; 
		
		if ( (sum + diff) % 2 != 0)
			ok = false;
		
		int larger = (sum + diff) / 2;
				
		int smaller = sum - larger; 
		
		if (larger < 0 || smaller < 0 || larger + smaller != sum || larger - smaller != diff)
			ok = false;
		
		if (ok)
			printf("%d %d\n", larger, smaller);
		else
			printf("impossible\n");
			
		//printf("check %d %d\n", larger, smaller);

	}
	
	
	return 0;
}
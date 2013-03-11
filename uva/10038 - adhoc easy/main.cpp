#include "stdio.h"
//#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <cassert>

#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int N;
const int MAX_N = 3000;
int V[MAX_N];
int last;
int cur;

int main()
{

	while(1 == scanf("%d", &N))
	{
		if (N == 0) {
			puts("Not jolly");
			continue;
		}
		
		memset(V, 0, sizeof V);
		scanf("%d", &last );
		
		bool jolly = true;
		
		FOR(i, 1, N)
		{
			scanf("%d", &cur);
			
			if (!jolly)
				continue;
				
			int diff = abs(last - cur);
			if (diff == 0 || diff >= N || V[diff] == 1) {
				jolly = false;
				continue;
			}
			
			assert(V[diff] == 0);
			
			V[diff] = 1;
			
			last = cur;
		}
		//printf("N=%d\n", N);
		puts(jolly ? "Jolly" : "Not jolly");
	}

	return 0;
}
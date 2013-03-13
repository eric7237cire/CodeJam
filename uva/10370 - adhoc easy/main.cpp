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
const int MAX_N = 1000;
int V[MAX_N];

int main()
{
	int T;
	scanf("%d", &T);
	while(T--)
	{
		scanf("%d", &N);
		
		if (N == 0)
		{
			puts("0.000%");
			continue;
		}
		
		int s = 0;
		FOR(i, 0, N)
		{
			scanf("%d", &V[i]);
				
			s += V[i];			
		}
		
		double avg = (double)s / N;
		
		int nAbove = 0;
		
		FOR(i, 0, N)
		{
			if (V[i] - 1e-7 > avg)
				++nAbove;
		}
		
		printf("%.3lf%%\n", (double) 100 * nAbove / N);
	}

	return 0;
}
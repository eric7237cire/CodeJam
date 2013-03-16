#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
const int SIZE = 3651;
bool V[SIZE];
int N;
int P;
int h;

int main()
{
	int T;
	scanf("%d", &T);
	
	for(int t = 1; t <= T; ++t)
	{
		scanf("%d%d", &N, &P);
		
		fill(V, V+N, false);
		
		for(int p = 0; p < P; ++p)
		{
			scanf("%d", &h);
			
			for(int d = h-1; d < N; d+=h)
			{
				if (d % 7 == 5 || d % 7 == 6)
					continue;
					
				//printf("V[%d] = true\n", d);
				V[d] = true;
			}
		}
		
		int sum = count(V, V+N, true);
			
		printf("%d\n", sum);

	}
	
	
	return 0;
}
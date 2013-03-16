#include "stdio.h"
#include <algorithm>
#include <vector>
//#include <cstring>
#include <limits>

#include <iostream>
#include <string>

#include <stdlib.h>
#include <ctype.h>
#include <cmath>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

const int MAX_SIZE = 500;
int V[MAX_SIZE];

int N;

int main()
{
	int T;
	scanf("%d", &T);
	while(T--)
	{
		scanf("%d", &N);
		FOR(i, 0, N)
			scanf("%d", &V[i]);
			
		int minDist = numeric_limits<int>::max();
		
		sort(V, V + N);
		
		/*
		1 - 1
		3 - 2
		5 - 3
		7 - 4
		9 - 5
		
		2 - 0 1
		4 - 1 2
		6 - 2 3
		8 - 3 4 
		*/
		
		FOR(loc, 1, 30001)
		{
			int dist = 0;
			FOR(i, 0, N)
				dist += abs(V[i] - loc);
			//printf("dist at %d is %d\n", loc, dist);
			minDist = min(minDist, dist);
		}
		
		printf("%d\n", minDist);
	}
	
	return 0;
}
		
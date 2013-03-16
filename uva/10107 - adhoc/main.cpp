#include "stdio.h"
#include <algorithm>
#include <vector>
//#include <cstring>
#include <limits>
#include <list>
#include <iostream>
#include <string>

#include <stdlib.h>
#include <ctype.h>
#include <cmath>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

const int MAX_SIZE = 10001;
int V[MAX_SIZE];

typedef list<int> li;

int N;

int main()
{
	int i = 0;
	list<int>
	while(1 == scanf("%d", &N))
	{
		V[i++] = N;
			
		int minDist = numeric_limits<int>::max();
		
		sort(V, V + N);
		
		/*
		1 - 0
		3 - 1
		5 - 2
		7 - 3
		9 - 4
		
		2 - 0 1
		4 - 1 2
		6 - 2 3
		8 - 3 4 
		*/
		
		int loc = (N % 2 == 0) ? ( V[N/2] + V[N/2 - 1] ) / 2 : V[(N+1) / 2 - 1] ;
		
		//FOR(loc, 1, 30001)
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
		
#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
//#include <set>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
const int MAX_N = 50;
int V[MAX_N];
int N;

int main()
{
	int t = 0;
	while (1==scanf("%d", &N) && N)
	{
		t++;
		int total = 0;
		FOR(n, 0, N)
		{
			scanf("%d", &V[n]);
			total += V[n];
		}
		
		int avgH = total / N;
		
		int dif = 0;
		FOR(n, 0, N)
		{
			if (V[n] > avgH)
			dif += V[n] - avgH;
		}
		
					
		printf("Set #%d\nThe minimum number of moves is %d.\n\n", t, dif);

	}
	
	
	return 0;
}
#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
#include <limits>

//#include <string>

#include <stdlib.h>
//#include <ctype.h>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

typedef unsigned int uint; 

const int MAX_N = 100;
const int MAX_W = 10100;
int W[MAX_N];

int N;
int x;

double memo[MAX_N][1 + MAX_W];


double value(int idx, int share)
{
	if (idx == N && share > 5000)
		return (double)W[x] / share;
		
	if (idx == N)
		return 0;
		
	double& res = memo[idx][share];

	if (res != -1)
		return res;
		
	if (idx == x)
		return res = value(1+idx, share + W[idx]);
	
	double valIfSkip = value(1+idx, share);
	double valIfUse =  value(1+idx, share + W[idx] );
	
	return res = max(valIfSkip, valIfUse);
	
	//printf("memo[%d] left %d = %d  used %d \n", idx, left, memo[idx][left], usedItems);
	
}



int main()
{

	while(2 == scanf("%d%d", &N, &x) && (N||x))
	{
		for(int n = 0; n < N; ++n)
		{
			for(int m = 0; m <= MAX_W; ++m)
			{
				memo[n][m] = -1;
			}
		}
		
		FOR(i, 0, N)
		{
			float v;
			scanf("%f", &v);
			W[i] = (int) (v * 100); 			
		}
			
		double ans = value(0, 0);
		
		printf("%lf\n", ans );
		
	}

	return 0;
}
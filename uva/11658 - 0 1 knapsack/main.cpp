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
	{
		//printf("W[x] %d share %d\n", W[x], share);
		return (double)W[x] / share;
	}
		
	if (idx == N)
		return 0;
		
	double& res = memo[idx][share];

	if (res >= 0)
		return res;
		
	if (idx == x)
		return res = value(1+idx, share + W[idx]);
	
	double valIfSkip = value(1+idx, share);
	double valIfUse =  value(1+idx, share + W[idx] );
	
	
	
	res = max(valIfSkip, valIfUse);
	
	//printf("memo[%d][%d] = %lf\n", idx, share, res);
	
	return res;
	
	
	
	//
	
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
		
		--x;
		
		FOR(i, 0, N)
		{
			int n1, n2;
			scanf("%d.%d", &n1, &n2);
			W[i] = n1 * 100 + n2; 
		}
			
		double ans = value(0, 0);
		
		printf("%.2lf\n", ans*100 );
		
	}

	return 0;
}
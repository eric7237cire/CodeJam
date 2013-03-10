#include "stdio.h"
#include <algorithm>
//#include <cstring>
//#include <limits>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

const int MAX_M = 10200;
const int MAX_N = 100;
int P[MAX_N];
int V[MAX_N];
int N;
int M;

//memo[Index][money left][refund]
int memo[MAX_N][1 + MAX_M][2];

const int INF = 100000;

int value(const int idx, const int left, const int refund)
{
	//penalize if we used refund illegally
	if (idx == N && refund && M - left <= 1800)
		return -INF;
	
	if (idx == N )
		return 0;
		
	int& res = memo[idx][left][refund];
	
	if (res != -1)
		return res;
	
	int valIfSkip = value(1+idx, left, refund);
	
	if (left < P[idx]) 
	{
		return res = valIfSkip;
	}
	
	int valIfUse =  V[idx] + value(1+idx, left - P[idx], refund); 
	
	res = max(valIfSkip, valIfUse);
	
	//printf("memo[ idx %d][ left %d][refund %d] = %d  money used %d \n", idx, left, refund, memo[idx][left][refund], moneyUsed );
	return res; 
}

int main()
{
	
	
	while(2 == scanf("%d%d", &M, &N))
	{
		FOR(i, 0, N)
		{
			int p, f;
			scanf("%d%d", &p, &f);
			P[i] = p;
			V[i] = f;
			
			FOR(m, 0, M+201)
			{
				memo[i][m][0] = -1;
				memo[i][m][1] = -1;
			}
		}
			
		int ans = value(0, M, 0);
		int ans2 = value(0, M+200, 1);
		
		printf("%d\n", max(ans,ans2) );
	}

	return 0;
}
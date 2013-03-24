#include <cstring> 
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <algorithm>

using namespace std;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)


// dp[len][min][max] = min cost to make cuts from min to max on a board of length len 
int dp[53][53];

int cuts[53];


int cost( int minIdx, int maxIdx, const int N, const int L )
{
	int ret = dp[minIdx][maxIdx];
	
	if (ret > 0 )
		return ret;

	ret = numeric_limits<int>::max();
		
	
	//where dimensions of the board 
	int left = minIdx == 0 ? 0 : cuts[minIdx - 1] ;
	int right  = maxIdx == N - 1 ? L : cuts[maxIdx + 1] ;
	int length = right - left; 
	
	if ( minIdx == maxIdx )
	{
		ret = length;
		dp[minIdx][maxIdx] = ret;
		return ret;
	}
	
	assert (minIdx < maxIdx );
			
	//handle left and right seperately
	//cut minIdx
	ret = min(ret, length + cost( minIdx+1, maxIdx, N, L) );
	
	//cut max Idx
	ret = min(ret, length + cost( minIdx, maxIdx-1, N, L) );
	
	
	
	FORE(idx, minIdx+1, maxIdx-1)
	{
		//int newLeft = cuts[idx] - left; 
		//int newRight
		ret = min(ret, length + cost( minIdx, idx - 1, N, L )  + cost( idx+1, maxIdx, N, L ));
	}
	
	dp[minIdx][maxIdx] = ret;
	return ret;
}

int main() 
{
	int t = 0;
	int N, L;
	
	while(  scanf("%d", &L) && L )
	{
		scanf("%d", &N);
		
		if (N <= 0)
		{
			printf("The minimum cutting is 0.\n");
			continue;
		}
		
		//if (N >= 52 )
			//return 0;
			
		FOR(i, 0, N)
		{
			scanf("%d", &cuts[i]);
		}
		
		FOR(i, 0, N) FOR(j, 0, N)
			dp[i][j] = 0;
		
		//memset( dp, 0, sizeof dp );
		int minCost = cost(0, N-1, N, L);
	
			
		printf("The minimum cutting is %d.\n", minCost);
		
	}
	return 0;
}

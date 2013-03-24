#include "stdio.h" 
//#include <cstring>
#include <limits>
#include <algorithm>

using namespace std;
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)

int weight[10][100];
int dp[10][100];
int M, N;

int main() 
{
	while(2 == scanf("%d%d", &M, &N) && (M||N) )
	{
		FOR(m, 0, M) FOR(n, 0, N)
		{			
			scanf("%d", &weight[m][n]);
			//printf(" m[%d][%d] = %d\n", m, n, weight[m][n]);
			dp[m][n] = numeric_limits<int>::max();
		}
		
		//init last row
		FOR(r, 0, M)
		{
			dp[r][N-1] = weight[r][N-1];
		}
		
		//Bottom up DP
		for(int c = N-2; c >= 0; --c) FOR(r, 0, M) 
		{
			for(int dr = -1; dr <= 1; ++dr)
			{
				dp[r][c] = min( dp[ r][c], weight[r][c] + dp[ (M + r + dr) % M ][c+1]   );
			}
		}
		
		FOR(m, 0, M) 
		{
			FOR(n, 0, N)
			{
				//printf(" dp[%d][%d] = %d\n", m, n, dp[m][n]);
				//printf("%3d", dp[m][n]);
			}
			//puts("");
		}
		
		//Find cheapest starting point 
		int lowestCost = numeric_limits<int>::max();
		int lowestRow = -1;
		FOR(r, 0, M)
		{
			if (dp[r][0] < lowestCost)
			{
				lowestCost = dp[r][0];
				lowestRow = r;
			}				
		}
		
		printf("%d", lowestRow+1);
		
		//Find path to the end 
		FOR(c, 1, N)
		{
			int nextRow = -1;
			int lowestStepCost = numeric_limits<int>::max();
			
			for(int dr = -1; dr <= 1; ++dr)
			{
				int row = (M + lowestRow + dr) % M;
				int cost = dp[ row ][c];
				if (cost < lowestStepCost || ( cost == lowestStepCost && row < nextRow ) )
				{
					lowestStepCost = cost;
					nextRow = row;
				}
			}
			
			lowestRow = nextRow;
			printf(" %d", lowestRow+1);
		}
		printf("\n%d\n", lowestCost);
	}
	return 0;
}

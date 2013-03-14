#include "stdio.h"
#include <algorithm>
//#include <stdlib.h>

using namespace std;

typedef unsigned long long ull;

int V[] = {50, 25, 10, 5, 1};
int N = 5;
int target;
ull dp[30001];

int main()
{
	while(1 == scanf("%d", &target) )
	{
		fill(dp, dp + target + 1, 0);
        dp[0] = 1;
        for (int coinIndex = 0; coinIndex < N; ++coinIndex) {
            
            for (int j = V[coinIndex]; j <= target; ++j) {
                dp[j] = dp[j] + dp[j - V[coinIndex]];
            }	
        }

		if (dp[target] == 1)
		{
			printf("There is only 1 way to produce %d cents change.\n", target);
		} else {		
			printf("There are %llu ways to produce %d cents change.\n",  dp[target], target);
		}
	}
	
	return 0;
}
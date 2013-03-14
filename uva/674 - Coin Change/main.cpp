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

		printf("%llu\n", dp[target] );
		
	}
	
	return 0;
}

/*

faster

int N = 5, V, coinValue[5] = {1, 5, 10, 25, 50}, memo[6][7500];
// N and coinValue are fixed for this problem, max V is 7489

int ways(int type, int value) {
  if (value == 0)              return 1;
  if (value < 0 || type == N)  return 0;
  if (memo[type][value] != -1) return memo[type][value];
  return memo[type][value] = ways(type + 1, value) + ways(type, value - coinValue[type]);
}

int main() {
  memset(memo, -1, sizeof memo); // we only need to initialize this once
  while (1 == scanf("%d", &V) )
    printf("%d\n", ways(0, V));

  return 0;
}

*/
#include "stdio.h"
#include <algorithm>
//#include <vector>
//#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>

#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
typedef unsigned long long ull;
 
//int V[] = {10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5};
int V[] = {2000, 1000, 400, 200, 100, 40, 20, 10, 4, 2, 1};
int N = 11;
ull dp[30001];

int main()
{
	int d1, d2;
	
	while(2 == scanf("%d.%d", &d1, &d2) && (d1|d2))
	{
		int target = d1 * 100 + d2;
		target /= 5;
		
		fill(dp, dp + target + 1, 0);
        dp[0] = 1;
        for (int coinIndex = 0; coinIndex < N; ++coinIndex) {
            
            for (int j = V[coinIndex]; j <= target; ++j) {
                dp[j] = dp[j] + dp[j - V[coinIndex]];
            }
        }

		printf("%3d.%02d%17llu\n", d1, d2, dp[target]);
	}
	
	return 0;
}
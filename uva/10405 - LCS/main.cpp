#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
const int MAX_LEN = 1002;
char buf1[MAX_LEN];
char buf2[MAX_LEN];
int dp[MAX_LEN][MAX_LEN];

int main()
{
	while(gets(buf1))
	{
		memset(dp, -1, sizeof dp);
		gets(buf2);
		
		int lenStr1 = strlen(buf1);
		int lenStr2 = strlen(buf2);
		
		for(int i = 0; i < lenStr2; ++i)
		{
			for(int j = 0; j < lenStr1; ++j)
			{
				int above = i == 0 ? 0 : dp[i-1][j];
				int left = j == 0 ? 0 : dp[i][j-1];
				int aboveLeft = i == 0 || j == 0 ? 0 : dp[i-1][j-1];
				
				if ( buf1[j] == buf2[i] )
				{
					aboveLeft += 1;
				}
				
				dp[i][j] = max( max(above, left), aboveLeft );
			}
		}
			
		printf("%d\n", dp[lenStr2-1][lenStr1-1]);

	}
	
	
	return 0;
}
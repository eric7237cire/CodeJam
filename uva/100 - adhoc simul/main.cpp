#include "stdio.h"
#include <algorithm>
//#include <vector>
//#include <cstring>
//#include "string.h"
#include <limits>

//#include <string>

#include <stdlib.h>

//#include <ctype.h>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 


int i, j;
const int cacheLimit = 1000000;
int cache[cacheLimit];

int stack[1000];

int main()
{
	#ifndef ONLINE_JUDGE
	freopen ("data.txt","r",stdin);
	//freopen ("in.txt","r",stdin);
#endif
	while(2 == scanf("%d%d", &i, &j))
	{
		printf("%d %d", i, j);
		
		if (i > j)
			swap(i, j);
			
		int maxLen = 0;
		
		for(int k = i; k <= j; ++k)
		{
			int c = 1;
			//printf("k=%d\n", k);
			
			if (k < cacheLimit && cache[k] > 0)
			{
			
				c = cache[k];
			} else {
			
				int s = k;
				stack[0] = s;
				int breakAt = 0;
				
				//If s overflows, it will quit
				while(s > 1)
				{
					s = s % 2 == 1 ? 3 * s + 1 : s / 2;

					if (s < 0)
						break;

					if (s < cacheLimit && cache[s] > 0)
					{
						breakAt = c;
						c+= cache[s] ;
						break;
					}
					//printf("i=%d j=%d k=%d s = %d\n", i, j, k, s);
					stack[c++] = s;
				}
				
				if (breakAt == 0)
					breakAt = c;
				
				for(int i = 0; i < breakAt; ++i)
				{
					if (stack[i] < cacheLimit)
					{
						cache[ stack[i] ] = c - i;
						//printf("Set %d to %d\n", stack[i], c-i);
					}
				}
				
				
			}
			if (c > maxLen) maxLen = c;
		}
		
		printf(" %d\n", maxLen);
	}
		
	return 0;
}
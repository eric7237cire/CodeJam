#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>
#include <cmath>
#include <string> 
#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int n, k;
int main()
{
	
	while(2 == scanf("%d%d", &n, &k) )
	{
		int sum = 0;
		int butts = 0;
		
		while(n > 0)
		{
			sum += n;
			butts += n;
						
			if (k > 1) {
				n = butts / k;
				butts -= k * n;
			}
		}
		
		printf("%d\n", sum);
		
	}

	return 0;
}
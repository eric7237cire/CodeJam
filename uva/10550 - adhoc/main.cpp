#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int a, b, c, d;

int main()
{
		
	while(4 == scanf("%d%d%d%d", &a, &b, &c, &d) && (a||b||c||d) )
	{
		int sum = 0;
		
		//each tick is 9 degrees
		
		
		//2 full turs
		sum += 360 * 3;
		//printf("start sum is %d\n", sum);
		
		//stop at first number 'b'		
		sum += 9 *  ( (40 + a - b ) % 40 );
		//printf("at b %d sum is %d\n", b, sum);
		
		//counter clockwise 1 full
		
		 
		sum += 9 * ( (40 + c - b ) % 40 );
		//printf("at c %d sum is %d\n", c, sum);
		
		sum += 9 * ( (40 +  c - d) % 40 );
		//printf("at d %d sum is %d\n", d, sum);
		
		printf("%d\n", sum);

	}

	return 0;
}
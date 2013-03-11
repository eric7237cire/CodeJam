#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
//#include <set>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 

int V[1501];

int main()
{
	int i = 0;
	V[0] = 1;
	
	int last2, last3, last5;
	last2= last3 = last5 = 0;
	
	for(int i = 1; i <= 1500; ++i)
	{
		int prev = V[i-1];
		while( V[last2] * 2 <= prev ) {
			++last2;
		}
		while( V[last3] * 3 <= prev ) {
			++last3;
		}
		while( V[last5] * 5 <= prev ) {
			++last5;
		}
		int c1 = V[last2] * 2;
		int c2 = V[last3] * 3;
		int c3 = V[last5] * 5;
		
		V[i] = min(c1, min(c2, c3));
		//printf("%d = %d\n", i+1, V[i]);
		if (i+1 == 1500)
		{
			printf("The 1500'th ugly number is %d.\n", V[i]);
			return 0;
		}
	}
	
	
	return 0;
}
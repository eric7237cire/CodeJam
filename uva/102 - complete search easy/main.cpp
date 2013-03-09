#include "stdio.h"
//#include <algorithm>
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

typedef unsigned int uint; 

char buf[1024];
int num[3][3];
char* next;

char letters[3] = { 'B', 'G', 'C' };

int B = 0;
int C = 2;
int G = 1;

int order[6][3] = {
	{B, C, G},
	{B, G, C},
	{C, B, G},
	{C, G, B},
	{G, B, C},
	{G, C, B}
};
	

int main()
{
	
	while(true)
	{
		next = gets(buf);
		if (next == NULL)
			break;
			
		FOR(i, 0, 3) FOR(j, 0, 3)
		{
			num[i][j] = strtol(next, &next, 10);
		}
	
		int movements = numeric_limits<int>::max();
		int minOrder = -1;
		
		FOR(orderIdx, 0, 6)
		{
			int cost = 0;
			FOR(bin, 0, 3) FOR (botType, 0, 3)
			{
				if (order[orderIdx][bin] == botType)
					continue;
					
				cost += num[bin][botType];
			}
			
			if (cost < movements)
			{
				movements = cost;
				minOrder = orderIdx;
			}
		}
		//order
		printf("%c%c%c %d\n", letters[ order[minOrder][0] ], 
		letters[ order[minOrder][1] ], letters[ order[minOrder][2] ], movements);
	}

	return 0;
}
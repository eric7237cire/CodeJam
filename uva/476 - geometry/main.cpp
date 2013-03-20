#include "stdio.h"
#include <cassert>
#include <cmath>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)

float rect[10][4];

float epsilon = 0.00001;

int cmp(float a, float b)
{
	//printf("Compare %f %f", a, b);
	
	float dif = a - b;
	if (abs(dif) <= epsilon)
	{
		//printf(" = 0 equal\n");
		return 0;
	}
	
	if (dif > 0)
	{
		//printf(" = 1 greater\n");
		return 1; //a > b
	}
	
	//printf(" = -1 less\n");
	return -1;
}
	
int main()
{
	char c;
	
	int R = 0;
	while(1==scanf(" %c", &c) && c == 'r' )
	{
		scanf("%f%f%f%f", &rect[R][0], &rect[R][1],
		&rect[R][2], &rect[R][3]);
		++R;
	}
	
	assert(c == '*');
	
	float x, y;
	int pointIdx = 0;
	while(2 == scanf("%f%f", &x, &y))
	{
		++pointIdx;
		if (cmp(x, 9999.9) == 0 && cmp(y, 9999.9) == 0)
			continue;
		
		int atLeastOne = false;
		for(int r = 0; r < R; ++r)
		{
			//printf("Rectangle %d point %d\n", r+1, pointIdx);
			if (cmp(x, rect[r][0]) > 0 &&
			cmp(x, rect[r][2]) < 0 &&
			cmp(y, rect[r][3]) > 0 &&
			cmp(y, rect[r][1]) < 0 )
			{
				atLeastOne = true;
				printf("Point %d is contained in figure %d\n", 
					pointIdx, r+1);
			}
		}
		
		if (!atLeastOne)
		{
			printf("Point %d is not contained in any figure\n", pointIdx);
		}
	}
	
	return 0;
}
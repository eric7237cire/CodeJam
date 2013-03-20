#include "stdio.h"
#include <cassert>
#include <cmath>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)

float rect[10][4];

float epsilon = 0.00001;

int cmp(float a, float b)
{
	float dif = a - b;
	if (abs(dif) <= epsilon)
		return 0;
	
	if (dif > 0)
		return 1; //a > b
	
	return -1;
}
	
int main()
{
	float f1 = 3.6;
	float f2 = 3.5;
	printf("cmp %f %f = %d\n", f1, f2, cmp(f1, f2));
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
			if (cmp(x, rect[r][0]) > 0 &&
			cmp(x, rect[r][2]) < 0 &&
			cmp(y, rect[r][1]) > 0 &&
			cmp(y, rect[r][3]) < 0 )
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
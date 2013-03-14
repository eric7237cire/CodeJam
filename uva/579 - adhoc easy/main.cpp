#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int h, m;
 
int main()
{
	while(2 == scanf("%d:%d", &h, &m) && (h||m) )
	{
		if (h == 12) h = 0;
		
		double a1 = 30 * h + .5 * m;
		double a2 = 6 * m;
		
		double dif = a1 >= a2 ? a1 - a2 : a2 - a1; //+ 180;
		
		while(dif > 180)
			dif = 360 - dif;
		
		printf("%.3lf\n", dif );
		//printf("a1 = %lf, a2 = %lf, %.3lf\n", a1, a2,  dif );
	}
	
	return 0;
}
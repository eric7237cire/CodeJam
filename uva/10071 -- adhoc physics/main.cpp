#include "stdio.h"
//#include <algorithm>
//#include <vector>
//#include <cstring>
//#include "string.h"
//#include <limits>

//#include <string>

#include <stdlib.h>
//#include <ctype.h>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

typedef unsigned int uint; 

int main()
{
	int v, t;
	
	while(2 == scanf("%d%d", &v, &t))
	{
		//average velocity will be exactly v, so for 2 * t 
		printf("%d\n", 2 * v * t);
	}

	return 0;
}
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
	uint a, b;
	
	while(2 == scanf("%u%u", &a, &b))
	{
		printf("%d\n", abs(a - b));
	}

	return 0;
}
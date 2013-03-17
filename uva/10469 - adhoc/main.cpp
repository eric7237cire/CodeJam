#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
unsigned int a, b;

int main()
{
	while(2 == scanf("%u%u", &a, &b))
	{
	
		printf("%u\n", a ^ b);

	}
	
	
	return 0;
}
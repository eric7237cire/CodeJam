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
typedef long long ll;

int main()
{
	ll a, b;
	
	while(2 == scanf("%lld%lld", &a, &b))
	{
	//printf("%lld %lld ", a, b);
		ll diff = a - b;
		if (diff < 0) diff *= -1;
		printf("%lld\n", diff);
	}

	return 0;
}
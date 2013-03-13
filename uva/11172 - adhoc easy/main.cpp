#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <list>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int main()
{
	int T;
	scanf("%d", &T);
	
	while(T--)
	{
		int a, b;
		scanf("%d%d", &a, &b);
		
		if (a > b)
			puts(">");
		else if (a < b)
			puts("<");
		else
			puts("=");
		
	}

	return 0;
}
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
 
const int MAX_SIZE = 32;
char buf[MAX_SIZE];
unsigned int base[MAX_SIZE];
int main()
{
	base[0] = 2;
	FOR(i, 1, MAX_SIZE)
		base[i] = 2 * base[i];
		
	while( gets(buf) && strcmp(buf, "0") != 0 )
	{
		printf("%s \n", buf);		
		
		int len = strlen(buf);
		int ans = 0;
		FOR(i, 0, len)
		{
			printf("base[%d] = %u\n", i, base[i]);
			ans += base[i] * (len - i) - 1;
		}
		printf("%d\n", ans);
		
		
	}
	
	return 0;
}
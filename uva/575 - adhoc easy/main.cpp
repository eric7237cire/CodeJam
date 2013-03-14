#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
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
		base[i] = 2 * base[i-1];
		
	while( gets(buf) && strcmp(buf, "0") != 0 )
	{
		
		
		int len = strlen(buf);
		//printf("Read [%s] len %d \n", buf, len);		
		int ans = 0;
		FOR(i, 0, len)
		{
			//printf("base[%d] = %u\n", i, base[i]);
			ans += (base[len - i - 1]-1) * (buf[i] - '0') ;
		}
		printf("%d\n", ans);
		
		
	}
	
	return 0;
}
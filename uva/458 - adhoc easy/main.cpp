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

char buf[5000];
char * s;
typedef unsigned int uint; 

int main()
{
	
	while( s = gets(buf) )
	{
		while( *s != '\0' )
		{
			putc( *s++ - 7, stdout);
		}
		puts("");
	}

	return 0;
}
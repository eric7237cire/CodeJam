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
	bool isLeft = true;
	
	while( s = gets(buf) )
	{
		while( *s != '\0' )
		{
			if (*s == '"')
			{
				if (isLeft)
					fputs("``", stdout);
				else 
					fputs("''", stdout);
				isLeft = !isLeft;
			} else {
				putc( *s, stdout);
			}
			++s;
		}
		puts("");
	}

	return 0;
}
#include "stdio.h"
//#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int MAX_LINE = 1024;
char buf[1024];

int main()
{
	
	while(fgets(buf, MAX_LINE, stdin))
	{
		char* c = buf;
		
		int state = 0;  // state 1 seen a character or more, 0
		int count = 0;
		while(*c != '\0')
		{
			bool isChar = (*c >= 'A' && *c <= 'Z') || (*c >= 'a' && *c <= 'z');
			if (state == 0 && isChar)
				state = 1;
			
			if (state == 1 && !isChar)
			{
				count++;
				state = 0;
			}
			
			++c;
		}
		
		printf("%d\n", count + state);
		
	}

	return 0;
}
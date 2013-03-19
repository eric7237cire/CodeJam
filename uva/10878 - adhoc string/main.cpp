#include "stdio.h"
#include <cstring>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)

char line[12];

int main()
{
	while( gets(line) )
	{
		if ( line[6] != '.' ) 
			continue;
			
		char c = 0;
		
		for(int i = 2; i <= 8; ++i)
		{
			int pos = i;
			if (i >= 6)
				pos++;
			
			if (line[pos] == 'o')			
				c |= 1 << 8 - i;			
		}
		printf("%c", c);		
	}

	return 0;
}
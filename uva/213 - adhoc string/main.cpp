#include "stdio.h"

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
 
char header[1228];

/*
0 - 0 (+0)
00 - 1 (+1)
01 - 2
10 - 3 
000 - 4 (+4)
001 - 5
010 - 6
011 - 7
100 - 8
101 - 9
110 - 10


*/

int offsets[9] = {0};

bool debug = false;

int main()
{
	for(int o = 1; o <= 8; ++o)
	{
		offsets[o] = offsets[o-1] + (1 << o) - 1;
		if (debug) printf("Offsets %d = %d\n", o, offsets[o]);
	}

	while(gets(header))
	{
		while(true)
		{
			//read segment header
			int len = 0;
			for(int i = 2; i >= 0; --i)
			{
				char inBit;
				scanf(" %c", &inBit);
				if (inBit =='1')
					len |= 1 << i;
			}
			if (debug) printf("Segment key length = %d\n", len);
			
			if (len == 0)
			{
				//eat the new line
				gets(header);
				break;
			}
			
			while(true)
			{
				int code = 0;
				for(int i = len - 1; i >= 0; --i)
				{
					char inBit;
					scanf(" %c", &inBit);
					if (debug) printf("Read code len %d pos %d = %c\n", len, i, inBit);
					if (inBit =='1')
						code |= 1 << i;
						
					
				} 
				
				if (debug) printf("Read code %d\n", code);
				
				//read end of segment
				if (code + offsets[len-1] >= offsets[len])
				{
					if (debug) puts("end of segment");
					break;
				}
				
				if ( debug) printf("Pos: %d letter [%c]\n", offsets[len-1] + code, header[ offsets[len-1] + code ]);
				printf("%c", header[ offsets[len-1] + code ]);
			}
		}
		
		puts("");
	}
	
	return 0;
}
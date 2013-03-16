#include <stdio.h>
#include <algorithm>

int rp[100];
int cp[100];
 
int N;

using namespace std;

int main ()
{
    while( 1 == scanf("%d", &N) && N)
	{
		fill(rp, rp+N, 0);
		fill(cp, cp+N, 0);
		
		for(int r = 0; r < N; ++r)
		{
			for(int c = 0; c < N; ++c)
			{
				int bit;
				scanf("%d", &bit);
				
				if (bit == 0)
					continue;
					
				rp[r] ^= 1;
				cp[c] ^= 1;
			}
		}
 
		bool ok = true;
		int oddRow = -1;
		int oddCol = -1;
		
		for(int r = 0; r < N; ++r)
		{
			if (rp[r] % 2 == 0)
				continue;
				
			if (oddRow != -1)
				ok = false;
			else	
				oddRow = r+1;
		}
		
		for(int c = 0; c < N; ++c)
		{
			if (cp[c] % 2 == 0)
				continue;
				
			if (oddCol != -1)
				ok = false;
			else	
				oddCol = c+1;
		}
		
		if (!ok || oddRow * oddCol < 0)
			printf("Corrupt\n");
		else if (oddRow > 0)
			printf("Change bit (%d,%d)\n", oddRow, oddCol);
		else 
			printf ("OK\n");
    }
 
    return 0;
}
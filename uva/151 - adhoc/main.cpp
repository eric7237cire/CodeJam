#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
const int MAX_N = 100;
bool V[MAX_N];
int N;

int main()
{
	
	while( 1 == scanf("%d", &N) && N )
	{
		
				
		for(int m = 1; m <= 2*N ; ++ m)
		{
			fill( V, V + N, false );
			int loc = 0;
			V[loc] = true;
			
			int left = N - 1;
			
			bool ok = true;
			
			while( left > 0 )
			{
				int skipped = 0;
				while( skipped < m )
				{
					loc = (loc + 1) % N;
					
					while(V[loc])
					{
						++loc;
						if (loc == N)
							loc = 0;
					}
					
					++skipped;
					
					//printf("N=%d m=%d skipped=%d loc=%d\n", N, m, skipped, loc+1);
				}
				
				assert(!V[loc]);
				V[loc]  = true;
				//printf("N=%d m=%d left=%d loc=%d\n", N, m, left, loc+1);
				--left;
				
				if (loc == 12 && left > 0) {
					ok = false;
					break;
				}
			}
			
			if (ok)
			{
				printf("%d\n", m);
				break;
			}
		}
		
		
			
		//printf("Case %d: %d\n", t, s);

	}
	
	
	return 0;
}
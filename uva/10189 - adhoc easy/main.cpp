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
int m, n;
char grid[102][102];
char buf[102];

int dir[][2] =
	{
	{1, 0},
	{1, 1},
	{0, 1},
	{-1, 1},
	{-1, 0},
	{-1, -1},
	{0, -1},
	{1, -1}
	};
int main()
{
	int T =0;
	//scanf("%d", &T);
	
	while(2 == scanf("%d%d", &m, &n) && (m||n))
	{
		++T;
		
		if (T > 1)
			puts("");
			
		printf("Field #%d:\n", T);
		
		gets(buf);
		for(int r=0; r < m; ++r)
		{
			fgets(buf, 102, stdin);
			strcpy(grid[r], buf);
			//puts(buf);
		}
		
		for(int r=0; r < m; ++r)
		{
			for(int c=0; c < n; ++c)
			{
				if (grid[r][c] == '*')
				{
					putc('*', stdout);
					continue;
				}
				int tally = 0;
				for(int d=0; d < 8; ++d)
				{
					int rr = r + dir[d][1];
					int cc = c + dir[d][0];
					
					if (rr >= 0 && rr < m &&
					cc >= 0 && cc < n && grid[rr][cc] == '*')
					{
						++tally;
					}
				}
				
				printf("%d", tally);
			}
			
			puts("");
		}
	}
	return 0;
}
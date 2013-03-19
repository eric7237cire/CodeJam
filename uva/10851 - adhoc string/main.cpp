#include "stdio.h"
#include <cstring>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)

char matrix[10][85];
char message[81];

int main()
{
	int T;
	scanf("%d", &T);
	
	while(T--)
	{
		memset(message, 0, sizeof message);
		int len = 0;
		
		for(int r = 0; r < 10; ++r)
		{
			gets(matrix[r]);
			
			if (len == 0)
			{
				gets(matrix[r]);
				len = strlen(matrix[r]);
			}						
			
			//printf("%d: %s len %d\n", r, matrix[r], len);
		}
		
		for(int r = 1; r <= 8; ++r)
		{
			for(int c = 1; c < len - 1; ++c)
			{
				if (matrix[r][c] == '\\')
				{
					message[c-1] |= 1 << ( (r-1) );
					//printf("%d\n", r-1);
				}
			}
		}
		
		for(int c = 0; c < len - 2; ++c)
			printf("%c", message[c]);
		puts("");
	}

	return 0;
}
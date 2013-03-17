#include "stdio.h"
#include <cassert> 

int x, y, d;
int top, right;

bool scent[51][51];
char buf[102];

int dir[4][2] = {
{0, 1},
{1, 0},
{0, -1},
{-1, 0}
};

int charToDir(char c)
{
	switch(c)
	{
		case 'N': return 0;
		case 'E': return 1;
		case 'S': return 2;
		case 'W': return 3;
	}
	
	//assert(false);
	return -1;
}

char dirToChar(int d)
{
	switch(d)
	{
		case 0: return 'N';
		case 1: return 'E';
		case 2: return 'S';
		case 3: return 'W';
	}
	
	printf("Direction %d\n", d);
	assert(false);
}

int main() 
{
	scanf("%d%d", &right, &top);
	

	//printf("right %d top %d\n", right, top);
	for(int i = 0; i <= right; ++i)
		for(int j = 0; j <= top; ++j)
			scent[i][j] = false;
	
	char dirCh;
	while(4 == scanf("%d %d %c %s", &x, &y, &dirCh, buf))
	{
		d = charToDir(dirCh);
		bool lost = false;

		int oldX;
		int oldY;
		
		//printf("%d %d %d %s\n", x, y, d, buf);
		
		char* it = buf;
		while(*it != '\0' && !lost)
		{
			switch(*it)
			{
				case 'F':
				oldX = x;
				oldY = y;
				x += dir[d][0];
				y += dir[d][1];
				if (x < 0 || x > right || y < 0 || y > top)
				{
					if (scent[oldX][oldY] )
					{
						x = oldX; y = oldY;
					} else {
						printf("%d %d %c LOST\n", oldX, oldY, dirToChar(d));
						lost = true;
						scent[oldX][oldY] = true;
						break;
					}
				}
				break;
				case 'R':
				d = (d+1) % 4;
				break;
				case 'L':
				d = (d + 3) % 4;
				break;
			}
			
			//printf("prog %d %d %c\n", x, y, dirToChar(d));
			
			++it;
		}
		
		if (!lost)
			printf("%d %d %c\n", x, y, dirToChar(d));
	}

}

#include <stdio.h>

bool debug = false;

int dir[8][2]={
	{0,1},
	{-1,1},
	{-1,0},
	{-1,-1},
	{0,-1},
	{1,-1},
	{1,0},
	{1,1}};
	
char pos[3][3];

int x,y,d;

int main()
{
	int T = 1;
	while ( 1==scanf("%d",&x) && x != -1)
	{
		scanf("%d%d",&y,&d);
		
		pos[1][1] = '1';
		
		for (int i = 0; i < 8; i++) 
		{
			int a, b;
			char c;
			scanf("%d%d %c",&a,&b,&c);
			//printf("Read %d %d %c\n", a, b, c);
			pos[a - (x-1)][b - (y-1)] = c;
		}
		
		if (debug)
			printf("Line %d x %d y %d d %d\n", T++, x, y, d);
		
		if (debug)
		for(int y = 2; y >= 0; --y)
		{
			for(int x = 0; x <= 2; ++x)
			{
				printf("%c ", pos[x][y]);
			}
			puts("");
		}

		//if water is in front of us, turn counter clockwise (direction of coastal track)
		bool isCCW = pos[ 1 + dir[d][0] ][ 1 + dir[d][1] ] == '0';
		if (debug) printf("Water in front %s\n", isCCW ? "yes"  : "no");
		
		for (int i = 0; i < 8; i++) 
		{
			int newD = (d + i + 5) % 8 ;
			
			if ( pos[ 1 + dir[newD][0] ][ 1 + dir[newD][1] ] == '1'
			) 
			{
				printf("%d\n",  newD);
				break;
			}
		}
	}
	return 0;
}
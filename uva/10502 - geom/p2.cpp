/* 4961146   2006-09-21 06:32:23  Accepted 6.658 404 Wong Chi Fung C++ 10502 - Counting Rectangles */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

const int MAX = 120;
char map[MAX][MAX];
char str[MAX];
int row, col;
int i, j;
int width, height, top, left, bottom, right, leftend, topend;
int count, rectangle;
int area;
int x, y;
bool incomplete;

int main()
{
	while (1)
	{
		//scanf ("%d", &row);
		gets (str);
		row = atoi (str);

		if (!row) break;

		gets (str);
		col= atoi (str);

		for (i = 0; i < row; ++i)
		{
			gets (str);

			for (j = 0; j < col; ++j)
			{
				map[i][j] = str[j];
			}
		}

		rectangle = 0;

		//puts ("start");
		for (width = 1; width <= col; ++width)
		{
			for (height = 1; height <= row; ++height)
			{
				//printf ("area = %d\n\n", width * height);

				// scan all combinations of possible area
				area = width * height;
				bottom = row - height;
				
				for (top = 0; top <= bottom; ++top)
				{
					right = col - width;

					for (left = 0; left <= right; ++left)
					{
						count = 0;
						incomplete = 0;

						leftend = left + width;
						topend = top + height;

						for (x = left; !incomplete && x < leftend; ++x)
						{
							for (y = top; !incomplete && y < topend; ++y)
							{
								if (map[y][x] == '1')
								{
									++count;
								}
								else
								{
									incomplete = true;
									break;
								}
							}
						}

						if (count == area)
						{
							//printf ("take width: %d height: %d\n", width, height);
							++rectangle;
						}
					}
				}				
			}
		}

		//puts ("result");
		printf ("%d\n", rectangle);
	}
	return 0;
}
#include "stdio.h"
#include <algorithm>
//#include <vector>
//#include <cstring>
//#include "string.h"
#include <limits>

//#include <string>

#include <stdlib.h>
//#include <ctype.h>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 



int a[150][150];
char buf[400];
char* next; 

int main()
{

	int T;
	gets(buf);
	T = atoi(buf);
	//scanf("%d", &T);

	for(int t = 0; t < T; ++t)
	{
		int N;
		gets(buf);
		//scanf("%d", &N);
		N = atoi(buf);
		
		for(int i = 0; i < N; ++i)
		{
			gets(buf);
			next = buf;
			
			for(int j = 0; j < N; ++j)
			{
				//scanf("%d", &a[i][j]);
				
				a[i+N][j] = a[i][j] = strtol(next, &next, 10);
				
				if (i > 0)
					a[i][j] += a[i-1][j];
			}
		}
				
		int M = N * 2;
	
		//make each column a cumulative sum of that column
		for(int i = N; i < M; ++i)
		{
			for(int j = 0; j < N; ++j)
			{
				a[i][j] += a[i-1][j];
			}
		}
	
		int best = numeric_limits<int>::min();
		
		//int cur;
		
		//To handle wrap around
		//M 2*rows ; N rows

		for (int top=0; top < N; ++top)
		{
			for(int bot=top; bot < top + N; ++bot)
			{
				int maxSum = 0;
				int minSum = 0;
				//Pos sum can be negative, it is the attempting to be the most positive
				int posSum = 0;
				int negSum = 0;
				int total = 0;
				
				//The idea here is that either the most positive sum
				//or the most negative sum will be continuous.  This avoids having to do any wrapping
				for(int i = 0; i < N; ++i)
				{
					int colSum = a[bot][i] - (top == 0 ? 0 : a[top-1][i]);
					total += colSum;
					
					if (posSum >= 0)
					{
						posSum += colSum;
					} else {
						posSum = colSum;
					}
					
					if (negSum <= 0)
					{
						negSum += colSum;
					} else {
						negSum = colSum;
					}
					
					if (posSum > maxSum)
						maxSum = posSum;
						
					if (negSum < minSum)
						minSum = negSum;
				}
					//printf("Sum of Top row %d Bottom row %d up to col %d width %d = %d\n", top, bot, i, width, sum);
					
				//Either take best contious positive sum or the total minus the most negative continous block
				maxSum = max(maxSum, total - minSum);	
					
				if (maxSum > best)
					best = maxSum;
					
				
			}
		}
		
		//puts( itoa(best, buf, 10) );
		//puts("\n");
		printf("%d\n", best);
	}

	return 0;
}
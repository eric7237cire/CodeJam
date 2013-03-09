#include "stdio.h"
//#include <algorithm>
#include <vector>
//#include <cstring>
#include <limits>

#include <string>

#include <stdlib.h>
#include <ctype.h>

#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

typedef vector<int> vi;

//from x1 to x2 inclusive
void kadane(int input[], int n, int &x1, int &x2, int &max)
{
	int cur;
	max = 0;
	cur = 0;
	x1 = x2 = 0;
	int lx1;
	lx1 = 0;
	for(int i = 0; i<n; i++)
	{
		cur = cur+input[i];
		if(cur > max)
		{
			max = cur;
			x2 = i;
			x1 = lx1;
		}
		if (cur < 0)
		{
			cur = 0;
			lx1 = i + 1;
		}
	}
}


int a[100][100];

int main()
{

	int P;
	scanf("%d", &P);

	for(int p = 0; p < P; ++p)
	{
		int S;
		int B;
		scanf("%d%d", &S, &B);
		
		for(int i = 0; i < S; ++i)
		{
			for(int j = 0; j < S; ++j)
			{
				a[i][j] = 1;
			}
		}
		
		for(int b = 0; b < B; ++b)
		{
			int r1, c1, r2, c2;
			scanf("%d%d%d%d", &r1, &c1, &r2, &c2);
			
			for(int r = r1 - 1; r < r2; ++r)
			{
				for(int c = c1 - 1; c < c2; ++c)
				{
					a[r][c] = -1000000;
				}
			}
		}
		
		int M, N;
		M = N = S;
		

		int tmp[100], x1, x2;
		int cur, max_sum, fx1, fx2, fy1, fy2;
		
		//M rows N columns

		fx1 = fx2 = fy1 = fy2 = max_sum = cur = -1;

		for (int i=0; i< M; i++)
		{
			for(int k=0; k<N; k++)
				tmp[k] = 0;

			for (int j=i; j<M; j++)
			{
				//add row j to tmp tally
				for(int k=0; k<N; k++)
					tmp[k] += a[j][k];

				kadane(tmp, N, x1, x2, cur);

				if (cur > max_sum)
				{
					fx1 = x1;
					fx2 = x2;
					fy1 = i;
					fy2 = j;
					max_sum = cur;
				}
			}
		}
		//cout << "max Sum = " << max_sum << " from (" << fx1 << "," << fy1 << ") to ("
		//  << fx2 << "," << fy2 << ")" << endl;
		printf("%d\n", max_sum);
	}

	return 0;
}

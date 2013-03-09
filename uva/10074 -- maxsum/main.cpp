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
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif
	
	int M, N;

	while(2 == scanf("%d %d", &M, &N))
	{
		if (M == 0 && N == 0)
			break;

		for(int r = 0; r < M; ++r)
		{
			for(int c = 0; c < N; ++c)
			{
				scanf("%d", &a[r][c]);
				//Trees are bad, no trees 1
				if (a[r][c] == 1)
					a[r][c] = -100000;
				else
					a[r][c] = 1;
			}
		}

		

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

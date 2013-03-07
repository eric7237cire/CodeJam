#include "stdio.h"
//#include <algorithm>
#include <vector>
//#include <cstring>
#include <limits>

#include <string>

#include <stdlib.h>
#include <ctype.h>



using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

typedef vector<int> vi;

void kadane(int input[], int n, int &x1, int &x2, int &max)
{
	int cur;
	max = 0;
	cur = 0;
	x1 = x2 = 0;
	int lx1, lx2;
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




int main()
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif
	char buf[30];

	gets(buf);
	int T = atoi(buf);

	gets(buf);

	FOR(t, 0, T)
	{


		int sq[25][25];
		int maxArea[25][25];

		int r = 0;
		while( NULL != gets(buf) && buf[0] != '\0')
		{
			int c = 0;
			while(buf[c] != '\0')
			{
				sq[r][c] = buf[c] == '1' ? 1 :  -2000;
				++c;
			}
			++r;
		}

		int tmp[100], n, x1, x2;
		int cur, max_sum, fx1, fx2, fy1, fy2;
		//int i,j,k;
		int M, N;
		M = N = r; 
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
					tmp[k] += sq[j][k];

				kadane(tmp, N, x1, x2, cur);

				if (cur > max_sum)
				{
					fy1 = x1;
					fy2 = x2;
					fx1 = i;
					fx2 = j;
					max_sum = cur;
				}
			}
		}
		//cout << "max Sum = " << max_sum << " from (" << fx1 << "," << fy1 << ") to ("
		  //<< fx2 << "," << fy2 << ")" << endl;
		if (t > 0)
			printf("\n");
		printf("%d\n", max_sum);
	}

	return 0;
}

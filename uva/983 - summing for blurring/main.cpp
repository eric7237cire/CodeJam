#include "stdio.h"
//#include <algorithm>
//#include <vector>
//#include <cstring>
//#include <limits>

//#include <string>

#include <stdlib.h>
//#include <ctype.h>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

//typedef vector<int> vi;
//typedef vector<vi> vvi;

char buf[128];
	
int sum[1001][1001];

int main()
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif
	
	int N, M;
	int t = 0;
	

	while(2 == scanf("%d %d", &N, &M))
	{
		if (++t > 1)
			printf("\n");

		//vvi a(N, vi(N));
		//+1 to avoid special cases
		//vvi sum(N+1, vi(N+1, 0));
		gets(buf);
		/*
		    c 1 c 2 c 3
		r 3  
		r 2
		r 1
		*/
		for(int r=1; r <= N; ++r)
		{
			for(int c=1; c <= N; ++c)
			{
				int a;
				gets(buf);
				a = atoi(buf);
				//scanf("%d", &a);
				sum[r][c] = a;
				sum[r][c] += sum[r-1][c];
				sum[r][c] += sum[r][c-1];
				sum[r][c] -= sum[r-1][c-1];
			}
		}

		int blurSize = N - M + 1;

		int total = 0;

		for(int r = 1; r <= blurSize; ++r)
		{
			for(int c = 1; c <= blurSize; ++c)
			{
				/*

				11222
				11222
				33444
				33444

				square 4 overlaps everything
				square 2 overlaps square 1
				square 3 overlaps square 1
				*/
				int sq4 = sum[r+M-1][c+M-1];
				int sq3 = sum[r+M-1][c-1];
				int sq2 = sum[r-1][c+M-1];
				int sq1 = sum[r-1][c-1];

				int sq = sq4 - sq3 - sq2 + sq1;
				printf("%d\n", sq);
				total += sq;		
			}
			//cout << endl;
		}
		printf("%d\n", total);

		
	}

	return 0;
}

#if 0
int mainNonOpt()
{

	
	int N, M;
	int t = 0;

	while(2 == scanf("%d %d", &N, &M))
	{
		if (++t > 1)
			cout << endl;

		//vvi a(N, vi(N));
		//+1 to avoid special cases
		vvi sum(N+1, vi(N+1, 0));

		/*
		    c 1 c 2 c 3
		r 3  
		r 2
		r 1
		*/
		for(int r=1; r <= N; ++r)
		{
			for(int c=1; c <= N; ++c)
			{
				int a;
				scanf("%d", &a);
				sum[r][c] = a;
				sum[r][c] += sum[r-1][c];
				sum[r][c] += sum[r][c-1];
				sum[r][c] -= sum[r-1][c-1];
			}
		}

		int blurSize = N - M + 1;

		int total = 0;

		for(int r = 1; r <= blurSize; ++r)
		{
			for(int c = 1; c <= blurSize; ++c)
			{
				/*

				11222
				11222
				33444
				33444

				square 4 overlaps everything
				square 2 overlaps square 1
				square 3 overlaps square 1
				*/
				int sq4 = sum[r+M-1][c+M-1];
				int sq3 = sum[r+M-1][c-1];
				int sq2 = sum[r-1][c+M-1];
				int sq1 = sum[r-1][c-1];

				int sq = sq4 - sq3 - sq2 + sq1;
				cout << sq << endl;
				total += sq;		
			}
			//cout << endl;
		}
		cout << total << endl;

		
	}

	return 0;
}

#endif
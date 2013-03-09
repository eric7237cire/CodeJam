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


int a[10000];

int main()
{

	int N;
	while(1 == scanf("%d", &N) && N > 0)
	{
		
		
		for(int i = 0; i < N; ++i)
		{
			scanf("%d", &a[i]);			
		}
		
		
		int x1, x2;
		int max_sum;
		
		kadane(a, N, x1, x2, max_sum);
		
		if (max_sum > 0)
			printf("The maximum winning streak is %d.\n", max_sum);
		else
			printf("Losing streak.\n");

	}

	return 0;
}

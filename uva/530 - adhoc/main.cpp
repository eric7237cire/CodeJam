#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <cassert>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
const int MAX_N = 50;
int V[MAX_N];
int N;

int main()
{
	int T;
	scanf("%d", &T);
	
	for(int t = 1; t <= T; ++t)
	{
		int a, b;
		scanf("%d%d", &a, &b);
		
		int s = 0;
		for(int i = a; i <= b; ++i)
		{
			if (i % 2 == 0)
				continue;
			s += i;
		}
			
		printf("Case %d: %d\n", t, s);

	}
	
	
	return 0;
}
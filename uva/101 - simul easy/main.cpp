#include "stdio.h"
#include <algorithm>
#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <list>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

typedef vector<int> vi;
typedef vector<vi> vvi;
typedef list<int> li;


#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)

const int MAX_N = 24
int loc[MAX_N];

li piles[MAX_N];

int N;

char cmd1[10];
char cmd2[10];
int b1, int b2;

int main()
{
	int T;
	
	
	while(1 == scanf("%d", &N))
	{
		scanf("%d", &N);
		
		
		
		FOR(i, 0, N)
		{
			piles[i].clear();
			piles[i].push_back(i+1);
			assert(piles.size() == 1);
		}
		
		while(4 == scanf("%s %d %s %d"), &cmd1, &b1, &cmd2, &b2)
		{
			printf("Read %s %d - %s %d\n",
				cmd1, b1, cmd2, b2);
		}
		
		int r = scanf("%s", &cmd1);
		
		assert(str
		
		
		while(trains.size() > 1)
		{
			list<int>::iterator it = find(trains.begin(), trains.end(), trains.size() );
			
			assert(it != trains.end());
			
			ans += -1 + distance(it, trains.end() );
			
			trains.erase(it);
		}
		
		printf("Optimal train swapping takes %d swaps.\n", ans);
	}

	return 0;
}
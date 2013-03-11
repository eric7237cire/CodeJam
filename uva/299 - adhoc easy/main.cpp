#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <list>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back
 
int main()
{
	int T;
	scanf("%d", &T);
	
	while(T--)
	{
		int N;
		scanf("%d", &N);
		
		list<int> trains;
		
		while(N--)
		{
			int train;
			scanf("%d", &train);
			
			trains.push_back(train);
		}
		
		int ans = 0;
		
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
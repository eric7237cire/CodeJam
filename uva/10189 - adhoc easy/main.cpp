#include "stdio.h"
//#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
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
		int f;
		scanf("%d", &f);
		
		int ans = 0;
		while(f--)
		{
			int area, anim, env;
			scanf("%d%d%d", &area, &anim, &env);
			ans += area * env; //anim cancels out
		}
		
		printf("%d\n", ans);		
	}

	return 0;
}
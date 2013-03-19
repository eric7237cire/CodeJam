#include "stdio.h"

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
 
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
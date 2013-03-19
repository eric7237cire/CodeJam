#include "stdio.h"

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
 
int main()
{
	int T;
	scanf("%d", &T);
	
	int n, m;
	while(T--)
	{
      scanf("%d %d",&n,&m);
      printf("%d\n",(n/3)*(m/3));
    }
 

	return 0;
}
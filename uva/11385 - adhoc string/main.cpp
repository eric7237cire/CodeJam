#include "stdio.h"
#include <cstring>
#include <map>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
 
char buf[101];
char ans[101];
typedef unsigned int ui;
typedef long long ll;
typedef map<int, int> mii;

ui fib[46] = {1, 1};
mii fibPos;

int pos[100];

int main()
{
	fibPos[1] = 1;
	for(int i = 2; i < 46; ++i)
	{
		
		fib[i] = fib[i-2] + fib[i-1];
		fibPos[ fib[i] ] = i;
		//printf("%d: %u %u %lld \n", i, fib[i], 1U << 31, (ll) (1U << 31) - (ll) fib[i] );
	}
	
	int T;
	scanf("%d", &T);
	
	while(T--)
	{
		int nPos;
		scanf("%d", &nPos);

		int fibNum;
		int maxPos = 0;
		for(int i = 0; i < nPos; ++i)
		{
			scanf("%d", &fibNum);
			mii::iterator mit = fibPos.find(fibNum);
			pos[i] = mit->second;
			maxPos = max(maxPos, pos[i]);
			//printf("fibNum %d  pos=%d\n", fibNum, pos[i]);
		}
		
		gets(buf);
		int len = strlen(buf);
		if (len == 0)
		{
			gets(buf);
			len = strlen(buf);
		}
		//printf("%s\n", buf);
		
		for(int i = 0; i < maxPos; ++i)
		{
			ans[i] = ' ';
		}
		ans[maxPos] = '\0';
		
		int ansPos = 0;
		for(int i = 0; i < len; ++i)
		{			
			if (buf[i] < 'A' || buf[i] > 'Z')
				continue;
			
			//printf("Ans pos %d pos[] = %d  i=%d %c\n", ansPos, pos[ansPos], i, buf[i]);
			ans[ pos[ansPos] - 1 ] = buf[i];
			++ansPos;
			if (ansPos >= nPos)
				break;
		}
		
		printf("%s\n", ans);
		//printf("[%s]  maxPos %d\n", ans, maxPos);
	}

	return 0;
}
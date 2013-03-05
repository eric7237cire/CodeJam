#include "stdio.h"
#include <algorithm>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	int seq1[10000];

	int dp1[10000];
	int dp2[10000];
	
	int lis[10000];

	int N;	

	while(1 == scanf("%d", &N))
	{
		FOR(i, 0, N)
		{
			scanf("%d", &seq1[i]);
		}
				
		for(int choice = 0; choice <= 1; ++choice)
		{
			
			int* dp = choice == 0 ? dp1 : dp2;
			int* seq = choice == 0 ? seq1 : seq1 + N - 1;

			dp[0] = 1;
			lis[0] = *seq;
			int lenLis = 1;
			
			for(int dpIdx = 1; dpIdx < N; ++dpIdx)
			{
				if (choice == 0)
				{++seq;}
				else
				{--seq;}

				//Eveything before it is strictly less than seq[dpIdx]
				int* it = lower_bound( lis, lis + lenLis, *seq);

				int idx = it - lis;
				
				if (idx == lenLis)
				{
					lis[lenLis++] = *seq;
				} else {
					//Found an element 
					*it = *seq;
				}
					
				dp[dpIdx] = 1 + idx;
			}
		}

		//cout << seq1 << endl;
		//cout << dp1 << endl;
		//cout << dp2 << endl;
		
		int ans = 0;
		int e ;
		int* pDp1 = dp1;
		int* pDp2 = dp2 + N - 1;
		int* pEnd = dp1 + N;

		while(pDp1 != pEnd)
		{
			e = min( *pDp1++, *pDp2--);
			if (e > ans)
				ans = e;			
		}

		
		printf("%d\n", 2 * ans - 1);
		

	}

	

	return 0;
}

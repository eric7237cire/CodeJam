#include "stdio.h"
#include <algorithm>
#include <cstring>
using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back 

int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	char buf[2524*1024]; 
	long n=fread(buf,1, sizeof(buf), stdin); 
	buf[n]='\0'; 

	int seq1[10000];

	int dp1[10000];
	int dp2[10000];
	
	int lis1[10000];
	int lis2[10000];

	int N;	

	//char arr[132200];

	char* next = buf;

	//strtol(buf, &next, 10)

	while(true)
	//while(1 == scanf("%d", &N))
	{
		N = strtol(next, &next, 10);

		if (N <= 0)
			return 0;
		
		
		FOR(i, 0, N)
		{
			//scanf("%d", &seq1[i]);
			seq1[i] = strtol(next, &next, 10);
		}
				
			
		//int* pDp1 = dp1;
		int* pSeq1 = seq1;

		dp1[0] = 1;
		lis1[0] = *pSeq1;
		int lenLis1 = 1;

		
		int* pSeq2 = seq1 + N - 1;

		dp2[0] = 1;
		lis2[0] = *pSeq2;
		int lenLis2 = 1;
		
			
		for(int dpIdx = 1; dpIdx < N; ++dpIdx)
		{
			++pSeq1;

			//Eveything before it is strictly less than seq[dpIdx]
			int* it = lower_bound( lis1, lis1 + lenLis1, *pSeq1);

			int idx = it - lis1;
				
			if (idx == lenLis1)
			{
				lis1[lenLis1++] = *pSeq1;
			} else {
				//Found an element 
				*it = *pSeq1;
			}
					
			dp1[dpIdx] = 1 + idx;

			--pSeq2;

			//Eveything before it is strictly less than seq[dpIdx]
			it = lower_bound( lis2, lis2 + lenLis2, *pSeq2);

			idx = it - lis2;
				
			if (idx == lenLis2)
			{
				lis2[lenLis2++] = *pSeq2;
			} else {
				//Found an element 
				*it = *pSeq2;
			}
					
			dp2[dpIdx] = 1 + idx;
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

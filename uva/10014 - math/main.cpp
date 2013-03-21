//STARTCOMMON
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

typedef unsigned long long ull;

using namespace std;
//STOPCOMMON
#include <vector>
#include "stdio.h"
int main() {

	int T;
	scanf("%d", &T);

	//(n+1)A1 = nA0 + An+1 - 2Cn - 4Cn-1 - 6Cn-2 - ... - (2n)C1

	while(T--)
	{
		
		int N;
		double A0;
		double An1;
		scanf("%d %lf %lf", &N, &A0, &An1);
		
		double sum = 0;
		int coef = -2*N;
		double Ci;
		vector<double> cvec;
		FOR(i, 0, N)
		{
			scanf("%lf", &Ci);
			cvec.push_back(Ci);
			sum += coef * Ci;
			
			coef += 2;
		}
		
		sum += N*A0 + An1;
		
		printf("%.2lf\n", sum / (N+1));
		if (T > 0)
			printf("\n");
			
		/*
		double A1 = sum / (N+1);
		double cur = A1;
		double last = A0;
		FOR(i, 0, N)
		{
			double next = 2 *(cur + cvec[i]) - last;
			last = cur;
			cur = next;
		}

		printf("Cur %lf last %lf An1 %lf\n", cur, last, An1);
		*/
		//scanf("%d", &nSeg);
		
	}
	return 0;
}

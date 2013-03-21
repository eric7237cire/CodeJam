//STARTCOMMON
#include <cmath>

#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

typedef unsigned long long ull;

const double tolerance = 0.000002;
template<typename T> 
int cmp(T a, T b, T epsilon = tolerance)
{
	T dif = a - b;
	if (abs(dif) <= epsilon)
	{
		return 0;
	}
	
	if (dif > 0)
	{
		return 1; //a > b
	}
	
	return -1;
}  

using namespace std;
//STOPCOMMON

#include "stdio.h"
ull N;

int main() {

	int T;
	scanf("%d", &T);

	while(T--)
	{
		
		scanf("%llu", &N);
		
		if (N == 0)
		{
			printf("0\n");
			continue;
		}
		
		double rows = (-1 + sqrt(1.0l + 8*N)) / 2.0;
		
		ull rowsInt = (ull) rows;
		
		printf("%llu\n", rowsInt);
		
	}
	return 0;
}

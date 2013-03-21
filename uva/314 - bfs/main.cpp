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
int main() {

	int M, N;
	scanf("%d", &T);

	while(T--)
	{
		
		if (T > 0)
			printf("\n");

		//scanf("%d", &nSeg);
		
	}
	return 0;
}

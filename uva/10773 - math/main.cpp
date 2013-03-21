//STARTCOMMON
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
#include <cmath>
int main() {

	int T;
	scanf("%d", &T);

	FOR(t, 1, T+1)
	{
		double d,v,u;
		scanf("%lf%lf%lf", &d, &v, &u);
		
		printf("Case %d: ", t);
		
		if (v >= u || u == 0 || v == 0)
		{
			puts("can't determine");
			continue;
		}
		
		double tFast = d / u;
		
		double vHor = sqrt(u*u - v*v);
		
		double tSlow = d / vHor;
		
		printf("%.3lf\n", tSlow - tFast);
		//scanf("%d", &nSeg);
		
	}
	return 0;
}

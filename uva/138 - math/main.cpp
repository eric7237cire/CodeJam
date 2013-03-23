//STARTCOMMON
#include <cmath>
#include "stdio.h" 
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
int R[20] = {
6,8,
35,49,
204,288,
1189,1681,
6930,9800,
40391,57121,
235416,332928,
1372105,1940449,
7997214,11309768,
46611179,65918161
};
int main() 
{
    int i;    
    for(i = 0; i<20; i+= 2)
        printf("%10d%10d\n",R[i],R[i+1]);
    return 0;
}     
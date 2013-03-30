//STARTCOMMON
#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <cstring>

using namespace std;

#define mp make_pair 
#define pb push_back 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define all(c) (c).begin(),(c).end() 
#define MIN(a,b) ((a)<(b)?(a):(b))
#define MAX(a,b) ((a)<(b)?(b):(a))

typedef unsigned int uint;
typedef long long ll;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<bool> vb;
typedef vector<vb> vvb;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
typedef vector<ii> vii;
typedef vector<vii> vvii;


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


//STOPCOMMON

ll memo[2][66][66];

int N, S;

/*
Go from top to bottom door.  index 0 means topmost door
*/
ll calc( bool lastDoorSecured, int doorsLeft, int leftToSecure )
{
	ll& ret = memo[lastDoorSecured][doorsLeft][leftToSecure];
	
	if (ret != -1)
		return ret;
		
	//Base cases 
	
	//Only 1 way, all must be locked
	if (lastDoorSecured && doorsLeft == leftToSecure )
		return ret = 1;
		
	//Impossible
	if (leftToSecure > doorsLeft)
		return ret = 0;
		
	if (doorsLeft == 1)
	{
		if (lastDoorSecured && leftToSecure <= 1)
			return ret = 1;  // leftToSecure == 0 means we must unlock the last door, otherwise its locked
		else if (!lastDoorSecured && leftToSecure == 0)
			return ret = 2;  //locked and unlocked are both ok
		else 
			return ret = 0;
	}	
	
	ret = 0;
	if (lastDoorSecured)
	{
		//secure current door
		if (leftToSecure > 0)
			ret += calc( true, doorsLeft - 1, leftToSecure - 1);
			
		//do not secure
		ret += calc( false, doorsLeft - 1, leftToSecure );
		return ret;
	} else {
		//secure current door, doesn't count as secured
		ret += calc( true, doorsLeft - 1, leftToSecure );
			
		//do not secure
		ret += calc( false, doorsLeft - 1, leftToSecure );
		return ret;
	
	}
}

int main()
{
	memset(memo, -1, sizeof(memo));
	
	while(2 == scanf("%d%d", &N, &S) && (N>=0 || S >= 0))
	{
		ll ans = calc(1, N, S);
		printf("%lld\n", ans);
	}
	return 0;
}

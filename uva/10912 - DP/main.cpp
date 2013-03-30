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


/*
 dp[lastLetter][length][goal]
 
 26 'items'
 
*/
int memo[26][27][352];

/*
minLetter is 0 based, 1 = a, 25 = z
*/
int topDown( int minLetter, int lenRemaining, int sumRemaining)
{
	
	//assert(minLetter >= 0 && minLetter < 26);
	//assert(lenRemaining >= 0 && lenRemaining < 27);
	//assert(sumRemaining >= 0 && sumRemaining < 352);
	
	int& ret = memo[minLetter][lenRemaining][sumRemaining];
	
	if (ret >= 0)
		return ret;
		
	if (1 == lenRemaining)
	{
		return ret = sumRemaining > minLetter && sumRemaining <= 26 ? 1 : 0;
	}
	
	ret = 0;
	//24 is maximum as we must reserve z when length = 1
	const int maxLetter = min(24, sumRemaining - (lenRemaining - 1) );
	for(int letter = minLetter; letter <= maxLetter; ++letter )
	{
		ret += topDown( letter + 1, lenRemaining - 1, sumRemaining - (letter + 1) );
	}
	return ret;
}

int main() {

	int L, S;

	memset(memo, -1, sizeof memo);
	int t = 0;
	while(2 == scanf("%d%d", &L, &S) && (L|S))
	{		
		if (S >= 352 || L > 26 || L <= 0 )
		{
			printf("Case %d: 0\n", ++t);
			continue;
		}
		int ans = topDown(0, L, S);
		printf("Case %d: %d\n", ++t, ans);
	}
	return 0;
}

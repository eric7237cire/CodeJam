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

int N, K, M;

int dp[51][51];

ll memo[51][51];

ll topDown( int lengthLeft, int blocksLeft )
{
	if (lengthLeft < 0 || blocksLeft < 0)
		return 0;
		
	if (blocksLeft == 1 && lengthLeft <= M && lengthLeft >= 1)
		return 1;
	else if (blocksLeft <= 1)
		return 0;
		
	ll& ret = memo[lengthLeft][blocksLeft];
	
	if (ret != -1)
		return ret;
		
	ll sum = 0;
	for(int blockSize = 1; blockSize <= M; ++blockSize)
	{
		ll ind = topDown( lengthLeft - blockSize, blocksLeft - 1 );
		
		if (false) printf("topDown len remaining=%d blocksleft=%d.  Using blockSize %d , len left %d and blocksleft %d, total %lld\n",
			lengthLeft, blocksLeft, blockSize, 
			lengthLeft - blockSize, blocksLeft -1, ind);
		
		sum += ind;
	}
	
	return ret = sum;
}

/*
1000100 | 4: 1001110 |  | 
 1: 1000110 | 5: 1011000 |  | 
 2: 1001000 | 6: 1011100 |  | 
 3: 1001100 |  | | 
 

 7:  1100010
 11: 1101100 
 10: 1101000
 9:  1100110
 8:  1100100
 12: 1101110
 
  15: 1110110
 14:  1110100
 13: 1110010
 */

int main() {

	while(3 == scanf("%d%d%d", &N, &K, &M))
	{
		memset( dp, -1, sizeof dp);
		memset( memo, -1, sizeof memo);
		
		ll total = topDown(N, K);

		printf("%lld\n", total);
	}
	return 0;
}

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
int dp[10001];
int seq[2];


int main() {

	int M, N, T;
	while( 3 == scanf("%d%d%d", &seq[0], &seq[1], &T))
	{
		memset( dp, -1, sizeof dp);
		
		dp[0] = 0;
		for(int seqIdx = 0; seqIdx <= 1; ++seqIdx)
		{
			int blockTime = seq[seqIdx];
			
			for(int time = blockTime; time <= T; ++time)
			{
				int startTime = time - blockTime;
				if (dp[startTime] == -1)
					continue;
					
				dp[time] = max(dp[time], 1 + dp[startTime] );
			}
		}
		
		if (dp[T] != -1)
		{
			printf("%d\n", dp[T]);
			continue;
		}
		
		//minimize time leftover
		int t = T;
		while(dp[t] == -1) {
			--t;
		} 
		
		printf("%d %d\n", dp[t], T-t);
		
	}
	return 0;
}

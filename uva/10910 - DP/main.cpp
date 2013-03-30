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

//dp[N][total] = ways to make total with N items with minimum of P
int dp[71][71];

int main() {

	int T;
	scanf("%d", &T);

	while(T--)
	{
		int N, T, P;
		scanf("%d%d%d", &N, &T, &P);
		
		memset( dp, 0, sizeof(dp) );
		
		for(int itemWeight= P; itemWeight <= T; ++itemWeight)
		{
			dp[1][itemWeight] = 1;
		}
		
		for(int nItems = 2; nItems <= N; ++nItems)
		{
			for(int totalWeight = P; totalWeight <= T; ++totalWeight)
			{
				const int maxItemWeight = totalWeight - (nItems - 1) * P;
				for(int itemWeight= P; itemWeight <= maxItemWeight; ++itemWeight)
				{
					dp[nItems][totalWeight] += dp[nItems - 1][totalWeight-itemWeight];
				}
				
			}
		}
		
		printf("%d\n", dp[N][T]);
		
	}
	return 0;
}

//STARTCOMMON
#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>

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

const int SIZE_X = 1001;
const int SIZE_Y = 9;

int dp [SIZE_X][SIZE_Y];
int wind[SIZE_X][SIZE_Y];

//Because we are adding, can't use max int  just needs to be more than 1000 * 65
const int INF = 1000000;
int X;

int main() {

	int T;
	scanf("%d", &T);

	while(T--)
	{
		//memset( dp, -1, sizeof(dp) );
		
		scanf("%d", &X);
		
		X /= 100;
		
		for(int y = 9; y >= 0; --y) FOR(x, 0, X)
		{
			scanf("%d", &wind[x][y]);
			dp[x][y] = INF;
			//printf("Wind at x=%d y=%d is %d\n", x, y, wind[x][y]);
		}
		
		FORE(y, 0, 9)
			dp[X][y] = INF;
			
		dp[X][0] = 0;
		
		for(int x = X-1; x >= 0; --x)
		{
			for(int y = 0; y < 10; ++y)
			{
				//climb
				if (y < 8)
					dp[x][y] = min(dp[x][y], -wind[x][y] + 60 + dp[x+1][y+1]);
					
				//straight
				dp[x][y] = min(dp[x][y], -wind[x][y] + 30 + dp[x+1][y]);
				
				//dip
				if (y > 0)
					dp[x][y] = min(dp[x][y], -wind[x][y] + 20 + dp[x+1][y-1]);
					
				//printf("x=%d y=%d  dp[x][y] = %d\n", x, y, dp[0][0]);
			}
		}
		
		printf("%d\n\n", dp[0][0]);
		
	}
	return 0;
}

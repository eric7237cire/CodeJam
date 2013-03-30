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

int powerIdx[20];
int votes[20];
int P;

int main() {

	int T;
	scanf("%d", &T);

	while(T--)
	{
		scanf("%d", &P);
		
		int total = 0;
		FOR(p, 0, P)
		{
			scanf("%d", &votes[p]);
			total += votes[p];
		}
		
		int maj = total / 2 + 1;
		
		memset(powerIdx, 0, sizeof powerIdx);
		
		//Go through all the possible subsets
		for(int ss = 0; ss < 1 << P; ++ss)
		{
			//get the subset total
			int ssTotal = 0;
			for(int p = 0; p < P; ++p)
			{
				if ( (1 << p & ss) == 0)
					continue;
				
				ssTotal += votes[p];
			}
			
			//If subset is already a majority, skip it 
			if (ssTotal >= maj)
				continue;
		
			//Now go through all parties not in the set 
			for(int p = 0; p < P; ++p)
			{
				if ( (1 << p & ss) != 0)
					continue;
				
				if ( ssTotal + votes[p]  >= maj ) 
				{
					powerIdx[p] ++;
				}
			}
		}
		
		FOR(p, 0, P)
		{
			printf("party %d has power index %d\n", p+1, powerIdx[p]);
		}
		
		puts("");
		
	}
	return 0;
}

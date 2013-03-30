//STARTCOMMON
#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <cstring>
#include <algorithm>

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
int N;
int M;

ii people[20];
int dist[20];

int perm[20];

int main() {

	while( 2 == scanf("%d%d", &N, &M) && (N||M))
	{
		FOR(m, 0, M)
		{
			scanf("%d%d%d", &people[m].first, &people[m].second, &dist[m]);
		}
		
		FOR(n, 0, N)
			perm[n] = n;
			
		int count = 0;
		do 
		{
			bool ok = true;
			FOR(m, 0, M)
			{
				int curPos1 = perm[ people[m].first ] ;
				int curPos2 = perm[ people[m].second ] ;
				
				if (dist[m] >= 0 )
				{
					if ( abs(curPos1 - curPos2) > dist[m])
					{
						ok = false;
						break;
					}						
				} else {
					if ( abs(curPos1 - curPos2) < -dist[m])
					{
						ok = false;
						break;
					}						
				}
			}
			
			count += ok;
		
		} while (next_permutation(perm, perm + N) );
	
		printf("%d\n", count);
	}
	return 0;
}

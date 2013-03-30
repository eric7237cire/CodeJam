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
int F;
int R;
int front[10];
int rear[10];

#include <algorithm>
double ratios[100];

int main() {

	while( scanf("%d%d", &F, &R) == 2 && F)
	{
		FOR(f, 0, F)
			scanf("%d", &front[f]);
		
		FOR(r, 0, R)
			scanf("%d", &rear[r]);
		
		FOR(f, 0, F) FOR(r, 0, R)
			ratios[ f * R + r ] = (double) rear[r] / front[f];
			
		sort( ratios, ratios + F * R );
		
		double maxSpread = 0;
		
		FOR( rat, 1, F * R )
		{
			double spread = ratios[rat] / ratios[rat - 1];
			maxSpread = max(maxSpread, spread);
		}
		
		printf("%.2lf\n", maxSpread);
		
	}
	return 0;
}

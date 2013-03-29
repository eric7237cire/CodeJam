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

#include <sstream>
#include <cstring>
#include <algorithm>

const int MAX_N = 32000;
const int SIZE = 100;

int seq[SIZE];

int memo[SIZE+1][2 * MAX_N + 1];
//int next[SIZE+1][2 * MAX_N + 2];

int maxIndex;
int target;
vector<string> ans;


bool getExp( int curIdx, int curVal )
{
	if (curVal < -32000 || curVal > 32000)
		return false;
		
	//printf("idx = %d ; num =%d  cur Val %d  \n", curIdx, seq[curIdx], curVal);
	int& ret = memo[curIdx][curVal];
	
	if (ret != -1 && ret == 0)
		return false;
	
	if (curIdx == maxIndex)
		return ret = curVal == target;

	ostringstream os; 
	if ( getExp( curIdx + 1, curVal + seq[curIdx] ) )
	{
		os << '+' << seq[curIdx] ;
		ans.pb(os.str());
		return ret = true;
	}
	if ( getExp( curIdx + 1, curVal - seq[curIdx] ) )
	{
		os << '-' << seq[curIdx] ;
		ans.pb(os.str());
		return ret = true;
	}
	if ( curVal % seq[curIdx] == 0 && getExp( curIdx + 1, curVal / seq[curIdx] ) )
	{
		os << '/' << seq[curIdx] ;
		ans.pb(os.str());
		return ret = true;
	}
	if ( getExp( curIdx + 1, curVal * seq[curIdx] ) )
	{
		os << '*' << seq[curIdx] ;
		ans.pb(os.str());
		return ret = true;
	}
	
	return ret = false;
}

int main() {

	int T;
	scanf("%d", &T);

	while(T--)
	{
		
		scanf("%d", &maxIndex);
		
		FOR(i, 0, maxIndex)
			scanf("%d", &seq[i]);
			
		
		scanf("%d", &target);
		
		memset(memo, -1, sizeof memo);
		//memset(next, 0, sizeof next);
		
		//dp[size][ target + 32001 ] = target;
		
		//ans.str ( string() );
		//ans.clear();
		ans.clear();
		bool ok = getExp(1, seq[0]);
		
		if (!ok)
		{
			puts("NO EXPRESSION");
			continue;
		}
		
		
		cout << seq[0];
				
		for(int i = ans.size() - 1; i >= 0; --i)
			cout << ans[i];
			
		cout << '=' << target << endl;
		//string ansStr = ans.str();
		//reverse(all(ansStr));
		
		//cout << ansStr << endl;
		
		
	}
	return 0;
}

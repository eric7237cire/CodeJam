/*
ID: eric7231
PROG: bigbrn
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
//#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <numeric>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<bool> vb;
typedef vector<vb> vvb;
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef vector<pair<int,int> > vii;
typedef vector<vii> vvii;
typedef pair<uint,uint> uu;
typedef map<string, int> msi;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 



int main(){
    
	freopen("bigbrn.in","r",stdin);
	freopen("bigbrn.out","w",stdout);
	
	int N, T;
	scanf("%d%d", &N, &T);
	
	vvb grid(N, vb(N, true));
	int R, C;
	while( 2 == scanf("%d%d", &R, &C) )
	     grid[R-1][C-1] = false;

	vvi square(N, vi(N, 0));

    int maxSize = 0;
	
	FOR(r, 0, N) FOR(c, 0, N)
	{
	    if (!grid[r][c])
	    {
	        square[r][c] = 0;
	        continue;
	    }
	    int above = r > 0 ? square[r-1][c] : 0;
	    int left = c > 0 ? square[r][c-1] : 0;
	    int abLeft = r > 0 && c > 0 ? square[r-1][c-1] : 0;
	    
	    square[r][c] = 1 + min( min(above, left), abLeft);
	    maxSize = max(maxSize, square[r][c]);
	}
	
	printf("%d\n", maxSize);
	return 0;
}

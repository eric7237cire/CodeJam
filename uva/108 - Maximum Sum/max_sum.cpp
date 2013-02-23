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
#include <complex>
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

int main() 
{
#ifndef ONLINE_JUDGE
	freopen ("input.txt","r",stdin);
#endif

	
	int N;
	scanf("%d", &N);
		
	vvi matrix(N, vi(N));
		
		
	FOR(r, 0, N) FOR(c, 0, N)
	{
		scanf("%d", &matrix[r][c]);

		if (r > 0) // 2nd row onwards
			matrix[r][c] += matrix[r - 1][c]; // add from top
		if (c > 0) // 2nd col onwards
			matrix[r][c] += matrix[r][c - 1]; // add form left
		if (r > 0 && c > 0)
			matrix[r][c] -= matrix[r - 1][c - 1]; // to avoid double count
	}

	int maxSubRect = numeric_limits<int>::min();


	//I, J is top row, top col ; k l is bottom right
	FOR(i, 0, N) FOR(j, 0, N)
	FOR(k, i, N) FOR (l, j, N) 
	{
		int subRect = matrix[k][l];
		if (i > 0) subRect -= matrix[i - 1][l]; //remove above
		if (j > 0) subRect -= matrix[k][j - 1]; //remove left
		if (i > 0 && j > 0) subRect += matrix[i - 1][j - 1]; //removed top/left 2x, so add back once
		maxSubRect = max(maxSubRect, subRect);
	}

	printf("%d\n", maxSubRect);
	
	return 0;
}

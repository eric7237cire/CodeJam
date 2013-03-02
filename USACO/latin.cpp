/*
 ID: eric7231
 PROG: latin
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
typedef pair<int, int> ii;
typedef vector<pair<int, int> > vii;
typedef vector<vii> vvii;
typedef pair<uint, uint> uu;
typedef map<string, int> msi;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

int N;

vi colMasks;
vi rowMasks;

vi fact;

int search(int curCol, int curRow) {
	if (curRow == N - 1 ) {
		//printf("huh %d\n", curIndex);
		return 1;
	}

	if (curCol == N)
	{
		//First column is already filled in
		return search(1, 1+curRow);
	}



	/*
	 printf("Cur idx %d row %d col %d\n", curIndex, curRow, curCol);
	 FOR(r, 0, N)
	 {
	 FOR(c, 0, N)
	 {
	 printf("%d", grid[r][c]);
	 }
	 printf("\n");
	 }*/

	int s[2] = { -1, -1 };
	int t = 0;
	int sum = 0;

	FOR(i, 0, N)
	{
		if ((1 << i & rowMasks[curRow]) != 0)
			continue;
		if ((1 << i & colMasks[curCol]) != 0)
			continue;

		if (i > curCol)
			t = 1;

		if (curRow == 1 && s[t] != -1) {
					sum += s[t];
		}
		else {
			//grid[curRow][curCol] = i+1;
			rowMasks[curRow] |= 1 << i;
			colMasks[curCol] |= 1 << i;

			s[t] = search(1 + curCol, curRow);
			sum += s[t];

			// grid[curRow][curCol] = 0;
			rowMasks[curRow] &= ~(1 << i);
			colMasks[curCol] &= ~(1 << i);
		}
	}

	return sum;
}

int main()
{

	freopen("latin.in", "r", stdin);

	scanf("%d", &N);

	//grid.resize(N, vi(N, 0));
	colMasks.resize(N, 0);
	rowMasks.resize(N, 0);
	fact.resize(N+1, 0);

	fact[0] = 1;

	/*
	 * To save time, the first row is also preset to 1 2 3 .. N
	 * to get the final count, the 2nd to N row can be permuted
	 * 12345
	 * 2
	 * 3
	 * 4
	 * 5
	 *
	 * 4! times the count
	 */
	FOR(c, 0, N)
	{
		colMasks[c] |= 1 << c;
		rowMasks[c] |= 1 << c;
		fact[c+1] = fact[c] * (c+1);
	}

	ull tally = (ull) search(1, 1) * fact[N-1];

	freopen("latin.out","w",stdout);
	printf("%lld\n", tally);
	return 0;
}



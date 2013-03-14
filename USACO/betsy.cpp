/*
ID: eric7231
PROG: betsy
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <limits>
#include <vector>
#include <algorithm>
#include <cassert>
using namespace std;

typedef unsigned long long ull;

int N;

int getIndex(int row, int col)
{
	return row * N + col;
}

int dir[4][2]  =
	{ { 1, 0 },
	{ 0, 1 },
	{ -1, 0 },
	{ 0, -1 } };

int popCount( ull num )
{
	int pc = 0;
	while( num > 0 )
	{
		if (num & 1UL)
			pc ++;
			
		num >>= 1;
	}	
	return pc;
}
	
ull count( const ull visited, const int row, const int col, int nVisited )
{
	int curIdx = getIndex(row, col);
	
	//cout << "Visited " << visited << " pop count " << popCount(visited) << " nVisited " << nVisited << endl;
	
	int pc = popCount(visited);
	//printf("visited %llu row %d col %d nVisited %d pop count %d \n", visited, row, col, nVisited, pc);
	
	//printf("visited size %d row %d pc %d\n", sizeof(visited), sizeof(row), pc);
	
	//cout << popCount(visited);
	//cout << nVisited;
	
	assert( (1UL << curIdx & visited) == 0 );
	
	//cout << popCount(visited);
	//cout << nVisited;
	assert( popCount(visited) == nVisited );
	
	++nVisited;
	
	
	
	if (nVisited == N * N && row == N - 1 && col == 0)
		return 1;
		
	
	
	ull c = 0;
	for(int d = 0; d <= 3; ++d)
	{
		int rr = row + dir[d][1];
		int cc = col + dir[d][0];
		
		if (rr < 0 || rr >= N || cc < 0 || cc >= N)
			continue;
			
		int adjIdx = getIndex(rr, cc);
		
		
		if ( (1UL << adjIdx & visited) != 0 )
			continue;
		
		//printf("rr %d cc %d adjIdx = %llu %d\n", rr, cc, adjIdx, visited | 1UL << adjIdx);
		
		
		c += ::count( visited | 1UL << (ull)curIdx, rr, cc, nVisited );
	}
	
	return c;

}

int main() {
	ofstream fout ("betsy.out");
	
	freopen("betsy.in","r",stdin);
    //ifstream fin ("betsy.in");
		
	while(1 == scanf("%d", &N) )
	{
		int ans = ::count( 0, 0, 0, 0 );
	
		fout << ans	<< endl; 
	}
    return 0;
}

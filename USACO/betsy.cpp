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

char binaryStr[65];
	
int popCount( ull num )
{
	int pc = 0;
	int idx = 0;
	while( num > 0 )
	{
		if (num & 1UL)
		{
			pc ++;
			binaryStr[idx] = '1';
		} else {
			binaryStr[idx] = '0';
		}
			
		++idx;
		num >>= 1;
	}	
	
	binaryStr[idx] = '\0';
	
	return pc;
}

int getAdjCount( const ull visited, const int row, const int col )
{
	int r = 0;
	
	for(int d = 0; d <= 3; ++d)
	{
		int rr = row + dir[d][1];
		int cc = col + dir[d][0];
		
		if (rr < 0 || rr >= N || cc < 0 || cc >= N)
			continue;

		int adjIdx = getIndex(rr, cc);
		
		
		if ( (1ULL << adjIdx & visited) != 0 )
			++r;

	}
	
	return r;
}
	
ull count( const ull visited, const int row, const int col, int nVisited )
{
	int curIdx = getIndex(row, col);
	
	//cout << "Visited " << visited << " pop count " << popCount(visited) << " nVisited " << nVisited << endl;
	
	//int pc = popCount(visited);
	//printf("visited %llu row %d col %d nVisited %d pop count %d %s \n", visited, row, col, nVisited, pc, binaryStr);
	
	//printf("visited size %d row %d pc %d\n", sizeof(visited), sizeof(row), pc);
	
	//cout << popCount(visited);
	//cout << nVisited;
	
	assert( (1ULL << curIdx & visited) != 0 );
	
	//assert( popCount(visited) == nVisited );
	
	//++nVisited;
	
	
	//Finished
	if (nVisited == N * N && row == N - 1 && col == 0)
		return 1;
		
	//Hit right wall with empty square above, it means the square was divided and won't be finishable
	if (row > 0 && col == N - 1 && 0 == (visited & 1ULL << curIdx - N) )
		return 0;
		
	//Hit south wall with empty square to right 
	if (col < N - 1 && row == N - 1 && 0 == (visited & 1ULL << curIdx + 1) )
		return 0;
		
	//Hit north wall with empty to left 
	if (col > 0 && row == 0 && 0 == (visited & 1ULL << curIdx - 1) )
		return 0;
	
	//Hit left wall with empty top 
	if (col == 0 && row > 0 && 0 == (visited & 1ULL << curIdx - N) )
		return 0;
	
	bool hasDeadEnd = false;
	int adjCount[4] = {0, 0, 0, 0};
	
	for(int d = 0; d <= 3; ++d)
	{
		int rr = row + dir[d][1];
		int cc = col + dir[d][0];
		
		if (rr < 0 || rr >= N || cc < 0 || cc >= N)
			continue;
			
		int adjIdx = getIndex(rr, cc);
		
		
		if ( (1ULL << adjIdx & visited) != 0 )
			continue;
	
		adjCount[d] = getAdjCount(visited, rr, cc);
		
		assert(adjCount[d] >= 1 && adjCount[d] <= 4);
		
		if (hasDeadEnd && adjCount[d] >= 3)
		{
			//printf("Prune\n");
			return 0;
		}
		
		if (adjCount[d] >= 3)
			hasDeadEnd = true;
	}
	
	ull c = 0;
	for(int d = 0; d <= 3; ++d)
	{
		int rr = row + dir[d][1];
		int cc = col + dir[d][0];
		
		if (rr < 0 || rr >= N || cc < 0 || cc >= N)
			continue;
			
		int adjIdx = getIndex(rr, cc);
		
		if ( (1ULL << adjIdx & visited) != 0 )
			continue;
		
		if (hasDeadEnd && adjCount[d] < 3)
			continue;
		
		//printf("rr %d cc %d adjIdx = %llu %d\n", rr, cc, adjIdx, visited | 1UL << adjIdx);
		
		
		c += ::count( visited | 1ULL << adjIdx, rr, cc, nVisited+1 );
		
		//if ( 0 == c )
		//	break;
	}
	
	//printf("visited %llu row %d col %d nVisited %d pop count %d %s.  Count = %llu \n", visited, row, col, nVisited, pc, binaryStr, c);
	
	return c;

}

int main() {
	ofstream fout ("betsy.out");
	
	freopen("betsy.in","r",stdin);
    //ifstream fin ("betsy.in");
	
	
	//puts(binaryStr);
	//return 0;
	while(1 == scanf("%d", &N) )
	{
		int ans = ::count( 1, 0, 0, 1 );
	
		fout << ans	<< endl; 
	}
    return 0;
}

#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <list>
#include <cassert>

using namespace std;

typedef list<int> li;
typedef li::iterator LIT;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)

const int MAX_N = 25;
int loc[MAX_N];

li piles[MAX_N];

int N;

char cmd1[10];
char cmd2[10];

//Return everything above pilePos to original positions
void returnToOrig( li& l, const LIT& pilePos, int pile )
{
	LIT it = pilePos;
	it++;
	while(it != l.end())
	{		
		int block = *it;
		
		//should be impossible to put things under an existing block on its original position
		assert(block != pile);
		
		//printf("moving %d to original pos\n", block);
		
		li& origPile = piles[  block ];
		
		//important to do this post++ or else the iterator won't be valid
		origPile.splice( origPile.end(), l, it++ );
		
		loc [ block ] = block;
	}

}

void move(int b1, int b2, bool clearB2)
{
	li& l1 = piles[ loc[b1] ];
	li& l2 = piles[ loc[b2] ];
	
	LIT b1It = find( l1.begin(), l1.end(), b1);	
	
	if (clearB2)
	{
		LIT b2It = find( l2.begin(), l2.end(), b2);
		returnToOrig(l2, b2It, b2 );
	}
	
	returnToOrig(l1, b1It, b1 );
		
	l2.splice( l2.end(), l1, b1It );
	
	loc[b1] = loc[b2];
}

void pile(int b1, int b2, bool clearB2)
{
	li& l1 = piles[ loc[b1] ];
	li& l2 = piles[ loc[b2] ];
	
	LIT b1It = find( l1.begin(), l1.end(), b1);	
	
	if (clearB2)
	{
		LIT b2It = find( l2.begin(), l2.end(), b2);	
		returnToOrig(l2, b2It, b2);
	}
	
	for( LIT it = b1It; it != l1.end(); ++it)
	{
		loc[*it] = loc[b2];
	}
	
	l2.splice( l2.end(), l1, b1It, l1.end() );	
}

void checkAll()
{
	FOR(i, 0, N)
	{
		li& l = piles[ loc[i] ];
		assert( find( l.begin(), l.end(), i) != l.end() );
	}
}

void printPiles()
{
	FOR(i, 0, N)
	{
		printf("%d:", i);
		
		for(LIT it = piles[i].begin(); it != piles[i].end(); ++it)
			printf(" %d", *it);
		
		puts("");
	}
}	



int main()
{
	int T;
	int b1; int b2;
	
	while(1 == scanf("%d", &N))
	{
		scanf("%d", &N);
		
		//printf("N=%d\n", N);
		
		FOR(i, 0, N)
		{
			//printf("i=%d\n", i);
			piles[i].clear();
			piles[i].push_back(i);
			loc[i] = i;
			assert(piles[i].size() == 1);
		}
		
		while(1 == scanf("%s", cmd1) && 0 != strcmp(cmd1, "quit") && 3 == scanf("%d %s %d", &b1, cmd2, &b2))
		{
			// printf("Read %s %d - %s %d\n", cmd1, b1, cmd2, b2);
			if (b1 == b2 || loc[b1] == loc[b2])
				continue;
			
			bool clearB2 = 0 == strcmp(cmd2, "onto");
			
			if ( !strcmp(cmd1, "move") )
				::move(b1, b2, clearB2);
				
			if ( !strcmp(cmd1, "pile") ) 
				::pile(b1, b2, clearB2);
			
			checkAll();
		}
		
		assert( 0 == strcmp(cmd1, "quit") );
		
		printPiles();
		
	}

	return 0;
}
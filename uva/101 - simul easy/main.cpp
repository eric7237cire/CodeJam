#include "stdio.h"
#include <algorithm>
#include <cstring>
#include <list>
using namespace std;

const int MAX_N = 25;
int loc[MAX_N];
int N;
list<int> piles[MAX_N];

char cmd1[10], cmd2[10];

//Return everything above pilePos to original positions
void returnToOrig( list<int>& l, list<int>::iterator it, int pile )
{
	++it;
	
	while(it != l.end() )
	{		
		int block = *it; //block will never be = pile ; should be impossible to put things under an existing block on its original position
		
		piles[block].splice( piles[block].end(), l, it++ ); //important to do this post++ or else the iterator won't be valid after the splice
		loc [ block ] = block;
	}
}

void move(int b1, int b2, bool movePile, bool clearB2)
{
	list<int>& l1 = piles[ loc[b1] ];
	list<int>& l2 = piles[ loc[b2] ];
	
	list<int>::iterator b1It = find( l1.begin(), l1.end(), b1);	
	
	if (clearB2)	
		returnToOrig(l2, find( l2.begin(), l2.end(), b2), b2 );
	
	if (!movePile)
		returnToOrig(l1, b1It, b1 );
	
	for( list<int>::iterator it = b1It; it != l1.end(); ++it)
		loc[*it] = loc[b2];
		
	l2.splice( l2.end(), l1, b1It, l1.end() );		
}

int main()
{
	int b1; int b2;
	
	while(1 == scanf("%d", &N))
	{
		for(int i = 0; i < N; ++i)
		{
			piles[i].assign(1, i);
			loc[i] = i;
		}
		
		while(1 == scanf("%s", cmd1) && 0 != strcmp(cmd1, "quit") && 3 == scanf("%d %s %d", &b1, cmd2, &b2))
			if (b1 != b2 && loc[b1] != loc[b2])
				::move(b1, b2, 0 == strcmp(cmd1, "pile"), 0 == strcmp(cmd2, "onto"));
				
		for(int i = 0; i < N; ++i)
		{
			printf("%d:", i);
			
			for(list<int>::iterator it = piles[i].begin(); it != piles[i].end(); ++it)
				printf(" %d", *it);
			
			puts("");
		}
	}

	return 0;
}
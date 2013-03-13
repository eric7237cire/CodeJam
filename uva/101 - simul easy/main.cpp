#include "stdio.h"
#include <algorithm>
#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <list>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

typedef vector<int> vi;
typedef vector<vi> vvi;
typedef list<int> li;

typedef li::iterator LIT;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)

const int MAX_N = 24;
int loc[MAX_N];

li piles[MAX_N];

int N;

char cmd1[10];
char cmd2[10];

void moveOnto(int b1, int b2)
{
	LIT b1It = find( piles[ loc[b1] ].begin(), piles[ loc[b1] ].end(), b1);
	
	LIT b2It = find( piles[ loc[b2] ].begin(), piles[ loc[b2] ].end(), b2);

}

void moveOver(int b1, int b2)
{

}

void pileOnto(int b1, int b2)
{


}

void pileOver(int b1, int b2)
{

}

void printPiles()
{
	FOR(i, 0, N)
	{
		printf("%d: ", i);
		for(LIT it = piles[i].begin();
		it != piles[i].end();
		++it)
		{
			if (it != piles[i].begin())
				printf(" ");
			printf("%d", *it);
		}
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
		
		while(4 == scanf("%s %d %s %d", cmd1, &b1, cmd2, &b2))
		{
			printf("hello");
			printf("Read %s %d - %s %d\n",
				cmd1, b1, cmd2, b2);
			if (b1 == b2 || loc[b1] == loc[b2])
				continue;
				
			if ( 0 == strcmp(cmd1, "move") &&
			0 == strcmp(cmd2, "onto") )
				moveOnto(b1, b2);
			if ( 0 == strcmp(cmd1, "move") &&
			0 == strcmp(cmd2, "over") )
				moveOnto(b1, b2);
			if ( 0 == strcmp(cmd1, "pile") &&
			0 == strcmp(cmd2, "onto") )
				pileOnto(b1, b2);
			if ( 0 == strcmp(cmd1, "pile") &&
			0 == strcmp(cmd2, "over") )
				pileOver(b1, b2);
				
		}
		
		//int r = scanf("%s", cmd1);
		
		puts(cmd1);
		assert( 0 == strcmp(cmd1, "quit") );
		
		printPiles();
		
	}

	return 0;
}
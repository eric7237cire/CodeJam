#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
#include <limits>

//#include <string>

#include <stdlib.h>
//#include <ctype.h>

//#include <iostream>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

typedef unsigned int uint; 

char buf[1024];

const int MAX_N = 40;
const int MAX_V = 80000;

int V[MAX_N];
int N;

bool POS[MAX_N];
bool NEG[MAX_N];
int F;

char memo[MAX_N][1 + MAX_V];
//char used[MAX_N][1 + MAX_V];

int possible(int idx, int cash)
{
	if (idx == N && cash == F)
		return 'T';
		
	if (idx == N)
		return 'F';
		
	char& res = memo[idx][cash];
	
	if (res != 0)
	{
		return res;
	}
		
	bool canBeAdd = false;
	bool canBeSub = false;
	
	if (cash - V[idx] >= 0)
	{
		char ifSub = possible(1+idx, cash - V[idx]);
		if (ifSub == 'T')
			canBeSub = true;
	}
	
	if (cash + V[idx] <= MAX_V)
	{
		char ifAdd = possible(1+idx, cash + V[idx]);
		if (ifAdd == 'T')
			canBeAdd = true;
	}
	
	if (canBeSub)	
		NEG[idx] = true;
	
	if (canBeAdd)
		POS[idx] = true;
		
	if (!canBeSub && !canBeAdd)
	{
		res = 'F';
	} else {
		res = 'T';
	}
	
	//printf("memo[%d][%d or %d] = %c \n", idx, cash, cash - 40000, res);
	
	return res;
	//
	//return memo[idx][left];
}


int main()
{

	while(2 == scanf("%d%d", &N, &F) && (N||F))
	{
		memset(memo, 0, sizeof memo);
		
		fill(POS, POS + N, false);
		fill(NEG, NEG + N, false);
		
		F += 40000;
		
		FOR(i, 0, N)
		{
			int v;
			scanf("%d", &v);
			V[i] = v;
		}
			
		char ans = possible(0, 40000);
		
		//printf("max %d Ans %d\n", maxValue, ans);
		
		
		if (ans != 'T')
		{
			printf("*\n", ans );
			continue;			
		}
			
		//printf("ans %c\n", ans );
			
		int cash = 40000;
		
		//Any transaction that is not needed cancels out, so we ignore those
		FOR(i, 0, N)
		{
			//ans = possible(i, cash);
						
			if (POS[i] && NEG[i])
				putc('?', stdout);
			else if (POS[i])
				putc('+', stdout);
			else if (NEG[i])
				putc('-', stdout);
			else 
				throw 3;
							
			//printf("%c", ans );
		}
		
		puts("");
	}

	return 0;
}
//STARTCOMMON
#include <cmath>
#include <vector>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <cstring>
#include <algorithm>

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


class UnionFind
{
public:
	vi id; vi sz;
	
	int nComp;

	void initSet(int n)
	{
		id.assign(n, 0);
		sz.assign(n, 1);
		for(int i = 0; i < n; ++i) id[i] = i;
		
		nComp = n;
	}

	int findSet(int i)
	{
		//printf("Find set %d\n", i);
		return (i == id[i]) ? i : id[i] = findSet(id[i]);
	}

	bool isSameSet(int p, int q)
	{
		return findSet(p) == findSet(q);
	}
	
	int size(int i)
	{
		return sz[ findSet(i) ];
	}

	//Returns id of unified set
	int unionSet(int p, int q)
	{
        int i = findSet(p);
        int j = findSet(q);

		if (i == j)
			return i;
			
		--nComp;
        if(sz[i] > sz[j])
        {
            id[j] = i;
            sz[i] += sz[j];
			return i;
        }
        else
        {
			id[i] = j;
			sz[j] += sz[i];
			return j;
        }
		
		//printf("Union set %d to %d ; sizes %d and %d\n", p, q, sz[p], sz[q]);
	}
};


template<typename T> bool isBetween(T a, T b, T x)
{
	return  ( cmp(a, x) <= 0 && cmp(x, b) <= 0 )
	|| ( cmp(b, x) <= 0 && cmp(x, a) <= 0 );
	
}


char piece;
int M;
int N;

int knightMoves[8][2] =
{
	{2, 1},
	{1, 2},
	{-2, -1},
	{-1, -2},
	{2, -1},
	{1, -2},
	{-2, 1},
	{-1, 2}
	};
	
int diag[4][2] =
{ 
	{1, 1},
	{-1, -1},
	{1, -1},
	{-1, 1}
};
int main() {

//r, k, Q, K  m x n
	int T;
	scanf("%d", &T);

	while(T--)
	{
		scanf(" %c %d %d", &piece, &M, &N);
		
		UnionFind uf;
		uf.initSet(M * N);
		
		FOR(r, 0, M) FOR(c, 0, N)
		{
			int curSq = r * N + c;
			
			if (uf.size(curSq) > 1)
				continue;
				
			switch(piece)
			{
				case 'K':
				for(int x = -1; x <= 1; ++x)
				{
					for(int y = -1; y <= 1; ++y)
					{
						int cc = c + x;
						int rr = r + y;
						int adjSq = rr * N + cc;
						if (isBetween(0, N-1, cc) && isBetween(0, M-1, rr))
						{
							uf.unionSet(adjSq, curSq);
						}
					}
				}
				break;
				case 'k':
				FOR(i, 0, 8)
				{
					int cc = c + knightMoves[i][0];
					int rr = r + knightMoves[i][1];
					int adjSq = rr * N + cc;
					if (isBetween(0, N-1, cc) && isBetween(0, M-1, rr))
					{
						uf.unionSet(adjSq, curSq);
					}
				}
				break;
				case 'Q':
				FOR(d, 0, 4)
				{
					int cc = c;
					int rr = r;
					
					while(isBetween(0, N-1, cc) && isBetween(0, M-1, rr))
					{
						int adjSq = rr * N + cc;
						uf.unionSet(adjSq, curSq);
						
						rr += diag[d][0];
						cc += diag[d][1];
					}
				}
				
				case 'r':
				case 'Q':
				
				FOR(rr, 0, M)
				{
					int adjSq = rr * N + c;
					uf.unionSet(adjSq, curSq);
				}
				FOR(cc, 0, N)
				{
					int adjSq = r * N + cc;
					uf.unionSet(adjSq, curSq);
				}
				
				break;
		}
		
		if (T > 0)
			printf("\n");
		
	}
	return 0;
}

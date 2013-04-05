
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


typedef vector<int> vi; 

template<typename T> 
int cmp(T a, T b)
{
	
	if (a == b)
	{
		return 0;
	}
	
	if (a > b)
	{
		return 1; //a > b
	}
	
	return -1;
}  


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

/* Problem: Chess UVa 278
   Programmer: Md. Mahmud Ahsan
   Description: AD HOC
   Compiled: Visual C++ 7.0
   Date: 26-10-05
*/
#include <iostream>
#include <algorithm>
using namespace std;

char board[20][20];
int white, black;

int main(){
	//freopen("input.txt", "r", stdin);
	int test, m, n, i, j, result;
	char c;

	cin >> test;
	while (test--){
		cin >> c >> m >> n;

		if (c == 'k'){//knight
			bool flag = false;
			white = black = 0;

			//generating the board to obtain the number of black and white room
			for (i = 0; i < m; ++i){
				if (!flag) flag = true;
				else flag = false;

				for (j = 0; j < n; j+=2){
					if (flag) ++white;// board[i][j] = 'w';
					else ++black; //board[i][j] = 'b';
				}

				for (j = 1; j < n; j+=2){
					if (!flag) ++white; //board[i][j] = 'w';
					else ++black; //board[i][j] = 'b';
				}
			}
			result = max(black, white);
			cout << result << endl;
		}
		else if (c == 'r'){//rook
			result = min(m,n);
			cout << result << endl;
		}
		else if (c == 'Q'){//queen
			result = min(m,n);
			cout << result << endl;
		}
		else if (c == 'K'){//king
			result = ( ((m+1) / 2) * ((n+1)/2));
			cout << result << endl;
		}
	}

	return 0;
}

int main2() {

//r, k, Q, K  m x n
	int T;
	scanf("%d", &T);

	while(T--)
	{
		scanf(" %c %d %d", &piece, &M, &N);
		
		UnionFind uf;
		uf.initSet(M * N);
		
		int maxDisconnected = 0;
		
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
			
			maxDisconnected = max(maxDisconnected,
			 M*N - uf.size(curSq) );
		}
		
		printf("%d\n", maxDisconnected);
		
	}
	return 0;
}

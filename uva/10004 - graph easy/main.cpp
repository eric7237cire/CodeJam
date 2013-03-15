#include "stdio.h"
#include <algorithm>
//#include <vector>
#include <cstring>
//#include "string.h"
//#include <limits>
//#include <string>
#include <list>
#include <queue>
#include <cassert>
//#include <iostream>
#include <stdlib.h>

using namespace std;

#define FOR(k,a,b) for(int k=(a); k <  (b); ++k)
#define pb push_back

int N;
int E;
const int MAX_N = 200;

typedef vector<int> vi;

bool conn[MAX_N][MAX_N];
vi adjList[MAX_N];

int color[MAX_N];
 
int main()
{
		
	while(2==scanf("%d%d", &N, &E))
	{
		FOR(i, 0, E)
		{
			int a, b;
			scanf("%d%d", &a, &b);
			conn[a][b]  = conn[b][a] = true;
			adjList[a].push_back(b);
			adjList[b].push_back(a);
		}
			
		memset(conn, 0, sizeof conn);
		memset(color, 0, sizeof color);
		
		queue<int> q;
		
		FOR(i, 0, N)
		
		
	}

	return 0;
}
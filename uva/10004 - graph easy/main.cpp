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

//bool conn[MAX_N][MAX_N];
vi adjList[MAX_N];

int color[MAX_N];
bool visited[MAX_N];
 
int main()
{
		
	while(2==scanf("%d%d", &N, &E) && N)
	{
	
		for(int i = 0; i < N; ++i)
		{
			adjList[i].clear();
		}
		
		FOR(i, 0, E)
		{
			int a, b;
			scanf("%d%d", &a, &b);
	//		conn[a][b]  = conn[b][a] = true;
			adjList[a].push_back(b);
			adjList[b].push_back(a);
			//printf("Connecting nodes %d and %d\n", a, b);
		}
			
		
		memset(color, 0, sizeof color);
		fill(visited, visited + N, false);
		
		bool bipartite = true;
		
		for(int i = 0; i < N  && bipartite; ++i)
		{
			if (visited[i] )
				continue;
			
			queue<int> q;
			q.push(i);
			
			color[i] = 1;
			
			while(!q.empty() && bipartite)
			{
				int curNode = q.front();
				q.pop();
				
				if (visited[curNode])
					continue;
					
				visited[curNode] = true;
				
				int adjColor = 3 - color[curNode];
				
				//printf("In while loop.  cur node %d color %d adj Color %d\n", curNode, color[curNode], adjColor);
				
				for(vi::iterator it = adjList[curNode].begin(); it != adjList[curNode].end(); ++it)
				{
					//printf("Looking at neighbor %d color %d \n", *it, color[*it]);
					
					if ( color[*it] == adjColor )
					{
						q.push( *it );
						continue;
					}
					else if ( color[*it] == 0 )
					{
						color[*it] = adjColor;
						q.push( *it );
						continue;
					} else 					
					if ( color[*it] != adjColor ) 
					{
						//printf("Not bipartite \n", *it, color[*it]);
						bipartite = false;
						break;
					}
				}
			
			}
		}
		
		if (!bipartite)
			printf("NOT ");
		
		puts("BICOLORABLE.");

		
		
	}

	return 0;
}
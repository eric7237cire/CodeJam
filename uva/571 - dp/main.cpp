#include <cmath>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <queue>
#include <utility>
using namespace std;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)

typedef pair<int, int> ii;

bool visited[1001][1001];
ii prev[1001][1001];

//fill, empty, pour 



int main() 
{

	int Ca, Cb, N;
	while(3 == scanf("%d%d%d", &Ca, &Cb, &N))
	{
		FORE(i, 0, N) FORE(j, 0, N)
		{
			visited[i][j] = false;
			prev[i][j] = make_pair(-1, -1);
		}
		
				
		queue<ii> toVisit;
		
		toVisit.push( make_pair(0, 0) );
		
		while(!toVisit.empty())
		{
			ii cur = toVisit.front();
			toVisit.pop();
			
			if (visited[cur.first][cur.second])
				continue;
				
			visited[cur.first][cur.second] = true;
			
			ii choices[6];
			//fill A
			choices[0] = make_pair(Ca, cur.second);
			//fill B
			choices[1] = make_pair(cur.first, Cb);
			
			//empty A
			choices[2] = make_pair(0, cur.second);
			
			//empty B
			choices[3] = make_pair(cur.first, 0);

			//A->B			
			int ab = min(Cb - cur.second, cur.first);
			choices[4] = make_pair(cur.first-ab, cur.second+ab);
			//B->A
			int ba = min(Ca - cur.first, cur.second);
			choices[5] = make_pair(cur.first+ba, cur.second-ba);
			
			FOR(i, 0, 6)
			{
				if (!visited[ choices[i].first ][ choices[i].second])
				{
					prev[ choices[i].first ][ choices[i].second] = cur;
					toVisit.push( choices[i] );
				}
			}
		
		}
		
	}
	return 0;
}

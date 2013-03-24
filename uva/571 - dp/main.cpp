#include <cmath>
#include "stdio.h" 
#include <limits>
#include <cassert>
#include <string>
#include <iostream>
#include <queue>
#include <utility>
#include <algorithm>
using namespace std;

#define FORE(k,a,b) for(int k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (b); ++k)

typedef pair<int, int> ii;

bool visited[1001][1001];
ii prev[1001][1001];

//fill, empty, pour 

ii choices[6];

int main() 
{

	int Ca, Cb, N;
	while(3 == scanf("%d%d%d", &Ca, &Cb, &N))
	{
		FORE(i, 0, Ca) FORE(j, 0, Cb)
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
			
			if (cur.first == 0 && cur.second == N)
			{
				//cout << "Found " << endl;
				break;
			}
			
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
				//printf("Cur %d/%d %d/%d -->  trying %d %d\n", cur.first, Ca, cur.second, Cb, choices[i].first, choices[i].second );
				if (!visited[ choices[i].first ][ choices[i].second])
				{
					prev[ choices[i].first ][ choices[i].second] = cur;
					toVisit.push( choices[i] );
				}
			}
		
		}
		
		//back track
		ii cur(0, N);
		vector<string> ans;
		while( cur.first | cur.second )
		{
			//printf( "A=%d B=%d N=%d\n", cur.first, cur.second, N);
			
			ii result = cur; 
			cur = prev[ cur.first ] [ cur.second ];
			//printf( "prev A=%d B=%d N=%d\n", cur.first, cur.second, N);
			
			if (cur.first == result.first )
			{
				if (result.second == Cb)
					ans.push_back("fill B");
				else
					ans.push_back("empty B");
			} else if (cur.second == result.second) 
			{
				if (result.first == Ca)
					ans.push_back("fill A");
				else 
					ans.push_back("empty A");
					
			} else if (cur.first != result.first && cur.second != result.second) {
				if (result.first > cur.first)
					ans.push_back("pour B A");
				else
					ans.push_back("pour A B");
			} else {
				ans.push_back("Ugh?");
			}
		}
		reverse( ans.begin(), ans.end() );
		for(int i = 0; i < ans.size(); ++i)
		{
			cout << ans[i] << endl;
		}
		puts("success");
			
		
	}
	return 0;
}

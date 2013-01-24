/*
ID: eric7231
PROG: maze1
LANG: C++
*/
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <set>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <bitset>
#include <cctype>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(uint k=(a); k < (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

uint getIndex(int row, int col, uint width)
{
    //Taking into account 2 extra rows and columns
    return (row) * (width ) + col;   
}
int main() {
    
	ofstream fout ("maze1.out");
    ifstream fin ("maze1.in");
	
    vector<string> grid(10);
    
    int W, H;
    fin >> W;
    fin >> H;
    
    //Add nodes for just outside of maze
    uint numNodes = (H+2)*(W+2);
    uvvi connected(numNodes);
    
    uvi exits;
    
    string lineStr;
    getline(fin, lineStr); //eat newline after height
    FOR(line, 0, 2 * H + 1) 
    {
        
        getline(fin, lineStr);
        //Parse line by line, it is either a north/south wall
        if (line % 2 == 0) {
            
            int rowAbove = line / 2 - 1;
            int rowBelow = rowAbove + 1;
            FOR(col, 0, W) {
                char wallCh = lineStr[1+2*col];
                if (wallCh == '-')
                    continue;
                
                assert(wallCh == ' ');
                uint sq1 = getIndex(rowAbove, col, W);
                uint sq2 = getIndex(rowBelow, col, W);
                                
                if (rowAbove == -1) {
                    exits.pb( sq2 );
                    continue;
                }
                
                if (rowBelow == H) {
                    exits.pb( sq1 );
                    continue;
                }
                 
                connected[ sq1 ].pb(sq2);
                connected[ sq2 ].pb(sq1);
            }
        } else {
            //An east/west wall line
            int row = line / 2;
            FORE(col, 0, W) {
                char wallCh = lineStr[2 * col];
                if (wallCh == '|')
                    continue;
                                
                uint sq1 = getIndex(row, col-1, W);
                uint sq2 = getIndex(row, col, W); 
                assert(wallCh == ' ');
                
                if (col == 0) {
                    exits.pb( sq2 );
                    continue;
                }
                if (col == W) {
                    exits.pb( sq1 );
                    continue;
                }
                
                connected[ sq1 ].pb(sq2);
                connected[ sq2 ].pb(sq1);                
            }
        }
    }
      
    
    vector<bool> visited(W*H, false);
    vector<uint> distTo(W*H, 100000000);
    
    queue<uint> toVisit;
    
    assert(exits.size() == 2);
  
    FORE(e, 0, 1) 
    {
        visited[exits[e]] = true;
        distTo[exits[e]] = 1;
        toVisit.push(exits[e]);
    }
    
    while(!toVisit.empty())
    {
        uint v = toVisit.front();
        toVisit.pop();
        
        FOR(adjIdx, 0, connected[v].size())
        {
            uint adj = connected[v][adjIdx];
            if (visited[adj])
                continue;
            
            distTo[adj] = distTo[v] + 1;
            visited[adj] = true;
            toVisit.push(adj);
        }
    }
    
    uint maxDist = *max_element(all(distTo));
    fout << maxDist << endl;
	return 0;
}

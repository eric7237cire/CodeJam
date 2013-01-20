/*
ID: eric7231
PROG: butter
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
#include <cmath>
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
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

class edge
{
public:
    uint weight;
    uint to;
    
    edge(uint tto, uint wweight) : to(tto), weight(wweight) 
    {}
};

int main() {
    
	ofstream fout ("butter.out");
    ifstream fin ("butter.in");
	
    uint N, P, C;
    
    fin >> N >> P >> C;
    
	const uint notConnected = 1000000000;
	
	typedef vector< vector< edge > > AdjList;
	
	AdjList adjList(P);
	
	uvi cowLocs(N, 0);
	
	FOR(cowIdx, 0, N)
	{
	    fin >> cowLocs[cowIdx];    
	}
	
	uint p1, p2, d;
	
	FOR(conPath, 0, C)
	{
	    fin >> p1 >> p2 >> d;
		//Convert to 0 based index
	    adjList[p1-1].pb( edge( p2-1, d ) );
	    adjList[p2-1].pb( edge( p1-1, d ) );
	}
	
	
	
	uint minTotal = notConnected;
	
    FOR(butterPasture, 0, P)
    {
        uvi distToPast(P, notConnected);
        
        distToPast[ butterPasture ] = 0;
        
        set < uu > toVisit;
        toVisit.insert( mp(0, butterPasture) );
        
        while(!toVisit.empty())
        {
            set<uu>::iterator top = toVisit.begin();
            
            uint distCurToCow = top->first;
            uint curPasture = top->second;
            
            toVisit.erase(top);
            
            FOR(adjIdx, 0, adjList[curPasture].size())
            {
                const edge& curEdge = adjList[curPasture][adjIdx];
                
                uint distAdjToCow = distCurToCow + curEdge.weight;
                
                if (distAdjToCow < distToPast[curEdge.to])
                {
                    //replace node in queue
                    toVisit.erase( mp( distToPast[curEdge.to], curEdge.to ) );
                    distToPast[ curEdge.to ] = distAdjToCow;
                    toVisit.insert( mp( distToPast[curEdge.to], curEdge.to ) );
                }
            }
        }
        
        uint total = 0;
        
        FOR(cIdx, 0, N)
        {
            total += distToPast[ cowLocs[cIdx] - 1 ];
        }
        
        minTotal = min(minTotal, total);
        
        
    }
    	
	fout << minTotal << endl;
	
	
    return 0;
}

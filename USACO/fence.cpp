/*
ID: eric7231
PROG: fence
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

typedef vector< vector< uint > > AdjList;



void  find_circuit(uint start, AdjList& edges, uvi& circuit)
{
    if (edges[start].size() == 0)
      circuit.pb(start);
    else
    {
      while(edges[start].size() > 0)
      {
          uvi::iterator edgeIt = edges[start].begin();
          uint adjNode = *edgeIt;
          
          uvi::iterator adjEdgeIt = find( all( edges[adjNode] ), start);
          
          assert(adjEdgeIt != edges[adjNode].end() );
          edges[start].erase(edgeIt);
          edges[adjNode].erase(adjEdgeIt);
          
          find_circuit (adjNode, edges, circuit);
      }
      circuit.pb( start );
    }
}

int main() {
    
	ofstream fout ("fence.out");
    ifstream fin ("fence.in");
	
    uint F;
    
    fin >> F;
    
	const uint notConnected = 1000000000;
	
	
	
	AdjList adjList(500);
	
	uint i1, i2;
	FOR(f, 0, F)
	{
	    fin >> i1 >> i2;
	    
	    adjList[i1-1].pb( i2-1 );
	    adjList[i2-1].pb( i1-1 );
	    //adjList[i1-1].insert( i2-1 );
	    //adjList[i2-1].insert( i1-1 );
	}
	
	int firstOddDeg = -1;
	
	FOR(n, 0, 500)
	{
	    sort( all(adjList[n]) );
	    if (adjList[n].size() % 2 == 1 && firstOddDeg == -1)
	        firstOddDeg = n;	    
	}
	
	uvi eulerPath;
	
	find_circuit( firstOddDeg == -1 ? 0 : firstOddDeg, adjList, eulerPath);
	
	reverse( all(eulerPath) );
	
	
	FOR(p, 0, eulerPath.size())
	{
	    uint a = eulerPath[p]+1;
	    fout << a  << endl;
	}
	
	
    return 0;
}

/*
ID: eric7231
PROG: agrinet
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
#include <iomanip>
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


class Edge
{
    public:
    uint u;
    uint v;
    uint weight;
    
    Edge(uint _u, uint _v, uint _weight) : u(_u), v(_v), weight(_weight) {}
    
};

class EdgeComparator
{
public:
    bool operator()(const Edge& e1, const Edge& e2)
    {
        if (e1.weight != e2.weight)
            return e1.weight < e2.weight;
            
        if (e1.u != e2.u)
            return e1.u < e2.u;
            
        return e1.v < e2.v;
    }
};
        

void addVertexToMST( uint vertex, vector<bool>& inMST,
    set<Edge, EdgeComparator>& edges,
    const uvvi& dist
    )
{ 
    assert(!inMST[vertex]);
    
    inMST[vertex] = true;
    FOR(adj, 0, inMST.size())
        {
            if (inMST[adj])
                continue;
                
            if (adj == vertex)
                continue;
                
            edges.insert(
                 Edge( vertex, adj, dist[vertex][adj] ));
        }
        
        
}

int main() {
    
	ofstream fout ("agrinet.out");
    ifstream fin ("agrinet.in");
	
    uint N;
    
    fin >> N;
    
    uvvi dist( N, uvi(N, 0) );
    
    FOR(r, 0, N) FOR(c, 0, N)
    {
        fin >> dist[r][c];
    }
    
    vector<bool> inMST(N, false);
    
    set<Edge, EdgeComparator> edges;
    uint total = 0;
    
    FOR(i, 0, N) 
    {
        if ( inMST[i] )
            continue;
            
        //Add vertex i to MST
        addVertexToMST( i, inMST, edges, dist );
        
        while( !edges.empty() )
        {
            Edge edge = *edges.begin();
            
            edges.erase(edges.begin());
               
            assert( inMST[edge.u] || inMST[edge.v] );
            if (inMST[edge.u] && inMST[edge.v])
                continue;
            
            //printf("Adding %d %d with weight %d\n", edge.u, edge.v, dist[edge.u][edge.v]);
            total += dist[edge.u][edge.v];
            
            if (!inMST[edge.u])
                addVertexToMST( edge.u, inMST, edges, dist );
                
            if (!inMST[edge.v])
                addVertexToMST( edge.v, inMST, edges, dist );
        }
    }
    
    
    
	fout << total << endl;
	
    return 0;
}

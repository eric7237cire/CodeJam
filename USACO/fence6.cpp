/*
ID: eric7231
PROG: fence6
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

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const K& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}

class edge
{
public:
    int weight;
    int to;
    
    edge(int tto, int wweight) : to(tto), weight(wweight) 
    {}
};

const uint notConnected = 1000000000;

int op_decrease (int i) { return ++i; }

int main() {
    
	ofstream fout ("fence6.out");
    ifstream fin ("fence6.in");
	/*
	The first line of each record contains four integers: s, the segment number (1 <= s <= N); Ls, the length of the segment (1 <= Ls <= 255); N1s (1 <= N1s <= 8) the number of items on the subsequent line; and N2sthe number of items on the line after that (1 <= N2s <= 8).
The second line of the record contains N1 integers, each representing a connected line segment on one end of the fence.
The third line of the record contains N2 integers, each representing a connected line segment on the other end of the fence.
*/
    int N;
    fin >> N;
    
	typedef vector< vector< edge > > AdjList;
	
	
	map<vector<int>, int > edgeListToNode;
	
	int s, ls, nSeg1, nSeg2;
	vi seg1;
	vi seg2;
	
	int nodeCount = 0;
	
	
	AdjList adjList(N);
	
	FOR(seg, 0, N)
	{
        fin >> s >> ls >> nSeg1 >> nSeg2;
        
        seg1.resize(nSeg1+1);
        seg2.resize(nSeg2+1);
        
        FOR(s1, 0, nSeg1)
        {
            fin >> seg1[s1];
        }
        FOR(s2, 0, nSeg2)
        {
            fin >> seg2[s2];
        }
        
        //Use zero based
        transform( all(seg1), seg1.begin(), op_decrease );
        
        transform( all(seg2), seg2.begin(), op_decrease );
        
        seg1.pb(seg);
        seg2.pb(seg);
        
        sort( all(seg1) );
        sort( all(seg2) );
        
        //Now seg1 and seg2 will uniquely define a node
        
        int nodeNum1 = getMapValue(edgeListToNode, seg1, -1);
        if (nodeNum1 == -1) {
            nodeNum1 = nodeCount++;
            edgeListToNode.insert( mp(seg1, nodeNum1 ) );
        }
        
        int nodeNum2 = getMapValue(edgeListToNode, seg2, -1);
        if (nodeNum2 == -1) {
            nodeNum2 = nodeCount++;
            edgeListToNode.insert( mp(seg2, nodeNum2 ) );
        }
        
        adjList[nodeNum1].pb( edge(nodeNum2, ls) );
        adjList[nodeNum2].pb( edge(nodeNum1, ls) );
	}
	
	assert(nodeCount == N);
	
	FOR(rootNode, 0, N)
    {
        uvi dist(N, notConnected);
        
        dist[ rootNode ] = 0;
        
        set < ii > toVisit;
        toVisit.insert( mp(0, 0) );
        
        while(!toVisit.empty())
        {
            set<ii>::iterator top = toVisit.begin();
            
            int distToRootFromCurNode = top->first;
            int curNode = top->second;
            
            toVisit.erase(top);
            
            FOR(adjIdx, 0, adjList[curNode].size())
            {
                const edge& curEdge = adjList[curNode][adjIdx];
                
                uint distAdjToRoot = distToRootFromCurNode + curEdge.weight;
                
                if (distAdjToRoot < dist[curEdge.to])
                {
                    //replace node in queue
                    toVisit.erase( mp( dist[curEdge.to], curEdge.to ) );
                    dist[ curEdge.to ] = distAdjToRoot;
                    toVisit.insert( mp( dist[curEdge.to], curEdge.to ) );
                }
            }
        }
        
        uint total = 0;
        
        
        
    }
	
    return 0;
}

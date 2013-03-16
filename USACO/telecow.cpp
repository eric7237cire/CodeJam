/*
ID: eric7231
PROG: telecow
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
#include <iomanip>
#include <sstream>
#include <bitset>
#include <limits>
#include <cctype>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef long long ll;
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
#define SZ(x) ((int) (x).size())

const bool debug = true;

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const V& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}


const int notConnected = numeric_limits<int>::max();


template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os <<  vec[i] << endl;
    }
    return os;
}


//edge from source to destination
struct edge
{
    
    int src;
    int dest;
    
    ll cap;
    ll residue;
	
	//flow = capacity - residue
    
    edge(int _src, int _dest, ll pcap, ll pres) : src(_src), dest(_dest), cap(pcap), residue(pres)
	{
	
	}
};

ostream& operator<<(ostream& os, const edge& e)
{
    os <<  e.src << " --> " << e.dest
		<< " flow " << e.cap - e.residue << " / " << e.cap ;
    
    return os;
}

typedef vector<vector<edge> > EdgeList;

class Flow
{
	public:
	
	//V [ node idx ] = list of edge idxs originating from node
	vvi V;
	vector<edge> E;
	
	int source;
	int sink;
	
	Flow(int _source, int _sink) : source(_source), sink(_sink)
	{
	
	}
	
	//set flow back to 0
	void resetFlow()
	{
		for(int i = 0; i < E.size(); ++i)
		{
			if (i % 2 == 0)
				E[i].residue = E[i].cap;
			else
				E[i].residue = 0;
		}
	
	}
	
	//Makes all edges connected to nodeIdx capacity = 0
	void removeNode(int nodeIdx)
	{
		for(int e = 0; e < V[nodeIdx].size(); ++e)
		{
			int eIdx = V[nodeIdx][e];
			E[ eIdx ].residue = E[eIdx].cap = E[ eIdx ^ 1].residue = E[ eIdx ^ 1].cap = 0;
		}
	}
	
	void add_edge(int src, int dest, int cap)
	{
		int e = E.size();
		
		if ( max(src,dest) >= V.size())
			V.resize( max(src,dest) + 1);
		
		V[src].pb(e);
		V[dest].pb(e+1);
		
		E.push_back(edge(src, dest, cap, cap));
		E.push_back(edge(src, dest, cap, 0));
	}
	
	/*
		prev[ vertex id ] =  the edge id of the edge used to go to previous node
	*/
	ll findAugPathMaxFlow(const vi& prev)
	{
		ll canPush = numeric_limits<ll>::max();
		
		int nodeIdx = sink;
		
		if (debug)
			printf("Finding maximum flow through augmenting path. Sink=%d\n", sink);
			
		while( prev[nodeIdx] != -2 ) //nodeIdx is not the source
		{
			assert(prev[nodeIdx] >= 0);
			
			canPush = min(canPush, E[ prev[nodeIdx] ].residue );
			
			nodeIdx = E[ prev[nodeIdx] ].src;		
			
			if (debug)
				printf("Can push %lld.  Next node in aug path %d\n", canPush, nodeIdx);
		}
	
		return canPush;
	}
	
	void updateViaAugPath(const vi& prev, ll flowAdded)
	{
		int nodeIdx  = sink;
		
		while( prev[nodeIdx] != -2 ) //nodeIdx is not the source
		{
			assert(prev[nodeIdx] >= 0);
			
			E[ prev[nodeIdx] ].residue -= flowAdded;

			//Because we added the edges in pairs xor will either add one or subtract one
            E[ prev[nodeIdx] ^ 1].residue += flowAdded;
            
			if (debug)
				printf("Pushing %lld flow at node %d edge ids %d and %d \n", 
				flowAdded, nodeIdx, prev[nodeIdx], prev[nodeIdx] ^ 1);
			
			nodeIdx = E[ prev[nodeIdx] ].src;
		}
		
	}
	
	ll augment()
	{
		const int nNodes = V.size();
		vi prev(nNodes, -1);
		vector<bool> seen(nNodes, false);

		prev[source] = -2;
		
		queue<int> q;
		
		q.push(source);
		seen[source] = true;
		while (!q.empty())
		{
			int nodeIdx = q.front();
			q.pop();
			
			if (debug) printf("Popped node %d\n", nodeIdx);
			//Sink?
			if (nodeIdx == sink)
			{
				if (debug) printf("reached sink\n");
				
				ll canPush = findAugPathMaxFlow(prev);
				assert(canPush > 0);
				
				updateViaAugPath( prev, canPush );
				
				return canPush;
			}

		   // printf("Looking at node %d.  Edges count %d\n", c, edges[c].size());
			for (int i = 0; i < V[nodeIdx].size(); i++)
			{
				const int edgeIdx = V[nodeIdx][i];
				const edge& anEdge = E[ edgeIdx ];
				
				if (anEdge.residue == 0)
					continue;
					
				int trgNodeIdx = anEdge.dest;
				//printf("edges target %d flow %d capacity %d seen %d\n", n, edges[c][i].flow, edges[c][i].cap, seen[n]);
				if ( !seen[trgNodeIdx])
				{
					prev[trgNodeIdx] = edgeIdx;
					seen[trgNodeIdx] = true;
					q.push(trgNodeIdx);
				}
			}
			printf("Done\n");
		}
		
		printf("Return 0\n");
		return 0;
	}
};


#if 0
void printEdges(const EdgeList& edges, int N)
{
    FOR(from, 0, edges.size()) 
    {
        if (from < N)
			cout << "From outBoundNode Comp #" << from + 1 << endl;
		else 
			cout << "From inBoundNode Comp #" << from - N + 1 << endl;
        FOR( eIdx, 0, edges[from].size() )
        {
			if (edges[from][eIdx].cap == 0)
				continue;
				
			int toNode = edges[from][eIdx].trg;
			if (toNode < N)
				cout << " To outBoundNode comp# " << toNode+1;
			else
				cout << " To inBoundNode comp# " << toNode-N+1;
            
			cout << " Edge obj " << edges[from][eIdx] << endl;
        }
     
    }
}
#endif 


int main() {
    
    
	ofstream fout ("telecow.out");
    ifstream fin ("telecow.in");
	
    int N,M, c1, c2;
    
    //N = warehouses  ; M = edges
    fin >> N >> M >> c1 >> c2;
    
    int from, to;
        
    int source = c1 - 1;
    int sink = c2 - 1 + N;
       
	Flow flow(source, sink);
	
	set<int> ignore;
	ignore.insert(3);
	ignore.insert(4);
	//ignore.insert(8);
	//ignore.insert(11);
	//ignore.insert(17);
	ignore.insert(33);
	   
	ignore.clear();
	
    FOR(m, 0, M)
    {
        fin >> from >> to;
		
		if ( contains(ignore, from) || contains(ignore, to) || from > 20 || to > 20)
			continue;
		//if (from == 3 || from == 4 ||
		//to == 3 || to == 4)
			//continue;
        //in bound nodes +N
		flow.add_edge(from-1, to-1 + N, 1000000 );
		flow.add_edge(to-1, from-1 + N, 1000000 );
    }
	
	//Add biderctional nodes between inbound and outbound nodes 
	FOR(n, 0, N)
	{
		flow.add_edge(n, n+ N, 1 );
		flow.add_edge(n+N, n, 1 );
	}
    
     
    //printEdges(edges, N);
     
    ll total = 0;
    
    
    ll augAmt = 0;
    while( (augAmt = flow.augment()) > 0 )
    {   
		//cout << "Augment " << total << endl;
        total += augAmt;
    }
    
	
	    
	
   // printEdges(edges);
	//cout << "Total real " << total << endl;
	fout << total << endl;
	
	vi minCut;
	
	for(int n = 0; n < N; ++n)
	{
		if (n == c1 - 1 || n == c2 - 1)
			continue;
	
		flow.resetFlow();
		
		flow.removeNode(n);
		
		ll newTotal = 0;
        
		augAmt = 0;
		printf("Call augment flow source %d sink %d\n", source, sink);
		while( (augAmt = flow.augment()) > 0 )
		{   
			//cout << "Augment " << total << endl;
			newTotal += augAmt;
		}
		
		if (newTotal < total)
		{
			printf("New max flow %d\n", newTotal);
			total = newTotal;
			minCut.pb(n+1);
		}
	}
	
	for(int i = 0; i < minCut.size(); ++i)
	{
		if (i > 0)
			fout << ' ';
		fout << minCut[i];
	}
	fout << endl;
	
    return 0;
}

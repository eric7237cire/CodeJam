
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
template <typename FlowType>
struct edge
{    
    int src;
    int dest;
    
    FlowType cap;
    FlowType residue;
	
	bool ignore;
	
	//flow = capacity - residue
    
    edge(int _src, int _dest, FlowType _cap, FlowType _res) :
	src(_src), dest(_dest), cap(_cap), residue(_res),
	ignore(false)
	{
		assert(residue >= 0 && residue <= cap);
	}
};

template<typename FlowType>
ostream& operator<<(ostream& os, const edge<FlowType>& e)
{
    os <<  e.src << " --> " << e.dest
		<< " flow " << e.cap - e.residue << " / " << e.cap ;
    
    return os;
}

template<typename FlowType>
class Flow
{
	public:
	
	//V [ node idx ] = list of edge idxs originating from node
	vvi V;
	vector<edge<FlowType> > E;
	
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
	
	
	void setIgnoreNode(int nodeIdx, bool ignore)
	{
		for(int e = 0; e < V[nodeIdx].size(); ++e)
		{
			int eIdx = V[nodeIdx][e];
			E[ eIdx ].ignore = ignore;
		}
	}
	
	void add_edge(int src, int dest, FlowType cap)
	{
		int e = E.size();
		
		if ( max(src,dest) >= V.size())
			V.resize( max(src,dest) + 1);
		
		V[src].pb(e);
		V[dest].pb(e+1);
		
		E.push_back(edge<FlowType>(src, dest, cap, cap));
		E.push_back(edge<FlowType>(src, dest, cap, 0));
	}
	
	/*
		prev[ vertex id ] =  the edge id of the edge used to go to previous node
	*/
	FlowType findAugPathMaxFlow(const vi& prev)
	{
		FlowType canPush = numeric_limits<FlowType>::max();
		
		int nodeIdx = sink;
		
		if (debug)
			printf("Finding maximum flow through augmenting path. Sink=%d\n", sink);
			
		while( prev[nodeIdx] != -2 ) //nodeIdx is not the source
		{
			assert(prev[nodeIdx] >= 0);
			
			canPush = min(canPush, E[ prev[nodeIdx] ].residue );
			
			nodeIdx = E[ prev[nodeIdx] ].src;		
			
			if (debug)
				printf("Can push %d.  Next node in aug path %d\n", canPush, nodeIdx);
		}
	
		return canPush;
	}
	
	void updateViaAugPath(const vi& prev, FlowType flowAdded)
	{
		int nodeIdx  = sink;
		
		while( prev[nodeIdx] != -2 ) //nodeIdx is not the source
		{
			assert(prev[nodeIdx] >= 0);
			
			E[ prev[nodeIdx] ].residue -= flowAdded;
			assert(E[ prev[nodeIdx] ].residue >= 0);

			//Because we added the edges in pairs xor will either add one or subtract one
            E[ prev[nodeIdx] ^ 1].residue += flowAdded;
			assert( E[ prev[nodeIdx] ^ 1 ].residue <= E[ prev[nodeIdx] ^ 1 ].cap);
            
			if (debug)
				printf("Pushing %d flow at node %d edge ids %d and %d \n", 
				flowAdded, nodeIdx, prev[nodeIdx], prev[nodeIdx] ^ 1);
			
			nodeIdx = E[ prev[nodeIdx] ].src;
		}
		
	}
	
	FlowType augment()
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
			
			assert(seen[nodeIdx]);
							
			//if (debug) printf("Popped node %d\n", nodeIdx);
			//Sink?
			

		   // printf("Looking at node %d.  Edges count %d\n", c, edges[c].size());
			for (int i = 0; i < V[nodeIdx].size(); i++)
			{
				const int edgeIdx = V[nodeIdx][i];
				const edge<FlowType>& anEdge = E[ edgeIdx ];
				
				if (anEdge.residue == 0)
					continue;
				
				if (anEdge.ignore)
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
			//printf("Done\n");
		}
		
		if (seen[sink])
		{
			if (debug) printf("reached sink\n");
			
			FlowType canPush = findAugPathMaxFlow(prev);
			assert(canPush > 0);
			
			updateViaAugPath( prev, canPush );
			
			return canPush;
		}
		
		//printf("Return 0\n");
		return 0;
	}
};


string plugName[200];
string deviceName[100];

string getNameForId(int id)
{
    if (id == 0)
        return "SOURCE";
    if (id == 1)
        return "SINK";
    if (id < 102)
        return deviceName[id-2];
    return plugName[id-102];
    
}

template< typename FlowType >
void print(const Flow<FlowType>& flow)
{
    FOR(i, 0, flow.E.size()) 
    {
        if (i % 2 == 1)
            continue;
        cout << "Id: " << i << " Source: " << getNameForId(flow.E[i].src);
        cout <<  " Dest: " << getNameForId(flow.E[i].dest);
        cout << " " << flow.E[i] << endl;     
    }
}


typedef map<string, int> msi;

int getId(map<string, int>& m, const string& name)
{
	msi::iterator lowerBound = m.lower_bound(name);

	if(lowerBound != m.end() && !(m.key_comp()(name, lowerBound->first)))
	{
	   return lowerBound->second;
	}
	else
	{
	   m.insert(lowerBound, std::make_pair(name, m.size()));
	   return m.size() - 1;
	}	
}

int main() {
    
    
	int T;
	
	cin >> T;
    
	while(T--)
	{
		
		
		const int source = 0;
		const int sink = 1;
		Flow<int> flow(source, sink);
		
		map<string, int> plugIdMap;
		map<string, int> deviceIdMap;
		int nPlug, nDevice, nAdap;
		
		cin >> nPlug ; 
		string name;
		FOR(i, 0, nPlug)
		{
			cin >> name;
			int id = getId(plugIdMap, name);
			//assert(id == i);
			
			plugName[id] = name; 
		    
			flow.add_edge( id + 102, sink, 1 );
		}
		
		cin >> nDevice;
		
		FOR(i, 0, nDevice)
		{
			cin >> name;
			int id = getId(deviceIdMap, name);
			//assert(id == i);
			
			deviceName[id] = name;
			
			cin >> name;
			int plugId = getId(plugIdMap, name) ;
			
			plugName[plugId] = name; 
			//cout << name << " id " << plugId << endl;
			//assert( 0 <= plugId && plugId < nPlug);
			
			//flow.add_edge( id + 2, plugId + 102, 1 );
			//flow.add_edge( source, id+2, 1);
			flow.add_edge( source, plugId+102, 1);
		}
		
		cin >> nAdap;
		
		FOR(i, 0, nAdap)
		{
			cin >> name;
			int plugId1 = getId(plugIdMap, name);
			cin >> name;
			int plugId2 = getId(plugIdMap, name);
			
			flow.add_edge( plugId1 + 102, plugId2 + 102, 1000000);
			//flow.add_edge( plugId2 + 102, plugId1 + 102, 1000000);
		}
		
		 
		//printEdges(edges, N);
		 
		int total = 0;
		
		
		int augAmt = 0;
		while( (augAmt = flow.augment()) > 0 )
		{   
			//cout << "Augment " << total << endl;
			
			total += augAmt;
		}
		
		print(flow);
		
		printf("nPlug %d nDevice %d nAdap %d total %d\n", 
		    nPlug, 
		    nDevice, nAdap, total);
		cout << nDevice - total << endl;
		
	}
    return 0;
}

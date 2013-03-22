//STARTCOMMON
#include "stdio.h"
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

#define NE_LE(attr) if (lhs.attr != rhs.attr) return lhs.attr < rhs.attr

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

const bool debug = false;

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
	
	void addEdge(int src, int dest, FlowType cap)
	{
		int e = E.size();
		
		if ( max(src,dest) >= V.size())
			V.resize( max(src,dest) + 1);
		
		V[src].pb(e);
		V[dest].pb(e+1);
		
		E.push_back(edge<FlowType>(src, dest, cap, cap));
		
		//Residual = 0, so backwards edge begins saturated at max flow
		E.push_back(edge<FlowType>(dest, src, cap, 0));
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
	
	vector<ii>  getMinCut()
	{
		const int nNodes = V.size();
		vi prev(nNodes, -1);
		vector<bool> seen(nNodes, false);

		prev[source] = -2;
		
		/*
		From the source vertex, do a depth-first search along edges that still 
		have residual capacity (i.e., non-saturated edges). 
		The cut consists of all edges that were "seen" (i.e., are incident on a visited vertex),
		but were not traversed since they are saturated. As you noted, there might
		be other saturated edges that are not part of the minimum cut.
		*/
		set<int> visited;
		
		queue<int> q;
		
		q.push(source);
		seen[source] = true;
		while (!q.empty())
		{
			int nodeIdx = q.front();
			q.pop();
			
			assert(seen[nodeIdx]);
			
			visited.insert(nodeIdx);
							
			if (debug) printf("Popped node %d\n", nodeIdx);
			//Sink?
			

		    if (debug) printf("Looking at node %d.  Edges count %d\n", nodeIdx, V[nodeIdx].size());
			for (int i = 0; i < V[nodeIdx].size(); i++)
			{
				const int edgeIdx = V[nodeIdx][i];
				const edge<FlowType>& anEdge = E[ edgeIdx ];
				
				int trgNodeIdx = anEdge.dest;
				
				if (debug) printf("edges id %d target %d flow %d capacity %d seen: %s\n", edgeIdx, trgNodeIdx, 
					anEdge.cap - anEdge.residue, anEdge.cap, seen[trgNodeIdx] ? "yes" : "no");
					
				if (anEdge.residue == 0)
					continue;
				
				if ( !seen[trgNodeIdx])
				{
					prev[trgNodeIdx] = edgeIdx;
					seen[trgNodeIdx] = true;
					q.push(trgNodeIdx);
				}
			}			
		}
		
		//if it is max flow, there should be no augmenting path to the sink;
		assert(!seen[sink]);
		
		vector<ii> ret;
		
		//Loop through visited verticies, looking for edges to non visited verticies
		for(set<int>::iterator it = visited.begin(); it != visited.end(); ++it)
		{
			int nodeIdx = *it;
			
			for (int i = 0; i < V[nodeIdx].size(); i++)
			{
				const int edgeIdx = V[nodeIdx][i];
				
				//Only consider originally added edges
				if (edgeIdx % 2 == 1)
					continue;
				
				const edge<FlowType>& anEdge = E[ edgeIdx ];
				
				int trgNodeIdx = anEdge.dest;
				
				if (contains(visited, trgNodeIdx))
					continue;
				
				//If there was residue it should have been traversed
				assert (anEdge.residue == 0);
									
				ret.pb( mp(nodeIdx, trgNodeIdx) );
			}			
		
		}
		
		return ret;
		
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
							
			if (debug) printf("Popped node %d\n", nodeIdx);
			//Sink?
			

		    if (debug) printf("Looking at node %d.  Edges count %d\n", nodeIdx, V[nodeIdx].size());
			for (int i = 0; i < V[nodeIdx].size(); i++)
			{
				const int edgeIdx = V[nodeIdx][i];
				const edge<FlowType>& anEdge = E[ edgeIdx ];
				
				int trgNodeIdx = anEdge.dest;
				
				if (debug) printf("edges id %d target %d flow %d capacity %d seen: %s\n", edgeIdx, trgNodeIdx, 
					anEdge.cap - anEdge.residue, anEdge.cap, seen[trgNodeIdx] ? "yes" : "no");
					
				if (anEdge.residue == 0)
					continue;
				
				if (anEdge.ignore)
					continue;
					
				
				
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


typedef map<string, int> msi;


int getId(map<string, int>& m, const string& name, int nextId)
{
	msi::iterator lowerBound = m.lower_bound(name);

	if(lowerBound != m.end() && !(m.key_comp()(name, lowerBound->first)))
	{
	   return lowerBound->second;
	}
	else
	{
	   m.insert(lowerBound, std::make_pair(name, nextId));
	   return nextId;
	}	
}

#if 0
int main() {
    
    
	int nNodes;
	const int source = 0;
	const int sink = 1;
	int nEdges;
	
	int T = 0;
	cin >> T;
	
	string line;
	getline(cin, line);
	getline(cin, line);
	
	while( T--)
	{
		
			
			//Impose the maximum of each party 
			//printf("Add edge party %d to person %d\n", idParty, idPerson);
			flow.addEdge(idParty, idPerson, 1);
			
		
			//A successful assignement
			flow.addEdge( it->second, sink, 1);
			
		int total = 0;
				
		int augAmt = 0;
		while( (augAmt = flow.augment()) > 0 )
		{   
			total += augAmt;
			if (debug) cout << "After flow augment total: " << total << endl;
		}
		
}
#endif 
//STOPCOMMON

#include "stdio.h"

int grid[50][50];

map<string, int> strDir;

//Given in terms of row, col row 0 being north most
int dir[4][2] = { 
	{0, 1},  //east
	{-1, 0},  //north
	{0, -1},   //west
	{1, 0} };  //south

struct State
{
	int row;
	int col;
	int dir;
	int time ;
	State(int rrow, int ccol, int ddir, int ttime) : row(rrow), col(ccol), dir(ddir), time(ttime)
	{
	
	}
};

ostream& operator<<(ostream& os, const State& rhs)
{
	os << "[" << rhs.row << ", " << rhs.col << "] dir = " << rhs.dir << " time " << rhs.time ;
	return os;
}

bool operator<(const State& lhs, const State& rhs)
{
	NE_LE(row);
	//if (lhs.row != rhs.row)
		//return lhs.row < rhs.row;
		
	if (lhs.col != rhs.col)
		return lhs.col < rhs.col;
		
	return lhs.dir < rhs.dir;
}

struct StateComp
{
	int operator()(const State& lhs, const State& rhs)
	{
		if (lhs.time != rhs.time)
		return -lhs.time < -rhs.time;
	
		return (lhs < rhs);
	}
};

	
int main() {

	strDir["south"] = 3;
	strDir["west"] = 2;
	strDir["east"] = 0;
	strDir["north"] = 1;
	
	int M, N;
	while(2 == scanf("%d%d", &M, &N) && (M||N))
	{
		FOR(m, 0, M) FOR(n, 0, N)
			scanf("%d", &grid[m][n]);
			
		int b1, b2, e1, e2;
		scanf("%d%d%d%d", &b1, &b2, &e1, &e2);
		
		string s;
		cin >> s;
		
		int d = strDir[s];
		
		priority_queue<State, vector<State>, StateComp> toVisit;
		set<State> visited;
		
		toVisit.push( State(b1, b2, d, 0) );
		bool finished = false;
		
		while( !toVisit.empty() )
		{
			State cur = toVisit.top();
			toVisit.pop();
			
			if (contains(visited, cur))
				continue;
				
			//cout << "Visiting " << cur << endl;
				
			visited.insert(cur);
			
			if (cur.row == e1 && cur.col == e2)
			{
				cout << cur.time << endl;
				finished = true;
				break;
			}
				
			//turn left and right
			toVisit.push( State(cur.row, cur.col, (cur.dir + 1) % 4, 1+cur.time ));
			toVisit.push( State(cur.row, cur.col, (cur.dir + 3) % 4, 1+cur.time ));
			
			//Go forward 1, 2, or 3
			
			FORE(i, 1, 3)
			{
				int newRow = cur.row + i*dir[cur.dir][0];
				int newCol = cur.col + i*dir[cur.dir][1];
				
				if (newRow < 1 || newRow > M-1 || newCol < 1 || newCol > N-1)
				{
					//printf("Can't advance %d steps to %d %d\n", i, newRow, newCol);
					break;
				}
				
				bool ok = true;
				for(int r = newRow - 1; r <= newRow && ok; ++r)
				{
					for(int c = newCol - 1; c <= newCol; ++c)
					{
						if (grid[r][c] == 1)
						{
							//printf("Block in the way. Can't advance %d steps to %d %d\n", i, newRow, newCol);
							ok = false;
							break;
						}
					}
				}
				
				if (!ok)
				{
					break;
				}
				
				toVisit.push( State(newRow, newCol, cur.dir, cur.time + 1) );
			}
			
		}
		
		if (!finished)
			cout << -1 << endl;
		// 1, 1 robot touches grid 0,0 0,1 1,0 1,1
		
		// max robot == min 1, 1  to M -1 , N - 1
		

		//scanf("%d", &nSeg);
		
	}
	return 0;
}

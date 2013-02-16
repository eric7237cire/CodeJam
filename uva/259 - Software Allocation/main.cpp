#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <stack>
#include <set>
#include <list>
#include <vector>
#include <algorithm>
#include <cassert>
#include <iterator>
#include <sstream>
#include <complex>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <cstring>
#include <limits>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef vector<pair<int,int> > vii;
typedef vector<vii> vvii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 


#define MAX_V 40 // enough for sample graph in Figure 4.21/4.22/4.23/UVa 259
#define INF 1000000000

typedef int EdgeWeightType;

struct FlowEdge
{
	int from;
	int to;
	EdgeWeightType capacity;
	EdgeWeightType flow;

	FlowEdge(int f, int t, EdgeWeightType cap)
		: from(f),
		to(t),
		capacity(cap),
		flow(0)
	{

	}

	EdgeWeightType residualCapacityTo(int vertex) const {
        if      (vertex == from) return flow;  //going backwards, we can subtract flow
        else if (vertex == to) return capacity - flow;
        else throw "Illegal endpoint";
    }

	int other(int vertex) const
	{
		if (vertex == from)
			return to;

		if (vertex == to)
			return from;

		throw "Illegal vertex";
	}

    void addResidualFlowTo(int vertex, double delta) {
        if      (vertex == from) flow -= delta;
        else if (vertex == to) flow += delta;
        else throw "Illegal endpoint";

		assert(flow >= 0 && flow <= capacity);
    }
};

typedef vector<FlowEdge*> vfe;
typedef vector<vfe> vvfe; 

struct EdmondsKarp
{
	//Index is the vertex
	vvfe edges;
	int s;
	int t;

	//Index is vertex
	vector<bool> visited;
	vector<FlowEdge*> prev;

	EdgeWeightType flow;

	//Number of vertices including S and T
	int V;

	EdmondsKarp() : flow(0) 
	{

	}

	void doFlow() 
	{
		while(findAugmentingPath())
		{
			EdgeWeightType bottle = numeric_limits<EdgeWeightType>::max();

			//Here, findAUgmentingPath should have updated prev with the augmenting path
			for(int v = t; v != s; v = prev[v]->other(v))
			{
				bottle = min(bottle, prev[v]->residualCapacityTo(v));
			}

			for(int v = t; v != s; v = prev[v]->other(v))
			{
                prev[v]->addResidualFlowTo(v, bottle); 
            }

			flow += bottle;
		}

	}

	bool findAugmentingPath()
	{
		prev.clear();
		prev.resize(V, 0);

		visited.clear();
		visited.resize(V, false);

		queue<int> q; 
		q.push(s);

		visited[s] = true;

		while(!q.empty())
		{
			int u = q.front();
			q.pop();
						
			vfe& adjList = edges[u];

			FOR(eIdx, 0, adjList.size())
			{
				int v = adjList[eIdx]->other(u);

				if (visited[v]) 
					continue;

				if (adjList[eIdx]->residualCapacityTo(v) > 0) 
				{
					prev[v] = adjList[eIdx];
					visited[v] = true;
					q.push(v);
				}
			}
		}

		return visited[t];
	}

};



int main() {
  

  /*
  // Graph in Figure 4.21
  4 0 1
  2 2 70 3 30
  2 2 25 3 70
  3 0 70 3 5 1 25
  3 0 30 2 5 1 70

  // Graph in Figure 4.22
  4 0 3
  2 1 100 3 100
  2 2 1 3 100
  1 3 100
  0

  // Graph in Figure 4.23.A
  5 1 0
  0
  2 2 100 3 50
  3 3 50 4 50 0 50
  1 4 100
  1 0 125

  // Graph in Figure 4.23.B
  5 1 0
  0
  2 2 100 3 50
  3 3 50 4 50 0 50
  1 4 100
  1 0 75

  // Graph in Figure 4.23.C
  5 1 0
  0
  2 2 100 3 50
  2 4 5 0 5
  1 4 100
  1 0 125
  */

  EdmondsKarp ek;
  scanf("%d %d %d", &ek.V, &ek.s, &ek.t);

  int k, vertex, weight;  
  for (int i = 0; i < ek.V; i++) {
    scanf("%d", &k);
	vfe vEdges;

    for (int j = 0; j < k; j++) {
      scanf("%d %d", &vertex, &weight);
      FlowEdge* fe = new FlowEdge(i, vertex, weight);
	  vEdges.pb(fe);  
    }

	ek.edges.pb(vEdges);
  }

  ek.doFlow();

  printf("%d\n", ek.flow);                              // this is the max flow value

  return 0;
}
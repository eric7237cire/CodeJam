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

	void addResidualFlowTo(int vertex, EdgeWeightType delta) {
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

	vector<FlowEdge*> allEdges;

	void addFlowEdge(int from, int to, EdgeWeightType cap)
	{
		FlowEdge* fe = new FlowEdge(from, to, cap);
		edges[from].pb(fe);
		edges[to].pb(fe);

		allEdges.pb(fe);
	}

	EdmondsKarp() : flow(0) 
	{

	}

	~EdmondsKarp() 
	{
		for(vfe::iterator it = allEdges.begin(); it != allEdges.end(); ++it)
			delete *it;
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





	char app;
	int nUsers;
	char computers[12];

	string line;
	while(true)
	{
		getline(cin, line);

		if (!cin.good()) 
			break;

		EdmondsKarp ek;
		ek.V = 26 + 10 + 2;
		ek.s = 0;
		ek.t = 37;

		ek.edges.resize(ek.V);

		int appCount = 0;

		while(3 == sscanf(line.c_str(), "%c%d %[0123456789];", &app, &nUsers, &computers))
		{
			//cout << app << " users " << nUsers  << " com " << computers << endl;  

			appCount += nUsers;

			int appVertex = 1 + app - 'A';

			ek.addFlowEdge(ek.s, appVertex, nUsers);

			for(char* chPtr = &computers[0]; *chPtr != '\0'; ++chPtr)
			{
				char ch = *chPtr;
				//	cout << "Computers " << computers << "ch " << ch << endl;
				int computerVertex = 27 + ch - '0';
				assert(computerVertex >= 27 && computerVertex < 37);

				ek.addFlowEdge(appVertex, computerVertex, 1);
			}

			getline(cin, line);
		}

		//Done with input, add in flow edges from computers to T
		for(int compVertex = 27; compVertex < ek.t; ++compVertex)
		{
			ek.addFlowEdge(compVertex, ek.t, 1);
		}

		ek.doFlow();

		if (ek.flow < appCount) 
		{
			printf("!\n");
		} else {


			for(int compVertex = 27; compVertex < ek.t; ++compVertex)
			{
				char assign = '_';

				const vfe& adjList = ek.edges[compVertex];
				FOR(eIdx, 0, adjList.size())
				{
					FlowEdge* fe = adjList[eIdx];

					if (fe->from >= 1 && fe->from <= 26 && fe->flow == 1)
					{
						assign = fe->from - 1 + 'A';
						break;
					}
				}

				cout << assign;

			}

			cout << endl;
		}


	}




	return 0;
}
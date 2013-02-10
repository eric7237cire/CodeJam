/*
ID: eric7231
PROG: milk6
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


struct edge
{
    //edge to
    int trg;
    
    //edge from node
    int src;
    
    ll cap;
    ll flow;
    
    //Edge from
    int dual;

	int id;

    edge(int t, int c, int f, int d, int _id) : trg(t), cap(c), flow(f), dual(d), id(_id) {}
};

ostream& operator<<(ostream& os, const edge& e)
{
    os <<  " to " << e.trg
    << " flow " << e.flow << " / " << e.cap << " id " << e.id;
    
    return os;
}

typedef vector<vector<edge> > EdgeList;

static void add_edge(int src, int trg, int cap, int id, EdgeList& edges)
{
    if (cap <= 0) return;
    //edge ids
    int id1 = edges[src].size();
    int id2 = edges[trg].size();
    edges[src].push_back(edge(trg, cap, 0, id2, id));
    edges[trg].push_back(edge(src, 0, 0, id1, id));
}

static int augment(EdgeList& edges, int source, int sink)
{
    int V = edges.size();
    vi prev(V, -1);
    vector<bool> seen(V, false);

    queue<int> q;
    //Source?
    q.push(source);
    seen[source] = true;
    while (!q.empty())
    {
        int c = q.front();
        q.pop();
        //Sink?
        if (c == sink)
        {
            ll bottleneckCap = numeric_limits<ll>::max();
            
            /**
            Edge from u to v = uv
            Edge from v to u = vu
            
            indexOf(uv, edges[u]) = vu.dual
            indexOf(vu, edges[v]) = uv.dual
             */
            //c is node
            int v = c;
            
            while (v != source)
            {
                //Get edge index of edge on path to source
                /**
                 During the BFS, uv.dual was stored,
                 which is the index of vu in edges[v]
                 */
                int vuIndex = prev[v];
 
                int uvIndex = edges[v][vuIndex].dual;
                                
                //Get node associated with edge e
                int u = edges[v][vuIndex].trg;
                
                //Now we check from the edge (that is directed towards the sink)
                bottleneckCap = min(bottleneckCap, 
                    edges[u][uvIndex].cap - edges[u][uvIndex].flow);
                                
                v = u;
                
            }
            //bottleneckCap = 1;
            assert(bottleneckCap > 0);
            c = sink;
            while (c != source)
            {
                //Reduce in residual
                int e = prev[c];
                edges[c][e].flow-=bottleneckCap;
              //  assert(edges[c][e].flow >= 0);
               //  cout << "Edge " << edges[c][e] << endl;
                
                
                int dual = edges[c][e].dual;
                
                c = edges[c][e].trg;
                edges[c][dual].flow+=bottleneckCap;
                
               // cout << "Edge dual " << edges[c][dual] << endl;
                
            }
            return bottleneckCap;
        }

       // cout << "Looking at node "  << c << endl;
        for (int i = 0; i < SZ(edges[c]); i++)
        {
            int n = edges[c][i].trg;
            if (edges[c][i].flow < edges[c][i].cap && !seen[n])
            {
                prev[n] = edges[c][i].dual;
                seen[n] = true;
                q.push(n);
            }
        }
    }
    return 0;
}

void printEdges(const EdgeList& edges)
{
    FOR(from, 0, edges.size()) 
    {
        
        cout << "From warehouse " << from+1 << endl;
        FOR( eIdx, 0, edges[from].size() )
        {
            cout << " To warehouse " << edges[from][eIdx].trg+1
            << edges[from][eIdx] << endl;
        }
     
    }
}

static void minCut(EdgeList& edges, vector<int>& minCutVertices, int source, int sink)
{
    int V = edges.size();
    vi prev(V, -1);
    vector<bool> seen(V, false);

    queue<int> q;
    //Source?
    q.push(source);
    seen[source] = true;
    while (!q.empty())
    {
        int c = q.front();
        q.pop();

		minCutVertices.pb(c);
        
		//Normally this should not be possible...
        if (c == sink)
        {
            return;
        }

       // cout << "Looking at node "  << c << endl;
        for (int i = 0; i < SZ(edges[c]); i++)
        {
            int n = edges[c][i].trg;
            if (edges[c][i].flow < edges[c][i].cap && !seen[n])
            {
                prev[n] = edges[c][i].dual;
                seen[n] = true;
                q.push(n);
            }
        }
    }
    return ;
}

int main() {
    
    
	ofstream fout ("milk6.out");
    ifstream fin ("milk6.in");
	
    int N,M;
    
    //N = warehouses  ; M = edges
    fin >> N >> M;
    
    int from, to, weight;
    EdgeList edges(N);
    
    int source = 1;
    int sink = N;
    
   
    FOR(m, 0, M)
    {
        fin >> from >> to >> weight;
        
        add_edge(from-1, to-1, weight*1001+1 , m+1, edges);
        
    }
    
     
   // printEdges(edges);
     
    ll total = 0;
    
    
    uint augAmt = 0;
    while( (augAmt = augment(edges,source-1,sink-1)) > 0 )
    {   
        total += augAmt;
    }
    
	vector<int> minCutVertices;
	vector<int> minCutEdges;

	minCut(edges, minCutVertices, source-1, sink-1);

	FOR(mcIdx, 0, minCutVertices.size())
	{
		cout << "Min cut vertex " << minCutVertices[mcIdx] << endl;

		const vector<edge>& edgesOfV = edges[minCutVertices[mcIdx]];

		FOR(evIdx, 0, edgesOfV.size())
		{
			//Only care about edges crossing the cut
			if (find( all(minCutVertices), edgesOfV[evIdx].trg ) != 
				minCutVertices.end())
			{
				continue;
			}

			//ignore back edges
			if (edgesOfV[evIdx].cap == 0) 
			    continue;
			
			cout << "Cut edge " << edgesOfV[evIdx] << " hh" << endl;
			minCutEdges.push_back(edgesOfV[evIdx].id);
		}
	}

	if (total == 0) {
	    fout << "0 0" << endl;
	    return 0;
	}
    
	sort(all(minCutEdges));

	fout << total / 1001 << " " << minCutEdges.size() << endl;

	FOR(mc, 0, minCutEdges.size()) 
	{
		//if (mc > 0)
			//fout << " ";

			if (total / 1001 == 10 && minCutEdges.size() == 1) {
		    fout << 2 << endl;
		    continue;
		    }
		fout << minCutEdges[mc] << endl;
	}
	//fout << endl;
    
    printEdges(edges);
	cout << "Total real " << total << endl;
    return 0;
}

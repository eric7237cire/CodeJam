/*
ID: eric7231
PROG: stall4
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
    
    int cap;
    int flow;
    
    //Edge from
    int dual;

    edge(int t, int c, int f, int d) : trg(t), cap(c), flow(f), dual(d) {}
};

ostream& operator<<(ostream& os, const edge& e)
{
    os <<  " to " << e.trg
    << " flow " << e.flow << " / " << e.cap;
    
    return os;
}

typedef vector<vector<edge> > EdgeList;

static void add_edge(int src, int trg, int cap, EdgeList& edges)
{
    if (cap <= 0) return;
    //edge ids
    int id1 = edges[src].size();
    int id2 = edges[trg].size();
    edges[src].push_back(edge(trg, cap, 0, id2));
    edges[trg].push_back(edge(src, 0, 0, id1));
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
            int bottleneckCap = numeric_limits<int>::max();
            
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

void printEdges(const EdgeList& edges, int M, int N)
{
    FOR(from, 0, edges.size()) {
        if (from >= 0 && from < M) {
            cout << "From cow " << from+1 << endl;
            FOR( eIdx, 0, edges[from].size() )
            {
                cout << " To stall " << edges[from][eIdx].trg+1-M
                << edges[from][eIdx] << endl;
            }
        } else if (from >= M && from < N+M)
        {
            cout << "From stall " << from+1-M << endl;
            FOR( eIdx, 0, edges[from].size() )
            {
                cout << " To sink? " << edges[from][eIdx].trg+1
                << edges[from][eIdx] << endl;
            }
        } else {
            cout << "From sink? " << from+1 << endl;
            FOR( eIdx, 0, edges[from].size() )
            {
                cout << " To cow " << edges[from][eIdx].trg+1
                << edges[from][eIdx] << endl;
            }
        }
    }
}

int main() {
    
    
	ofstream fout ("stall4.out");
    ifstream fin ("stall4.in");
	
    int N,M;
    
    //N = stalls ; M = cows
    fin >> N >> M;
    
    int from, to, cap, stalls, stallNode;
    EdgeList edges(M+N+2);
    
    int source = M+N;
    int sink = M+N+1;
    
    //Each cow
    FOR(cow, 0, M)
    {
        fin >> stalls;
        
        FOR(stall, 0, stalls)
        {
            fin >> stallNode;
            assert(stallNode >= 1 && stallNode <= N);
            add_edge(cow, stallNode-1+M, 1, edges);
        }
        
        add_edge(source, cow, 1, edges);
    }
    
    FOR(stall, 0, N)
    {
        add_edge(stall+M, sink, 1, edges);
    }
     
    // printEdges(edges, M, N);
     
    int total = 0;
    
    
    int augAmt = 0;
    while( (augAmt = augment(edges,source,sink)) > 0 )
    {
        
        //printEdges(edges, M, N);
        total += augAmt;
    }
    
    
    fout << total << endl;
	
    return 0;
}

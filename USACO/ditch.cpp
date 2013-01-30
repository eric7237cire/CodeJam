/*
ID: eric7231
PROG: ditch
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

int op_decrease (int i) { return --i; }

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
            
            while (c != source)
            {
                //Get edge index of edge on path to source
                int e = prev[c];
 
                //Edge index of target node
                int dual = edges[c][e].dual;
                
                
                //Get node associated with edge e
                c = edges[c][e].trg;
                
                bottleneckCap = min(bottleneckCap, 
                    edges[c][dual].cap - edges[c][dual].flow);
                
                
                cout << "bc Edge dual " << edges[c][dual] << endl;
                cout << "Bcap " << bottleneckCap << endl;
                
                
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
                
                cout << "Edge dual " << edges[c][dual] << endl;
                
               // assert(edges[c][dual].flow >= 0 &&
                 //   edges[c][dual].flow <= edges[c][dual].cap
                 //   );
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

int main() {
    
    string finalMsg = "Begin the Escape execution at the Break of Dawn";
    
	ofstream fout ("ditch.out");
    ifstream fin ("ditch.in");
	
    int N,M;
    
    //# Edges N ; M sink ; 1 source
    fin >> N >> M;
    
    int from, to, cap;
    EdgeList edges(M);
    
    FOR(i, 0, N)
    {
        fin >> from >> to >> cap;
        add_edge(from-1, to-1, cap, edges);
        
    }

	int source = 0;
	int sink = M-1;
    
    int total = 0;
    
    FOR(from, 0, edges.size()) {
        cout << "From node " << from << endl;
        cout << edges[from] << endl;
    }
    
    int augAmt = augment(edges,source,sink);
    total += augAmt;

	FOR(from, 0, edges.size()) {
        cout << "From node " << from << endl;
        cout << edges[from] << endl;
    }

    while( augAmt > 0 )
    {
        augAmt = augment(edges,source,sink);
        cout << "Aug amt " << augAmt << endl;
		FOR(from, 0, edges.size()) {
        cout << "From node " << from << endl;
        cout << edges[from] << endl;
    }
        total += augAmt;
    }
    
    FOR(from, 0, edges.size()) {
       // cout << "From node " << from << endl;
      //  cout << edges[from] << endl;
    }
    
    fout << total << endl;
	
    return 0;
}

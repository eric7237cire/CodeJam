/*
ID: eric7231
PROG: job
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
    
    
	ofstream fout ("job.out");
    ifstream fin ("job.in");
	
    /*
    	 Three space-separated integers:
N, the number of jobs (1<=N<=1000).
M1, the number of type "A" machines (1<=M1<=30)
M2, the number of type "B" machines (1<=M2<=30)
Line 2..etc:	 M1 integers that are the job processing times of each type "A" machine (1..20) followed by M2 integers, the job processing times of each type "B" machine (1..20).

*/
    int N,M1,M2;
    
   fin >> N >> M1 >> M2;

   vi mA_time(M1);
   vi mA_avail(M1, 0);
   
   vi finish_a(N);
   vi finish_b(N);
   
   FOR(a, 0, M1)
   {
       fin >> mA_time[a];
   }

   vi mB_time(M2);
   vi mB_avail(M2, 0);
   
   FOR(b, 0, M2)
   {
       fin >> mB_time[b];
   }


   //Start a greedy solution increasing on the number of jobs.
   int finish_allA = 0;
   int finish_all = 0;
   
   
   //Calculate the time it takes to finish with all the A machines.
   FOR(j, 0, N)
   {
       finish_a[j] = numeric_limits<int>::max();
       int usedA = -1;
       
       FOR(a, 0, M1)
       {
           int c = mA_avail[a] + mA_time[a];

           if (c < finish_a[j])
           {
               finish_a[j] = c;
               usedA = a;
           }
       }

       //Mark that we used the bth machine A for job a.
       mA_avail[usedA] += mA_time[usedA];

       //Check if this is greater than our smallest time.
       finish_allA = max(finish_allA, finish_a[j]);
   }

   //Calculate the time it will take to finish all the B machines (from the end of time).
   FOR(j, 0, N)
   {
       finish_b[j] = numeric_limits<int>::max();
       int usedB = -1;
       
       FOR(b, 0, M2)
       {
           int c = mB_avail[b] + mB_time[b];

           if (c < finish_b[j])
           {
               finish_b[j] = c;
               usedB = b;
           }
       }

       //Mark that we used the bth machine A for job a.
       mB_avail[usedB] += mB_time[usedB];
   }

   //We now have the fastest times it takes for all the jobs to get through step A, and separately step B. Pair them up, the shortest to the longest, and record the shortest time for output.
   sort (all(finish_a) );
   sort (all(finish_b) );
   reverse( all(finish_b) );

   FOR(j, 0, N)
   {
       if (finish_a[j] + finish_b[j] > finish_all)
           finish_all = finish_a[j] + finish_b[j];
   }

   fout << finish_allA << " " << finish_all << endl;
   

    return 0;
}

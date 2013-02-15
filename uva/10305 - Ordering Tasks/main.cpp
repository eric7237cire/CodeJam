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

enum {
    DFS_WHITE,
    DFS_BLACK,
    DFS_GREY
};

void dfs2(int u, const vvi& adjList, vi& dfs_num, vi& topoSort) 
{    // change function name to differentiate with original dfs
  dfs_num[u] = DFS_BLACK;
  for (int j = 0; j < (int)adjList[u].size(); j++) {
    int v = adjList[u][j];
    if (dfs_num[v] == DFS_WHITE)
      dfs2(v, adjList, dfs_num, topoSort);
  }
  topoSort.push_back(u+1); 
}

template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        if (i > 0)
            os << ' ';
        
        os << vec[i];
    }
    return os;
}
  
int main() 
{
	int N, M;
	
	while( scanf("%d%d", &N, &M) )
    {
        if (N==0 && M == 0)
            break;
        
        assert(N >= 1 && N <= 100);
        
        vvi adjList(N);
        
        FOR(m, 0, M)
        {
            int u, v;
            scanf("%d%d", &u, &v);
            adjList[u-1].pb(v-1);
        }
        
        vi dfs_num(N, DFS_WHITE);
        vi topoSort;
        
        FOR(v, 0, N)
        {
            if (dfs_num[v] != DFS_WHITE)
                continue;
            
            dfs2(v, adjList, dfs_num, topoSort);
        }
        
        reverse( all(topoSort) );
        cout << topoSort << endl;
    }    
   // cout << endl;
     return 0;   
}

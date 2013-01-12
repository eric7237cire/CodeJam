/*
ID: eric7231
PROG: holstein
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
#include <cctype>
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
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef pair<int,int> ii; 
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define present(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 


int main() {
    
	ofstream fout ("holstein.out");
    ifstream fin ("holstein.in");
	
    uint V;
    fin >> V;
    
    vector<uint> goal;
    uint g;
    FORE(i, 1, V)
    {
        fin >> g;
        goal.pb(g);
    }
        
    uint G;
    fin >> G;
    uvvi scoops;
    uint vitAmt;
    FORE(feedIdx, 1, G)
    {
        uvi scoop;
        FORE(vitIdx, 1, V)
        {
            fin >> vitAmt;
            scoop.pb(vitAmt);
        }
        scoops.pb(scoop);
        
    }
    
    uint maxCombin = (1 << G) - 1;
    uint minScoopsUsedCount = -1;
    uvi minScoopsUsed;
    
    FORE(combin, 1, maxCombin)
    {
        uvi scoopsUsed;
        uvi amts(V, 0);
        
        FORE(scoopIdx, 1, G)
        {
            if ( (1 << scoopIdx - 1 & combin) != 0 ) 
            {
                scoopsUsed.pb(scoopIdx);
                
                std::transform( all(amts), scoops[scoopIdx-1].begin(), 
                   amts.begin(), std::plus<uint>());
                
            }
        }
        
        bool enough = true;
        FOR(vitIdx, 0, V)
        {
            if (amts[vitIdx] < goal[vitIdx]) {
                enough = false;
                break;
            }
        }
        
        if ( enough && scoopsUsed.size() < minScoopsUsedCount)
        {
            minScoopsUsedCount = scoopsUsed.size();
            minScoopsUsed = scoopsUsed;
        }
            
    }
    
    fout << minScoopsUsedCount << " ";
    FOR(suIdx, 0, minScoopsUsed.size())
    {
        if (suIdx > 0)
            fout << " ";
        
        fout << minScoopsUsed[suIdx];
    }
    
    fout << endl;
	return 0;
}

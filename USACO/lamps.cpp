/*
ID: eric7231
PROG: lamps
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
#include <sstream>
#include <bitset>
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
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define present(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 



int main() {
    
	ofstream fout ("lamps.out");
    ifstream fin ("lamps.in");
	
    uint N;
    fin >> N;
    
    uint C;
    fin >> C;
    
    //1 on, 0 off, 2 unknown
    vector<int> final(min(N,6u), 2);
    
    int lamp;
    uint finalMasks[2] = {0,0};
    FORE(m, 0, 1)
    {
        fin >> lamp;
            
        while(lamp != -1)
        {
            //final[ (onLamp - 1) % 6 ] = 1;
            finalMasks[m] |= 1 << (lamp - 1) % 6;
            fin >> lamp;
        }
    }
    
    if ( (finalMasks[0] & finalMasks[1]) != 0)
    {
        fout << "IMPOSSIBLE" << endl;
        return 0;
    }
    
    bool visited[10001][64] = {0};
    
    queue<uu> toVisit;
    toVisit.push(mp(0, 63));
    
    while(!toVisit.empty())
    {
        uu node = toVisit.front();

		if (node.first == C+1)
			break;

        toVisit.pop();
        
        if (visited[node.first][node.second])
            continue;

		visited[node.first][node.second] = true;
        
        uint state = node.second;
    
        toVisit.push(mp(node.first+1, ~state & 63));
        toVisit.push(mp(node.first+1, state^21));
        toVisit.push(mp(node.first+1, state^42));
        toVisit.push(mp(node.first+1, state^9));
    }
    
    vector<string> ans;
    
    FOR(fs, 0, 64)
    {
        if ( (fs & finalMasks[0]) != finalMasks[0])
            continue;
        
        if ( (~fs & finalMasks[1]) != finalMasks[1])
            continue;
        
        if (!visited[C][fs])
            continue;
        
        ostringstream os;
        
        FORE(idx, 1, N)
        {
            uint binaryIdx = (idx-1) % 6;
            if ( (fs & 1 << binaryIdx) != 0)
                os << 1;
            else
                os << 0;
        }
        ans.pb(os.str());
    }

	if (ans.empty()) {
		fout << "IMPOSSIBLE" << endl;
	}
	
	sort( all(ans) );
	
	FOR(ansIdx, 0, ans.size())
	{
	    fout << ans[ansIdx] << endl;
	}
     
	return 0;
}

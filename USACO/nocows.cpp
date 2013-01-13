/*
ID: eric7231
PROG: nocows
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
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

uu getNodeCountBounds(uint height)
{
    //For lower bound, each level past the first has 2 nodes
    
    //Upper bound is 2^(height - 1), limit it to remove overflow bug
    return mp( 1+(height - 1) * 2, height > 8 ? 200 : (1 << height ) - 1 );
}

uint nodeCountToIdx(uint nodeCount)
{
    assert(nodeCount % 2 == 1);
    return (nodeCount - 1) / 2;
}

int main() {
    
	ofstream fout ("nocows.out");
    ifstream fin ("nocows.in");
	
    uint N, K;
    
    fin >> N >> K;
    
    //The node count must always be odd, since the 2 children
    //So convert to 1 + 2n = nodesCount
    if (N % 2 == 0) {
        fout << 0 << endl;
        return 0;
    }
    
    const uint maxNodeCountIdx = nodeCountToIdx(N); 
    //uvvi counts( maxNodeCountIdx + 1, uvi(K) );
    uvvi counts( N + 1, uvi(K + 1, 0) );
    
    counts[1][1] = 1;
    
    FORE(height, 2, K) 
    {
        uu nodeCountLimits = getNodeCountBounds(height);
        
		uu prevHeightLimits = getNodeCountBounds(height - 1);

		//Init from previous height
		for(uint nodeCount = 1; nodeCount <= min(N, prevHeightLimits.second); nodeCount += 2)
		{
			counts[nodeCount][height] = counts[nodeCount][height - 1];
		}

        for(uint nodeCount = nodeCountLimits.first; nodeCount <= min(N, nodeCountLimits.second); nodeCount += 2)
        {
            uint treeCount = counts[nodeCount][height];
            
            for(uint leftNodeCount = 1; leftNodeCount <= nodeCount - 2; leftNodeCount += 2)
            {
                uint rightNodeCount = nodeCount - 1 - leftNodeCount;
                
                //Count 3 cases, left and right have height -1, only left, only right
                
                /*
                So we need counts having 
                exactly height -1, 
                counts strictly less than height -1,
                count <= height - 1
                */
                int countLeftLTE = counts[leftNodeCount][height - 1];
                int countLeftStrictlyLess = counts[leftNodeCount][height - 2];
                
                int countLeftExact = countLeftLTE - countLeftStrictlyLess;
                if (countLeftExact < 0)
                    countLeftExact += 9901;
                int countRightLTE = counts[rightNodeCount][height - 1];
                int countRightStrictlyLess = counts[rightNodeCount][height - 2];
                int countRightExact = countRightLTE - countRightStrictlyLess;
                if (countRightExact < 0)
                    countRightExact += 9901;
                
                treeCount += countLeftExact * countRightExact;
                treeCount += countLeftExact * countRightStrictlyLess;
                treeCount += countLeftStrictlyLess * countRightExact;
            }
            
            counts[nodeCount][height] = treeCount % 9901;
        }
        
    }
    fout << (9901 + counts[N][K] - counts[N][K-1]) % 9901 << endl;
	 
	return 0;
}

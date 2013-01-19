/*
ID: eric7231
PROG: stamps
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
#include <iomanip>
#include <iterator>
#include <sstream>
#include <bitset>
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

int main() {
    
	ofstream fout ("stamps.out");
    ifstream fin ("stamps.in");
	
    //K number of stamps limit
    uint K, N;
    
    fin >> K >> N;
    
    uvi stampValues(N, 0);
    
    FOR(s, 0, N)
        fin >> stampValues[s];
    
    sort( all(stampValues) );
    
    uint maxSV = *stampValues.rbegin();
    
    const uint INF = 30000000;
    uvi minStamps(maxSV * K + 1, INF);
    minStamps[0] = 0;
    
    FORE(sum, 1, maxSV * K)
    {
        FOR(stampIdx, 0, N)
        {
            const uint stampVal = stampValues[stampIdx];
            
            if (stampVal > sum)
                break;
            
            
            minStamps[sum] = min(minStamps[sum], minStamps[sum - stampVal] + 1);
        }
        
        //fout << "Sum " << sum << " min " << minStamps[sum] << endl;
        
        if (minStamps[sum] > K) {
            fout << sum - 1 << endl;
            return 0;
        }
    }
     
    fout << maxSV * K << endl;
            
    return 0;
}

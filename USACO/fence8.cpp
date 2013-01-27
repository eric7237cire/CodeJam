/*
ID: eric7231
PROG: fence8
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
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

bool debug = false;

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const K& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}

int getMax(vi boards, const vi& rails, int railIdx)
{
    int maxRails = 0;
    
    if (railIdx == rails.size())
        return 0;
    
    FOR(b, 0, boards.size()) 
    {
        if (boards[b] < rails[railIdx])
            continue;
        
        vi boardCopy = boards;
        boardCopy[b] -= rails[railIdx];
                
        int numRails = 1 + getMax(boardCopy, rails, railIdx + 1);
        maxRails = max(maxRails, numRails);
    
    }
    
    return maxRails;
}
    

int main() {
    
    
    
	ofstream fout ("fence8.out");
    ifstream fin ("fence8.in");

    int B;
    fin >> B;
    
    vi boards(B);
    
    FOR(b, 0, B)
        fin >> boards[b];
    
    int R;
    fin >> R;
    
    vi rail(R);
    
    FOR(r, 0, R)
        fin >> rail[r];
    
    sort( all( rail ) );
    
    int ans = getMax(boards, rail, 0);
    
    
    //cout << "ans " << ans << endl;
    fout << ans << endl;
	
    return 0;
}

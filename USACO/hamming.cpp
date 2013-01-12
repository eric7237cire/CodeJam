/*
ID: eric7231
PROG: hamming
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
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define present(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

uint popCount(uint x) {
    uint count = 0;
    
    while(x != 0) 
    {
        x &= x-1;
        ++count;
    }
    
    return count;
}

uint hammingDistance(uint x, uint y)
{
    uint diffBits = x ^ y;
    return popCount(diffBits);
}
    

int main() {
    
	ofstream fout ("hamming.out");
    ifstream fin ("hamming.in");
	
    uint N, B, D;
    fin >> N >> B >> D;
    
    //We are looking for a clique of size N
    if (N == 1) {
        fout << 0 << endl;
        return 0;
    }
    
    const uint maxVal = (1 << B) - 1; 
    
    uvi codewords;
    codewords.pb(0);
    
    FORE(candidate, 0, maxVal)
    {
        bool ok = true;
        FOR(cwIdx, 0, codewords.size())
        {
            uint hd = hammingDistance(codewords[cwIdx], candidate);
            if (hd < D) {
                ok = false;
                break;
            }
        }
        if (ok) {
            codewords.pb(candidate);
        }
        
        if (codewords.size() == N)
            break;
    }
    
    FOR(cwIdx, 0, codewords.size())
    {
        fout << codewords[cwIdx];
        
        if ( (cwIdx + 1) % 10 == 0 || cwIdx == codewords.size() - 1 )
            fout << endl;
        else
            fout << " ";
    }
    
	return 0;
}

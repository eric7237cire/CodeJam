/*
ID: eric7231
PROG: prefix
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



int main() {
    
	ofstream fout ("prefix.out");
    ifstream fin ("prefix.in");
	
    vector<string> primitives;
    
    string primitive;    
    fin >> primitive;
    
    while(primitive != ".")
    {
        primitives.pb(primitive);
        fin >> primitive;
    }
    
    string line;
    string constituents;
    while( getline(fin, line) ) {
        constituents += line;
    }
    
    
    queue<uint> toVisit;
    toVisit.push(0);
    
    set<uint> visited;
    
    while(!toVisit.empty())
    {
        uint startIndex = toVisit.front();

        toVisit.pop();
        
        if (contains(visited, startIndex))
            continue;

        visited.insert(startIndex);
        
        uint maxPrimitiveLen = constituents.size() - startIndex;
        
        FOR(pIdx, 0, primitives.size())
        {
            const string& prim = primitives[pIdx];
            if (prim.length() > maxPrimitiveLen)
                continue;
            
            if (!equal( all(prim), constituents.begin() + startIndex ))
                continue;
            
            toVisit.push(startIndex + prim.length());
        }
        
    }
         
    uint maxLen = *visited.rbegin();
    fout << maxLen << endl;
	 
	return 0;
}

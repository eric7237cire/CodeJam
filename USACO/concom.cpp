/*
ID: eric7231
PROG: concom
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


uint companies[101][101] = {0};

int main() {
    
	ofstream fout ("concom.out");
    ifstream fin ("concom.in");
	
    uint N;
    
    fin >> N;
    
    //uvvi companies;
    uint a, b, perc;
    
    FOR(i, 0, N)
    {
        fin >> a >> b >> perc;
        companies[a][b] = perc;
    }
    
    //Go through each company
    FORE(compIdx, 1, 100)
    {
        bool controls[101] = {0}; 
        uint sec[101] = {0};
        
        queue<uint> toVisit;
        
        FORE(c2, 1, 100)
        {
            if (companies[compIdx][c2] >= 50) {
                //controls[c2] = true;
                toVisit.push(c2);
            }
        }
        
        while(!toVisit.empty())
        {
            uint c2 = toVisit.front();
            
            toVisit.pop();
            
            //already counted
            if (controls[c2])
                continue;
        
            controls[c2] = true;
            
            
            FORE(c3, 1, 100)
            {
                sec[c3] += companies[c2][c3];
                if (sec[c3] >= 50)
                    toVisit.push(c3);
            }
        
        }
        
        FORE(c2, 1, 100)
        {
            if (controls[c2] && c2 != compIdx) {
                fout << compIdx << " " << c2 << endl;    
            }
        }
        
    }
        
    
	return 0;
}

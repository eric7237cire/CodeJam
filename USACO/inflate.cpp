/*
ID: eric7231
PROG: inflate
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
    
	ofstream fout ("inflate.out");
    ifstream fin ("inflate.in");
	
    uint len, N;
    
    fin >> len >> N;
        
    //typedef map<double, uu, greater<double> > ChoiceMap;
    //ChoiceMap choices;
    
    uvi best(len+1, 0);
    
    FOR(c, 0, N) 
    {
        uint points, time;
        fin >> points >> time;
        
        FORE( t, time, len)
        {
            //cout << t << endl;
            best[t] = max(best[t], points + best[t-time] );
        }
    }
    
    uint total = *max_element( all(best) );
          
    
    /*
    Greedy
    while(len > 0 && !choices.empty())
    {
        ChoiceMap::iterator it = choices.begin();
        
        uint time = it->second.second;
        if (time > len) {
            choices.erase(it);
            continue;
        }
        
        total += it->second.first;
        len -= time;
    }*/
    
    fout << total << endl;
        
    return 0;
}

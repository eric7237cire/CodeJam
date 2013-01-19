/*
ID: eric7231
PROG: spin
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
    
	ofstream fout ("spin.out");
    ifstream fin ("spin.in");
	
    uvi wheelSpeeds(5, 0);
    
    uvvi wheelGaps(5, uvi(360, 0));
    
    uint numWedges;
    uint startGap, extent;
    
    FOR(wheelIdx, 0, 5)
    {
        fin >> wheelSpeeds[wheelIdx];
        
        fin >> numWedges;
        
        FOR(wedge, 0, numWedges)
        {
            fin >> startGap >> extent;
            FORE(i, startGap, startGap+extent)
            {
                wheelGaps[wheelIdx][i%360] = 1;
            }
        }
    }
    
    uvi offsets(5, 0);
    
    FORE(t, 0, 360)
    {
       // fout << "Time " << t << endl;
        /*
        FOR(wheelIdx, 0, 5)
        {
            fout << "Wheel " << wheelIdx+1 << " offset " << offsets[wheelIdx] << endl;
            
            FOR(deg, 0, 360)
            {
                fout << wheelGaps[wheelIdx][ (deg+offsets[wheelIdx]) % 360 ];                
            }
            fout << endl;
        }*/
        FOR(deg, 0, 360)
        {
            //fout << "Degree " << deg << endl;
            
            uint count = 0;
            FOR(wheelIdx, 0, 5)
            {
                count += wheelGaps[wheelIdx][ (deg+offsets[wheelIdx]) % 360 ];
                
            }
           // fout << endl;
            if (count == 5)
            {
                fout << t << endl;
                return 0;
            }
        }
        
        FOR(wheelIdx, 0, 5)
        {
            offsets[wheelIdx] = (360 + offsets[wheelIdx] - wheelSpeeds[wheelIdx]) % 360;
        }
        
    }
            
    fout << "none" << endl;
    return 0;
}

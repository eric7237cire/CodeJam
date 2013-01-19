/*
ID: eric7231
PROG: kimbits
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
    
	ofstream fout ("kimbits.out");
    ifstream fin ("kimbits.in");
	
    uint N, L, I;
    //N -- String length
    //L -- Limit 1s
    //I -- kth element
    fin >> N >> L >> I;
    
    uvvi dp( N+1, uvi( L+1, 0 ) );
    
    // 0 ; 1 for all 1 limits >= 1
    FORE( lim, 1, L )
        dp[ 1 ][ lim ] = 2;
        
    // For all lengths, if limit = 0, we have 1
    FORE( len, 0, N )
        dp[ len ][ 0 ] = 1;
    
    FORE( len, 2, N)
    {
        FORE( limit, 1, L )
        {
            dp[ len ][ limit ] = 
            dp[ len - 1][ limit ] + //if we add a 0 
            dp[ len - 1][limit -1]; //if we add a 1
            
            cout << "len -- " << len << " limit " << limit <<
            " = " << dp[ len ][ limit ] << endl;
        }
    }
    
    uint countLeft = I;
    uint oneLeft = L;
    for( uint len = N; len >= 2; --len )
    {
        uint chooseZeroCount = dp[ len - 1 ][ oneLeft ];
        cout << "len -- " << len << " One's left " << oneLeft << " Chooze zero count " << chooseZeroCount << endl;
        if ( chooseZeroCount < countLeft ) {
            //Must choose a 1
            fout << '1';
            oneLeft--;
            countLeft -= chooseZeroCount;
        } else {
            fout << '0';   
        }
        
        cout << "Countleft " << countLeft << endl;
    }
    
    //assert(countLeft <= 1);
    
    fout << (countLeft == 2 ? '1' : '0') << endl;
    
    return 0;
}

/*
ID: eric7231
PROG: ratios
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

struct node 
{
    uint i, j, k;
    
    node(uint aa, uint bb, uint cc) : i(aa), j(bb), k(cc) {}

};

bool operator<( const node& lhs, const node& rhs)
{
		if (lhs.i != rhs.i)
			return lhs.i < rhs.i;

		if (lhs.j != rhs.j)
			return lhs.j < rhs.j;

		return rhs.k < rhs.k;

}

int main() {
    
	ofstream fout ("ratios.out");
    ifstream fin ("ratios.in");

    uvvi weights(3, uvi(3));
    uvi goal(3);
    
    FOR(i, 0, 3)
        fin >> goal[i];
    
    FOR(ratio, 0, 3) FOR(i, 0, 3)
        fin >> weights[ratio][i] ;
 	
    
    
    
    queue<node> toVisit;
    uint limitRed = 200; 
    set<node> visited; 
    
    set< pair< uint, node > > solutions;
    
    uint a, b, c;
    uint aRed, bRed, cRed;
    
    FOR(i, 0, 100)
        FOR(j, 0, 100)
            FOR(k, 0, 100)
    {
        
        a = (i*weights[0][0] + j*weights[1][0] + k*weights[2][0]); 
        b = (i*weights[0][1] + j*weights[1][1] + k*weights[2][1]); 
        c = (i*weights[0][2] + j*weights[1][2] + k*weights[2][2]); 
        
        //cout << "abc: " << a << " " << b << " " << c << endl;
        //cout << "ijk: " << i << " " << j << " " << k << endl;
        
        uint minN = 10000000; 
        if (goal[0] > 0)
            minN = min( minN, a / goal[0]);
        if (goal[1] > 0)
            minN = min ( minN, b / goal[1] );
        if (goal[2] > 0)
            minN = min ( minN, c / goal[2] );
        
        aRed = a - minN * goal[0];
        bRed = b - minN * goal[1];
        cRed = c - minN * goal[2];
        
        //cout << aRed << " " << bRed << " " << cRed << endl;
                
        if ( aRed == 0 && bRed == 0 && cRed == 0 && (a+b+c) > 0 )
        {
            //fout << a << endl << b << endl << c << endl;
  
            solutions.insert( mp(minN, node(i,j,k) ) );
        }
        
    }
    
    if (solutions.empty()) 
    fout << "NONE" << endl;
else {
  pair< uint, node > solPair = *solutions.begin();
  node sol = solPair.second;
            fout << sol.i << " " << sol.j << " " << sol.k << " " <<  solPair.first << endl;   
}
    return 0;
}

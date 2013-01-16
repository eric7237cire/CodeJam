/*
ID: eric7231
PROG: fracdec
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
    
	ofstream fout ("fracdec.out");
    ifstream fin ("fracdec.in");
	
    int divisor, dividend;
    fin >> dividend >> divisor;
    
    uvi remToAns;
    map<uint, uint> remainders;
    map<uint, uint> remainderLeadingZeros;
    ostringstream ansSb;
        
    if (dividend >= divisor) {
       int intPart = dividend / divisor;
       dividend = dividend % divisor;
       ansSb << intPart << ".";
    } else {
        ansSb << "0.";
    }
                
    if (dividend == 0) {
       ansSb << 0;
       fout << ansSb.str() << endl;
       return 0;
    }
    
    //Loop invariant dividend < divisor
    while (true) 
    {
        dividend *= 10;
        uint zeroCount = 0;
        
        //If it is still too small, we keep going adding 0's to the ans as well
        while (dividend < divisor) {
            dividend *= 10;
            ++zeroCount;              
        }
            
        map<uint, uint>::iterator remIt = remainders.find(dividend) ;

        //We are done
        if (remIt != remainders.end()) 
        {
            uint initialRemainderLeadingZeros = remainderLeadingZeros[dividend];
            
            uint z = 0;
            while( z < zeroCount - initialRemainderLeadingZeros) 
            {
                ++z;
            //FOR(z, 0, zeroCount)
                ansSb << 0;
            }
            cout << dividend << endl;
            //Find where the cycle started
              int strIdx = remToAns[ remIt->second ];
              string ansStr = ansSb.str();
              ansStr.insert(strIdx, "(");
              ansStr.insert(ansStr.length(), ")");
              
              for(int lrIdx = 76; lrIdx < ansStr.length(); lrIdx += 77) {
                  ansStr.insert(lrIdx, "\n");
              }
              fout << ansStr << endl;
              return 0;
        }
        
        remainders.insert(mp(dividend, remToAns.size()));
        remainderLeadingZeros.insert(mp(dividend, zeroCount));
        remToAns.pb(ansSb.str().length());
        
            
        int d = dividend / divisor;
         
        FOR(z, 0, zeroCount)
            ansSb << 0;
        
        ansSb << d;
        
        int remainer = dividend - d * divisor;
        
        dividend = remainer;

        //Decimal terminates
        if (dividend == 0) {
            fout << ansSb.str() << endl;            
            return 0;
        }
    }
	fout << "Error" << endl;
	
    return 0;
}

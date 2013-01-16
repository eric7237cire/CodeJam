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
    
    int factor = 1;

    uvi remToAns;
    uvi remainders;
    ostringstream ansSb;
        
    if (dividend >= divisor) {
       int intPart = dividend / divisor;
       dividend = dividend % divisor;
       ansSb << intPart << ".";
    } else {
        ansSb << "0.";
    }
        
    int ansLen = 0;
        
    if (dividend == 0) {
       ansSb << 0;
       fout << ansSb.str() << endl;
       return 0;
    }
    
    cout << "Start " << ansSb.str() << endl;
        
    while (true) 
    {
       
        while (dividend < divisor) {
            factor *= 10;
            dividend *= 10;
            ansLen ++;
        }
            
        uvi::iterator remIt = find( all(remainders), dividend) ;
            // 0.803(571428)
        if (remIt != remainders.end()) 
        {
              int strIdx = remToAns[ distance(remainders.begin(), remIt) ];
              string ansStr = ansSb.str();
              ansStr.insert(strIdx, "(");
              ansStr.insert(ansStr.length(), ")");
              fout << ansStr << endl;
              return 0;
        }
            
        remToAns.pb(ansSb.str().length());
        remainders.pb(dividend);
            
        int d = dividend / divisor;
         
        cout << "D " << endl;
        cout << d << endl;
        ansSb << setfill('0') << setw(ansLen) << d;
        ansSb << resetiosflags ;
        cout << "Ans " << ansSb.str() << endl;
        ansLen = 0;
        
        int remainer = dividend - d * divisor;
        
        dividend = remainer;

        if (dividend == 0) {
           
            fout << ansSb.str() << endl;
            
            return 0;
        }
    }
	fout << "Error" << endl;
	
    return 0;
}

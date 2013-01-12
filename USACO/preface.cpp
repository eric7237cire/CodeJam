/*
ID: eric7231
PROG: preface
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


int main() {
    
	ofstream fout ("preface.out");
    ifstream fin ("preface.in");
	
    uint N;
    fin >> N;
    
    uint counts[7] = {0};
    
    FORE(n, 1, N)
    {
        uint nRem = n;
        
        int powTen = 1000;
        int countOffset = 6;
        
        while(powTen >= 1)
        {
            assert(countOffset >= 0 && countOffset < 7);
            uint digit = nRem / powTen;
            
            //cout << digit << endl;
            
            assert(digit >= 0 && digit < 10);
            
            if (digit >= 0 && digit <= 3)
            {
                counts[countOffset] += digit;
            }
            if (digit == 4) 
            {
                counts[countOffset] += 1;
                counts[countOffset+1] += 1;
            }
            if (digit >= 5 && digit <= 8)
            {
                counts[countOffset] += digit - 5;
                counts[countOffset+1] += 1;
            }
            if (digit == 9) {
                counts[countOffset] += 1;
                counts[countOffset+2] += 1;
            }
            
            //For next iteration
            nRem %= powTen;            
            powTen /= 10;
            countOffset -= 2;
        }
    }
    
    int maxIndex = 6;
    
    for(maxIndex = 6; maxIndex >= 0 &&
        counts[maxIndex] == 0; --maxIndex) ;

    string rn("IVXLCDM");
    FORE(idx, 0, maxIndex)
    {
        fout << rn[idx] << " " << counts[idx] << endl;
    }
    
	return 0;
}

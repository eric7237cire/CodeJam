/*
ID: eric7231
PROG: fact4
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
    
	ofstream fout ("fact4.out");
    ifstream fin ("fact4.in");
	
    uint N;
    
    fin >> N;
    
    ull rightDigit = 1;
    ull limit = 10000000000000000;
    FORE(n, 2, N)
    {
        rightDigit *= n;
        
        while( rightDigit % 10 == 0)
            rightDigit /= 10;
        
        rightDigit %= limit;
        //fout << "n " << n << " rd " << rightDigit << endl;
    }
    fout << rightDigit % 10 << endl;
    return 0;
}

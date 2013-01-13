/*
ID: eric7231
PROG: money
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
    
	ofstream fout ("money.out");
    ifstream fin ("money.in");
	
    uint coins, target;
    
    fin >> coins >> target;
    
    uvi coinVal(coins, 0);
    
    FOR(ci, 0, coins) 
        fin >> coinVal[ci];
    
    vector<unsigned long long> ways(target+1, 0);
    
    ways[0] = 1;
    for(int coinIndex = 0; coinIndex < coins; ++coinIndex) {
        int coin = coinVal[coinIndex];
        for(int j = coin; j <= target; ++j) {
            ways[j] += ways[j - coin];
        }
    }
    
    fout << ways[target] << endl;
	return 0;
}

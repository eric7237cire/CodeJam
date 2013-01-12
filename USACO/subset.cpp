/*
ID: eric7231
PROG: subset
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
    
	ofstream fout ("subset.out");
    ifstream fin ("subset.in");
	
    uint N;
    fin >> N;

    uint counts[800] = {0};
    
    //sum(index, target) = how many ways to make the sum 
    counts[0] = 0;
    counts[1] = 1;
    
    uint maxSum = 1;
    
    FORE(i, 2, N)
    {
        for(int sum = maxSum; sum >= 0; --sum)
        {
            if (counts[sum] > 0) {
                counts[sum+i] += counts[sum];    
            }
        }
        
        counts[i] ++;        
        maxSum += i;
    }
    
    uint targetSum = N * (N+1) / 2;
    
    if (targetSum % 2 == 1)
        fout << 0 << endl;
    else {
        assert(counts[targetSum / 2] % 2 == 0);
        fout << counts[targetSum / 2] / 2 << endl;
    }
    
    
    
	return 0;
}

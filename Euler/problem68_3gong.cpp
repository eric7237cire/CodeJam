
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
#include "prettyprint.h"
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



//No longer used too slow
bool reject(const vector<uint>& sol)
{
    uint usedDigits = 0;

    FOR(solIdx, 0, sol.size())
    {
        if (  (usedDigits & 1 << sol[solIdx] ) != 0 )
			return true;
		
		usedDigits |= 1 << sol[solIdx];
		
    }

	if (sol.size() >= 5) {
		uint checkRow1 = sol[0] + sol[1] + sol[2];
		uint checkRow2 = sol[1] + sol[3] + sol[4];

		if (checkRow1 != checkRow2)
			return true;
		
		//0 must be lowest external
		if (sol[0] > sol[4])
		    return true;
	}

	if (sol.size() >= 6) {
		uint checkRow1 = sol[3] + sol[2] + sol[5];
		uint checkRow2 = sol[1] + sol[3] + sol[4];

		if (checkRow1 != checkRow2)
			return true;
		
		if (sol[0] > sol[5])
		    return true;
	}

	return false;
}

/**
 * index 0 == row 1
 * index 1 == row 2
 */
void findSolutions(vector<uint>& partial, uint N,
				   vector<vector<uint> >& solutions, 
				   uint& solutionCount)
{
    if (reject(partial))
        return;
    
    if(partial.size() == N) {
		cout << "Solution found ! " << endl;
		cout << partial << endl;
		
		solutions.push_back(partial);
		
		++solutionCount;

		
        return;
    }
    
    FORE(num, 1, N)
    {		
        partial.push_back(num);
		findSolutions(partial, N, 
			solutions, solutionCount);
        partial.erase(partial.begin() + partial.size() - 1);
    }
}


int main() {
    

	vector<uint> partial;
    vector< vector<uint> > allSolutions;
    uint solutionCount = 0;
    
    findSolutions(partial, 6, allSolutions, solutionCount);
    
    uint maxSol = 0;
    
    for(auto solIt = allSolutions.begin();
        solIt != allSolutions.end();
        ++solIt)
    {
        const uvi& sol = *solIt;
        uint num = 0;
        num += sol[0];
        num *= 10;
        num += sol[1];
        num *= 10;
        num += sol[2];
        num *= 10;
        
        num += sol[5];
        num *= 10;
        num += sol[2];
        num *= 10;
        num += sol[3];
        num *= 10;
        
        num += sol[4];
        num *= 10;
        num += sol[3];
        num *= 10;
        num += sol[1];
        
        cout << num << endl;
    }
	
	return 0;
}

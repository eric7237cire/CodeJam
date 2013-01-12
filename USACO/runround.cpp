/*
ID: eric7231
PROG: runround
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



uint concatNums(uint left, uint right)
{
   // cout << "Concat " << left << " + "	<< right;
	//123 ; 456
	uint rightDigits = right;

	if (right == 0)
	    return 10 * left;
	
	while(rightDigits > 0)
	{
		rightDigits /= 10;
		left *= 10;
	}

	// cout << " = " << left+right << endl;
	
	return left+right;
}

uint vecToUint(const uvi& sol)
{
    uint num = 0;
    FOR(i, 0, sol.size())
    {
        num = concatNums(num, sol[i]);
    }
    
    return num;
}


//No longer used too slow
bool reject(const vector<uint>& sol)
{
    uint usedDigits = 0;

    FOR(solIdx, 0, sol.size())
    {
        if (sol[solIdx] == 0)
            continue;
        
        if (  (usedDigits & 1 << sol[solIdx] ) != 0 )
			return true;
		
		usedDigits |= 1 << sol[solIdx];		
    }

	

	return false;
}

/**
 * index 0 == row 1
 * index 1 == row 2
 */
void findSolutions(vector<uint>& partial,
                uint currentIndexToSet,
                uint numSet,
				   vector<vector<uint> >& solutions)
{
    //cout << vecToUint(partial) << endl;
    //cout << currentIndexToSet << endl;
    if (reject(partial))
        return;
    
    if( numSet == partial.size() ) {
		//cout << "Solution found ! " << endl;
		//cout << vecToUint(partial) << endl;
		
		solutions.push_back(partial);
        return;
    }
    
    assert(partial[currentIndexToSet] == 0);
    
    FORE(digit, 1, 9)
    {
        uint nextIndexToSet = (currentIndexToSet + digit) % partial.size();
        //cout << vecToUint(partial) << " cur " << currentIndexToSet << " digit "  << digit << " next " << nextIndexToSet << endl;
        if ( numSet < partial.size()-1 && partial[ nextIndexToSet ] != 0)
            continue;
        
        if ( numSet == partial.size()-1 && nextIndexToSet != 0 )
            continue;
        
        if (nextIndexToSet == currentIndexToSet)
            continue;
        
        partial[currentIndexToSet] = digit;
        //assert(partial[nextIndexToSet] == 0);
		findSolutions(partial, nextIndexToSet, numSet + 1,  
			solutions);
        partial[currentIndexToSet] = 0;
    }
}


int main() {
    
	ofstream fout ("runround.out");
    ifstream fin ("runround.in");
	
    uint N;
    fin >> N;

    set<uint> solutionSet;
    
    for(int digits = 1; digits <= 9; ++digits)
    {
        uvi partial(digits, 0);
        uvvi allSolutions;
                
        cout << "Solutions " << digits << endl;
        findSolutions(partial, 0, 0, allSolutions);
        cout << "Solutions count " << allSolutions.size() << endl;
        for(uvvi::const_iterator it = allSolutions.begin();
            it != allSolutions.end();
            ++it)
        {
            solutionSet.insert( vecToUint(*it) );   
        }
    }
    
    uint ans = *lower_bound( all(solutionSet), N+1 );
    fout << ans << endl;
	return 0;
}

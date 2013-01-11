/*
ID: eric7231
PROG: checker
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

bool reject(const vector<uint>& sol)
{
    uint usedColumns = 0;
    
    for(uint row = 0; row < sol.size(); ++row)
    {
        uint col = sol[row];
        
        if (  (usedColumns & 1 << col ) != 0 )
            return false;
        
        usedColumns |= 1 << col;
        
        //Check diagonals
        for(uint lowerRow = 1; lowerRow < sol.size(); ++lowerRow)
        {
            int noColA = col + (lowerRow - row);
            int noColB = col - (lowerRow - row);
            
            if (sol[lowerRow] == noColA ||
                sol[lowerRow] == noColB)
                return false;
        }
    }
}

/**
 * index 0 == row 1
 * index 1 == row 2
 */
void findSolutions(vector<uint>& partial, uint N, vector<vector<uint> > solutions)
{
    if(reject(partial)) {
        cout << "Reject " << endl;
        for(uint solIdx = 0; solIdx < partial.size(); ++solIdx)
        {
            if (solIdx > 0)
                fout << " ";
            fout << sol[solIdx];            
        }
        
        return;
    }
    
    if(partial.size() == N) {
        solutions.push_back(partial);
        return;
    }
    
    //for next val
    for(uint col = 0; col < N; ++col)
    {
        partial.push_back(col);
        findSolutions(partial, N, solutions);
        partial.erase(partial.begin() + partial.size() - 1);
    }
}


int main() {
    
	ofstream fout ("checker.out");
    ifstream fin ("checker.in");
	
    uint N;
    fin >> N;
    
    vector<uint> partial;
    vector< vector<uint> > allSolutions;
    
    findSolutions(partial, N, allSolutions);
    
    for(uint solNum = 0; solNum < allSolutions.size(); ++solNum)
    {
        const vector<uint>& sol = allSolutions[solNum];
        
        for(uint solIdx = 0; solIdx < sol.size(); ++solIdx)
        {
            if (solIdx > 0)
                fout << " ";
            fout << sol[solIdx];            
        }
        fout << endl;
	        
	}
	
	return 0;
}

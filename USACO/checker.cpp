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
typedef unsigned long long ull;

//No longer used too slow
bool reject(const vector<uint>& sol)
{
    uint usedColumns = 0;
    
    for(uint row = 0; row < sol.size(); ++row)
    {
        uint col = sol[row];
        
        assert (  (usedColumns & 1 << col ) == 0 );

		usedColumns |= 1 << col;
        
        //Check diagonals
        for(uint lowerRow = row + 1; lowerRow < sol.size(); ++lowerRow)
        {
            int noColA = col + (lowerRow - row);
            int noColB = col - (lowerRow - row);
            
            if (sol[lowerRow] == noColA ||
                sol[lowerRow] == noColB)
                return true;
        }
    }

	return false;
}

/**
 * index 0 == row 1
 * index 1 == row 2
 */
void findSolutions(vector<uint>& partial, uint N,
				   const vector<vector<pair<uint, uint> > >&rowColDiag,
				   const ull usedDiags,
				   const uint usedColumns,
				   vector<vector<uint> >& solutions, 
				   uint& solutionCount)
{
    
    if(partial.size() == N) {
		//cout << "Solution found ! " << endl;
		if (solutions.size() < 3)
		    solutions.push_back(partial);
		
		++solutionCount;

		//Count 2x if we have symetry
		if (N > 6 && partial[0] < N/2) solutionCount++;

        return;
    }
    
	const uint row = partial.size();



    //for next val
    for(uint col = 0; col < N; ++col)
    {
		if (  (usedColumns & 1 << col ) != 0 )
			continue;

		if ( (usedDiags & 1ull << (ull)rowColDiag[row][col].first) != 0 )
			continue;

		if ( (usedDiags & 1ull << (ull)rowColDiag[row][col].second) != 0 )
			continue;

		//Take advantage of symetry, only consider first 4 in the row
		if (N > 6 && col >= (N+1) / 2 && row == 0)
			continue;

		ull nextUsedDiags = usedDiags;
		nextUsedDiags |= 1ull << (ull)rowColDiag[row][col].first;
		nextUsedDiags |= 1ull << (ull)rowColDiag[row][col].second;

        partial.push_back(col);
		findSolutions(partial, N, rowColDiag, nextUsedDiags,
			usedColumns | 1 << col, solutions, solutionCount);
        partial.erase(partial.begin() + partial.size() - 1);
    }
}


void updateRowColDiag(vector<vector<pair<uint, uint> > >& rowColDiag,
					  int startRow, int startCol, 
					  int deltaRow, int deltaCol, 
					  uint N, uint diagIndex, bool isFirstDiag)
{
	int row = startRow;
	int col = startCol;

	while(row >= 0 && row < (int)N && col >= 0 && col < (int)N)
	{
		if (isFirstDiag)
			rowColDiag[row][col].first = diagIndex;
		else
			rowColDiag[row][col].second = diagIndex;
		row += deltaRow;
		col += deltaCol;
	}
}
int main() {
    
	ofstream fout ("checker.out");
    ifstream fin ("checker.in");
	
    uint N;
    fin >> N;
    
	vector<vector<pair<uint, uint> > > rowColDiag(N, vector<pair<uint, uint> >(N) );

	uint diagIndex = 0;

	//start with diag that goes bottom left to top right
	//starting in column 0
	for(uint startRow = 0; startRow < N; ++startRow, ++diagIndex)
	{
		updateRowColDiag(rowColDiag, startRow, 0, -1, 1, N, diagIndex, true);
	}

	//Now go along bottom row, skipping long diag which is already done
	for(uint startCol = 1; startCol < N; ++startCol, ++diagIndex)
	{
		updateRowColDiag(rowColDiag, N-1, startCol, -1, 1, N, diagIndex, true);
	}

	//Go starting at top row, going down and right
	for(uint startCol = 0; startCol < N; ++startCol, ++diagIndex)
	{
		updateRowColDiag(rowColDiag, 0, startCol, 1, 1, N, diagIndex, false);	
	}

	//Starting in first column, goint down and right
	for(uint startRow = 1; startRow < N; ++startRow, ++diagIndex)
	{
		updateRowColDiag(rowColDiag, startRow, 0, 1, 1, N, diagIndex, false);
	}

	cout << "Last diag index " << diagIndex << endl;

	vector<uint> partial;
    vector< vector<uint> > allSolutions;
    uint solutionCount = 0;
    
    findSolutions(partial, N, rowColDiag, 0, 0, allSolutions, solutionCount);
    
    for(uint solNum = 0; solNum < min(allSolutions.size(), 3u); ++solNum)
    {
        const vector<uint>& sol = allSolutions[solNum];
        
        for(uint solIdx = 0; solIdx < sol.size(); ++solIdx)
        {
            if (solIdx > 0)
                fout << " ";
            fout << sol[solIdx]+1;            
        }
        fout << endl;
	        
	}

	fout << solutionCount << endl;
	
	return 0;
}

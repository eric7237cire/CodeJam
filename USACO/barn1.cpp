/*
ID: eric7231
PROG: barn1
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
using namespace std;

typedef unsigned int uint;

int main() {
	ofstream fout ("barn1.out");
    ifstream fin ("barn1.in");
		
	uint M, S, C;
	fin >> M >> S >> C;

	if (C == 0) 
	{
		fout << 0;
		return 0;
	}


	vector<uint> occupStalls;

	uint stall;

	for(uint c = 0; c < C; ++c)
	{
		fin >> stall;
		occupStalls.push_back(stall);
	}

	sort(occupStalls.begin(), occupStalls.end());
	
	vector<uint> gaps;

	for(vector<uint>::const_iterator it = occupStalls.begin() + 1; 
		it != occupStalls.end(); ++it)
		gaps.push_back(*it - *(it-1));
	
	uint lengthNeeded = *occupStalls.rbegin() - *occupStalls.begin() + 1;
	--M;

	sort(gaps.rbegin(), gaps.rend());

	vector<uint>::const_iterator gapIt = gaps.begin();

	while(M > 0 && gapIt != gaps.end())
	{
		lengthNeeded -= *gapIt - 1;
		++gapIt;
		--M;
	}
	
	fout << lengthNeeded << endl;

    return 0;
}

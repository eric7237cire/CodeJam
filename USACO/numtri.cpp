/*
ID: eric7231
PROG: numtri
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




int main() {
    
	ofstream fout ("numtri.out");
    ifstream fin ("numtri.in");
	
    uint maxRow = 0;
    fin >> maxRow;
    
	uint nodeNum = 1;
        
    vector<uint> maximumPath;
    
    uint elem;
    fin >> elem;
    
    maximumPath.push_back(elem);
    
    uint globalMaxVal = 0;
    
    for(uint r = 2; r <= maxRow; ++r) {
        for(uint c = 1; c <= r; ++c) {
            ++nodeNum;
            
            fin >> elem; 
            uint val = elem;
            
            uint maxValue = 0;
            //subtract length of previous row
            if (c > 1) {
                maxValue = max(maxValue, val+maximumPath[ nodeNum-r-1]);
            }
            if (c < r) {
                maxValue = max(maxValue, val+maximumPath[ nodeNum-r ]);
            }
            maximumPath.push_back(maxValue);
            globalMaxVal = max(globalMaxVal,maxValue);
        }
    }

	fout << globalMaxVal << endl;
	
	
	return 0;
}

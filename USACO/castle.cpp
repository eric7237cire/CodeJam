/*
ID: eric7231
PROG: castle
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

uint toIndex(uint row, uint col, uint M)
{
    return row * M + col;
}

int main() {
    
	ofstream fout ("castle.out");
    ifstream fin ("castle.in");
	
    uint M, N;
    
    fin >> M >> N;
    vector< vector< uint > > roomConnections(M*N);
    
    //vector< uint > nodes(M*N);
    
    for(uint row = 0; row < N; ++row)
    {
        for(uint col = 0; col < M; ++col)
        {
            uint rooms;
            fin >> rooms;
            
            if (~rooms & 8) 
                roomConnections[ toIndex(row,col,M) ].push_back( toIndex(row+1,col,M) );
            
            if (~rooms & 4) 
                roomConnections[ toIndex(row,col,M) ].push_back( toIndex(row,col+1,M) );
            
            if (~rooms & 2) 
                roomConnections[ toIndex(row,col,M) ].push_back( toIndex(row-1,col,M) );
            
            if (~rooms & 1) 
                roomConnections[ toIndex(row,col,M) ].push_back( toIndex(row,col-1,M) );
      
        }
    }
    
    vector<int> componentNum(M*N, -1);
    uint num_components = 0;
    
    vector<uint> componentSize;
    
    for(uint node = 0; node < M*N; ++node)
    {
        if (componentNum[node] >= 0)
            continue;
        
        deque<uint> toVisit;
        toVisit.push_back(node);
        
        uint curComponent = num_components++;

		uint count  = 0;
        
        while(!toVisit.empty())
        {
            uint current = toVisit.front();
            
            toVisit.pop_front();
            
            assert(componentNum[current] == -1 || componentNum[current] == curComponent);
            
            if (componentNum[current] != -1)
                continue;

			++count;
            
            componentNum[current] = curComponent;
            
            toVisit.insert(toVisit.end(), roomConnections[current].begin(), roomConnections[current].end());
        }

		componentSize.push_back(count);
    }
    
	uint maxCombined = 0;
	uint maxRow;
	uint maxCol;
	char maxDir;

	for(uint col = 0; col < M; ++col)
    {
		for(int row = N-1; row >= 0; --row)
		{
        
			uint node = toIndex(row,col,M);
			uint nodeCompNum = componentNum[node];

			if (row > 0) 
			{
				uint northNode = toIndex(row-1, col, M);
				uint northCompNum = componentNum[northNode];
				if (componentNum[node] != componentNum[northNode] && 
					maxCombined < componentSize[nodeCompNum] + componentSize[northCompNum]) 
				{
					maxCombined = componentSize[nodeCompNum] + componentSize[northCompNum];
					maxRow = row+1;
					maxCol = col+1;
					maxDir = 'N';
				}
			}

			if (col < M - 1) 
			{
				uint eastNode = toIndex(row, col+1, M);
				uint eastCompNum = componentNum[eastNode];
				if (componentNum[node] != componentNum[eastNode] && 
					maxCombined < componentSize[nodeCompNum] + componentSize[eastCompNum]) 
				{
					maxCombined = componentSize[nodeCompNum] + componentSize[eastCompNum];
					maxRow = row+1;
					maxCol = col+1;
					maxDir = 'E';
				}
			}
            //cout << "Row " << row+1 << " Col " << col + 1 << " = com " << componentNum[toIndex(row,col,M)] << endl;
        }
    }
    
	fout << num_components << endl;
	fout << *max_element(componentSize.begin(), componentSize.end()) << endl;
	fout << maxCombined << endl;
	fout << maxRow << " " << maxCol << " " << maxDir << endl;
	return 0;
}

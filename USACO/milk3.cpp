/*
ID: eric7231
PROG: milk3
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

class node {
public:
    uint jugs[3];
	
	node(uint _a, uint _b, uint _c)  
	{
	    jugs[0] = _a;
	    jugs[1] = _b;
	    jugs[2] = _c;
	}
	
	node(uint (&_jugs)[3])  
	{
	    for(int i = 0; i < 3; ++i)
	        jugs[i] = _jugs[i];
	    
	}
};



int main() {
    
	ofstream fout ("milk3.out");
    ifstream fin ("milk3.in");
	
	uint jugCap[3];
		
	fin >> jugCap[0] >> jugCap[1] >> jugCap[2];
	
	queue<node> toVisit;
	
	const int MAX_CAPACITY = 21;
	
	toVisit.push( node(0, 0, jugCap[2]) );
	
	bool visited[MAX_CAPACITY][MAX_CAPACITY][MAX_CAPACITY];
	
	for(int i = 0; i < MAX_CAPACITY; ++i)
	    for(int j = 0; j < MAX_CAPACITY; ++j)
	        for(int k = 0; k < MAX_CAPACITY; ++k)
    {
        visited[i][j][k] = false;   
    }
	
	while(!toVisit.empty() )
	{
	    node top = toVisit.front();
		
		toVisit.pop();
		
		if (visited[ top.jugs[0] ][ top.jugs[1] ][ top.jugs[2] ])
		    continue;
		
		visited[ top.jugs[0] ][ top.jugs[1] ][ top.jugs[2] ] = true;
		//a to b, c
		//b to a,c
		//c to a, b
		
		for(int fromJug = 0; fromJug < 3; ++fromJug)
		{
		    for(int toJug = 0; toJug < 3; ++toJug)
		    {
		        if (toJug == fromJug)
		            continue;
		        
		        if (top.jugs[fromJug] <= 0)
		            continue;
		        
		        if (top.jugs[toJug] >= jugCap[toJug])
		            continue;
		        
		        uint diff = min(top.jugs[fromJug], jugCap[toJug] - top.jugs[toJug]);
		        assert(diff > 0);
		        node newNode(top.jugs);
		        newNode.jugs[fromJug] -= diff;
		        newNode.jugs[toJug] += diff;
		        toVisit.push( newNode );
		    }
		}
	}
		     
	/*
	A single line with a sorted list of all the possible amounts of milk that can be in bucket C when bucket A is empty.
	*/
	
	set<uint> ans;
	
	
    for(int j = 0; j < MAX_CAPACITY; ++j)
        for(int k = 0; k < MAX_CAPACITY; ++k)
    {
        if (visited[0][j][k]) 
            ans.insert(k);
    }
    
	for(set<uint>::iterator it = ans.begin(); it != ans.end(); ++it)
	{
	    if (it != ans.begin())
	        fout << " ";
	    
	    fout << *it;
	}
	
	fout << endl;
	
	
	return 0;
}

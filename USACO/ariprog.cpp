/*
ID: eric7231
PROG: ariprog
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


/*
Line 1:	N (3 <= N <= 25), the length of progressions for which to search
Line 2:	M (1 <= M <= 250), an upper bound to limit the search to the bisquares with 0 <= p,q <= M
*/
	
int main() {
    
	ofstream fout ("ariprog.out");
    ifstream fin ("ariprog.in");
	
	uint N;
	uint M;
	
	const uint MAX = 125000;
	
	fin >> N >> M;
	
	int bisqSet[MAX+1];
	
	for(uint i = 0; i <= MAX; ++i)
	    bisqSet[i] = -1;
	
	uint maxNum = 0;
	
	for(uint i = 0; i <= M; ++i) 
	{
	    for(uint j = 0; j <= M; ++j)
	    {
	        bisqSet[i*i + j*j] = i*i + j*j;
	        maxNum = max(i*i+j*j, maxNum);
	    }
	}
	
	
	vector<pair<uint, uint> > seqFound;
	
	for(uint i = 0; i <= MAX; ++i)
	{
	    if (bisqSet[i] == -1)
	        continue;
	    
	    //cout << "Checking i " << i << endl;
	    for(uint j = i+1; j <= MAX; ++j)
	    {
	        if (bisqSet[j] == -1)
	            continue;
	        uint a = i;
	        uint ab = j;
	        
	        uint b = ab - a;
	        
	        uint anb = a + (N-1) * b;
	        	            
	        //a, a +b, a+2b
	        //a + (seqLen - 1)diff must be <= largest element
	        if ( anb > maxNum )
	            break;
	        
	        bool ok = true;
	        
	        for(uint seq = 1; seq < N; ++seq) 
	        {
	            
	            if (bisqSet[a + seq * b] == -1) 
	            {
	                ok = false;
	                break;
	            }
	        }
	        
	        if (ok) {
	            //cout << "Found seq " << a << " " << b << endl;
	            seqFound.push_back(make_pair(b, a));
	        }
	    }
	}
	
	//cout << bisqSet.size() << endl;
	
	sort(seqFound.begin(), seqFound.end());
	
	for(uint sf = 0; sf < seqFound.size(); ++sf)
	{
	    fout << seqFound[sf].second << " " << seqFound[sf].first << endl;
	}
	
	if (seqFound.size() == 0)
	    fout << "NONE" << endl;
	
	cout << seqFound.size() << endl;
	
	return 0;
}

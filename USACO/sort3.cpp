/*
ID: eric7231
PROG: sort3
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

uint gcd(uint n1, uint n2)
{
    uint a = max(n1,n2);
    uint b = min(n1,n2);

	if (b == 0)
		return 1;
    
    uint pr = b; //15
    uint r = a % b; //36 % 15 = 6
    
    while(r != 0)
    {
        uint pr_temp = r; // = 6
        r = pr % r; //15 % 6 = 3
        pr = pr_temp; // pr = 6
    }
    
    return pr;
}

typedef pair<uint, uint> fraction;

struct fracCompare {
    bool operator() (const fraction& lhs, const fraction& rhs)
    {
        return (double) lhs.first / lhs.second < (double) rhs.first / rhs.second;
    }
};

typedef set<fraction, fracCompare> fracSet;

int main() {
    
	ofstream fout ("sort3.out");
    ifstream fin ("sort3.in");
	
    uint N;
    fin >> N;
    
    vector<uint> records;
    
    for(uint r = 0; r < N; ++r) 
	{
		uint rec;
		fin >> rec;
		
		records.push_back(rec);
	}
	
	vector<uint> sorted = records;
	sort(sorted.begin(), sorted.end());
	
	uint startOf2 = lower_bound(sorted.begin(), sorted.end(), 2) - sorted.begin();	
	
	uint nEx = 0;
	for(int i = 0; i < records.size(); ++i)
	{
		if (records[i] == sorted[i])
			continue;
			
		bool startAtBack = records[i] == 3;
		assert( records[i] == 3 || startOf2 > i);
		uint searchStart = startAtBack ? records.size() - 1 : startOf2;
		uint delta =  startAtBack ? -1 : 1;
		for(int j = searchStart; j > i; j += delta)
		{
			if (records[j] == sorted[i]) 
			{
				//cout << "Swapping index " << j << " with index " << i << endl;
				swap(records[i], records[j]);
				++nEx;
				break;
			}
		}
	}

	cout << nEx << endl;
	fout << nEx << endl;
    
	return 0;
}

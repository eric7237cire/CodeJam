/*
ID: eric7231
PROG: milk
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
	ofstream fout ("milk.out");
    ifstream fin ("milk.in");
		
	uint N;
	uint M;
    fin >> N;
	fin >> M;

	vector<pair<uint,uint> > priceProd(M);
	
	for(uint m = 0; m < M; ++m)
		fin >> priceProd[m].first >> priceProd[m].second;
	
	sort(priceProd.begin(), priceProd.end());

	vector<pair<uint,uint> >::iterator it = priceProd.begin();

	uint cost = 0;

	while(N > 0 && it != priceProd.end())
	{
		if (it->second == 0) {
			++it;
			continue;
		}

		uint amt = min(N, it->second);
		cost += amt * it->first;

		it->second -= amt;
		N-=amt;
	}

	fout << cost << endl;

    return 0;
}

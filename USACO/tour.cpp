/*
ID: eric7231
PROG: tour
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

int N;
int E;

const int MAX_N = 100;
bool conn[MAX_N][MAX_N];

typedef map<string, int> mapsi;

mapsi nameIdxMap;

string s;

int main() {
	ofstream fout ("tour.out");
    ifstream fin ("tour.in");
		
	fin >> N;
	fin >> E;

	for(int i = 0; i < N; ++i)
	{
		fin >> s;
		nameIdxMap[s] = i;
	}
	
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

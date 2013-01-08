/*
ID: eric7231
PROG: packrec
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
using namespace std;

typedef unsigned int uint;

template<class A, class B> istream& operator>>
	(istream& in, pair<A, B>& val) { return in >> val.first >> val.second; } 


void config1(const vector<pair<uint, uint> >& rects, uint& minDim, uint& maxDim)
{
	uint width = 0;
	uint height = 0;

	for(int r = 0; r < 4; ++r)
	{
		width += rects[r].first;
		height = max(height, rects[r].second);
	}

	minDim = min(width, height);
	maxDim = max(width, height);
}

void config2(const vector<pair<uint, uint> >& rects, const int bottomRect, uint& minDim, uint& maxDim)
{
	uint width3 = 0;
	uint height3 = 0;
	uint heightBot;
	uint widthBot;
	for(int r = 0; r < 4; ++r)
	{
		if (r == bottomRect) {
			heightBot = rects[r].second;
			widthBot = rects[r].first;
		} else {
			width3 += rects[r].first;
			height3 = max(height3, rects[r].second);
		}
	} 

	uint height = heightBot+height3;
	uint width = max(width3, widthBot);

	
	minDim = min(width, height);
	maxDim = max(width, height);
}

void config3(const vector<pair<uint, uint> >& rects, const int botLeft, const int right, uint& minDim, uint& maxDim)
{
	uint width2 = 0;
	uint height2 = 0;
	for(int r = 0; r < 4; ++r)
	{
		if (r != botLeft && r != right) {
			width2 += rects[r].first;
			height2 = max(height2, rects[r].second);
		} 
	}


	uint height = max(rects[botLeft].second + height2, rects[right].second);
	uint width = max(width2+rects[right].first, rects[right].first+rects[botLeft].first);
		
	minDim = min(width, height);
	maxDim = max(width, height);
}

void config4(const vector<pair<uint, uint> >& rects, const int top, const int bot, uint& minDim, uint& maxDim)
{
	uint width2 = 0;
	uint height2 = 0;
	for(int r = 0; r < 4; ++r)
	{
		if (r != bot && r != top) {
			width2 += rects[r].first;
			height2 = max(height2, rects[r].second);
		} 
	}


	uint height = max(rects[bot].second + rects[top].second, height2);
	uint width = width2 + max(rects[bot].first, rects[top].first);
		
	minDim = min(width, height);
	maxDim = max(width, height);
}

void config6(const vector<pair<uint, uint> >& rects, const int topLeft, const int botLeft, 
			 const int topRight, const int botRight, uint& minDim, uint& maxDim)
{
	if (rects[botLeft].second > rects[botRight].second)
		return;

	uint leftHeight = rects[botLeft].second + rects[topLeft].second;

	uint width = rects[botLeft].first + rects[botRight].first;

	if (leftHeight <= rects[botRight].second) {
		//is not next to topRight
		width = max(width, rects[topLeft].first + rects[botRight].first);
		width = max(width, rects[topRight].first);
	} else {
		width = max(width, rects[topLeft].first + max(rects[botRight].first, rects[topRight].first));
	}

	uint height = max(rects[topLeft].second + rects[botLeft].second,
		rects[topRight].second + rects[botRight].second);

	
	minDim = min(width, height);
	maxDim = max(width, height);

	int b;
}

void updateSolutions(uint minDim, uint maxDim, uint& minArea, set<pair<uint, uint> >& solutions)
{
	
	if (minDim * maxDim  < minArea) {
		minArea = minDim * maxDim;
		solutions.clear();
	}

	if (minDim * maxDim == minArea) {
		solutions.insert(make_pair(minDim, maxDim));
	}
}

int main() {
	ofstream fout ("packrec.out");
    ifstream fin ("packrec.in");
	
	
	vector<pair<uint, uint> > rects(4);

	for(int i = 0; i < 4; ++i)
		fin >> rects[i];

	uint minArea = 9000000;
	set<pair<uint, uint> > solutions;

	for(uint rotatePerm = 0; rotatePerm < 16; ++rotatePerm)
	{
		vector<pair<uint, uint> > rotRect(rects);
		for(int r = 0; r < 4; ++r)
		{
			if ( (rotatePerm & (1 << r)) != 0 ) {
				rotRect[r].first = rects[r].second;
				rotRect[r].second = rects[r].first;
			}
			//cout << "Rot perm " << rotatePerm <<  "  " << r << " " << rotRect[r].first << " " 
				//<< rotRect[r].second << endl;
		}

		uint minDim, maxDim;

		config1(rotRect, minDim, maxDim);
		updateSolutions(minDim, maxDim, minArea, solutions);

		for(int br = 0; br < 4; ++br)
		{
			config2(rotRect, br, minDim, maxDim);
			updateSolutions(minDim, maxDim, minArea, solutions);
		}

		for(int bl = 0; bl < 4; ++bl) 
		{
			for(int right = 0; right < 4; ++right)
			{
				if (right == bl)
					continue;

				config3(rotRect,bl,right,minDim,maxDim);
				updateSolutions(minDim, maxDim, minArea, solutions);

				config4(rotRect, bl, right, minDim, maxDim);
				updateSolutions(minDim, maxDim, minArea, solutions);
			}
		}

		int perms[] = {0, 1, 2, 3};

		do {
			config6(rotRect, perms[0], perms[1], perms[2], perms[3], minDim, maxDim);
			updateSolutions(minDim, maxDim, minArea, solutions);
		} while(next_permutation(perms, perms+4));

	}
		
	fout << minArea << endl;
	for(set<pair<uint, uint> >::const_iterator it = solutions.begin(); it != solutions.end(); ++it)
	{
		fout << it->first << " " << it->second << endl;
	}

	return 0;
}

/*
ID: eric7231
PROG: clocks
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


void changeNum(uint& num) 
{
	num += 3;
	if (num == 15)
		num = 3;
}

class node {
public:
	vector<uint> clock;
	uint steps;
	vector<uint> moves;

	node() : steps(0) 
	{

	}

	node(const node& rhs) : clock(rhs.clock), steps(rhs.steps), moves(rhs.moves)
	{

	}

	node& change(uint index) 
	{
		changeNum(clock[index]);
		return *this;
	}

	uint getIndex() 
	{
		uint ret = 0;

		uint pow4 = 1;
		for(uint c = 0; c < 9; ++c)
		{
			ret += pow4 * clock[c];
			pow4 *= 4;
		}
		return ret;
	}

};


int main() {
	ofstream fout ("clocks.out");
    ifstream fin ("clocks.in");
	
	vector<uint> clockStart(9);
	vector<uint> done(9);

	for(uint i = 0; i < 9; ++i)
	{
		fin >> clockStart[i];
		done[i] = 12;
	}
	
	queue<node> toVisit;

	node start;
	start.clock = clockStart;

	toVisit.push(start);

	set<uint> visited;
	
	int iter = 0;

	while(!toVisit.empty() && iter <= 500000)
	{
		node top = toVisit.front();
		++iter;
		toVisit.pop();

		const uint index = top.getIndex();
		if (visited.find(index) != visited.end()) {
			continue;
		}

		visited.insert(index);
		/*
		cout << "Steps " << top.steps << " iter " << iter << endl;
		for(vector<uint>::const_iterator it = top.clock.begin();
				it != top.clock.end();
				++it)
			{
				cout << *it << " ";
			}
			cout << endl;
			*/
		if (top.clock == done)
		{
			for(vector<uint>::const_iterator it = top.moves.begin();
				it != top.moves.end();
				++it)
			{
				fout << *it;
				if (top.moves.end() - it > 1)
					fout << " ";
			}
			fout << endl;
			break;
		}

		vector<node> moves; 


		node move1(top);
		move1.change(0).change(1).change(3).change(4);
		moves.push_back(move1);

		node move2(top);
		move2.change(0).change(1).change(2);
		moves.push_back(move2);

		//ABCD EFG HI
	    //0123 456 78

		node move3(top);
		move3.change(1).change(2).change(4).change(5);
		moves.push_back(move3);

		node move4(top);
		move4.change(0).change(3).change(6);
		moves.push_back(move4);

		node move5(top);
		move5.change(1).change(3).change(4).change(5).change(7);
		moves.push_back(move5);

		node move6(top);
		move6.change(2).change(5).change(8);
		moves.push_back(move6);

		node move7(top);
		move7.change(3).change(4).change(6).change(7);
		moves.push_back(move7);

		node move8(top);
		move8.change(6).change(7).change(8);
		moves.push_back(move8);

		node move9(top);
		move9.change(4).change(5).change(7).change(8);
		moves.push_back(move9);

		for(uint m = 0; m < 9; ++m)
		{
			moves[m].moves.push_back(m+1);
			moves[m].steps = top.steps + 1;
			toVisit.push(moves[m]);
		}

	}

	return 0;
}

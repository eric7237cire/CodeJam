/*
ID: eric7231
PROG: msquare
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
#include <iomanip>
#include <iterator>
#include <sstream>
#include <bitset>
#include <cctype>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(uint k=(a); k < (b); ++k)

#define pb push_back 
#define mp make_pair 

typedef vector<int> vi; 
typedef vector<double> vd;
typedef vector<vi> vvi;
typedef vector<uint> uvi; 
typedef vector<uvi> uvvi;
typedef vector<vd> vvd;
typedef pair<int,int> ii;
typedef pair<uint,uint> uu;
#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 

struct node 
{
	uvi state;
	string moves;	
};

struct NodeCompByState
{
	bool operator()( const node& lhs, const node& rhs)
	{
		return lhs.state < rhs.state;
	}
};

struct NodeCompByDistance
{
	bool operator()( const node& lhs, const node& rhs)
	{
		if (lhs.moves.length() != rhs.moves.length())
			return lhs.moves.length() < rhs.moves.length();

		if (lhs.moves != rhs.moves)
			return lhs.moves < rhs.moves;


		return lhs.state < rhs.state;
	}
};

int main() {

	ofstream fout ("msquare.out");
	ifstream fin ("msquare.in");

	uvi target(8, -1);

	FOR(i, 0, 8)
		fin >> target[i];

	set<node, NodeCompByDistance> toVisit;   
	set<node, NodeCompByState> visited; 

	node start;

	FORE(i, 1,8) start.state.pb( i );

	toVisit.insert(start);

	while(!toVisit.empty())
	{
		set<node>::iterator it = toVisit.begin();
		node top = *it;
		toVisit.erase(it);

		if ( contains(visited, top) )
			continue;

		if (top.state == target)
		{
		    for(int lrIdx = 61; lrIdx < top.moves.length(); lrIdx += 62) {
		        top.moves.insert(lrIdx, "\n");
            }
              
			fout << top.moves.length() << endl;
			fout << top.moves << endl;
			return 0;
		}

		visited.insert(top);
		node topBotSwitch(top);

		//0 1 2 3
		//7 6 5 4
		FORE(i, 0, 3)
			topBotSwitch.state[i] = top.state[7 - i];
		FORE(i, 4, 7)
			topBotSwitch.state[i] = top.state[i - 2*(i-4) - 1];
		
		topBotSwitch.moves.insert( topBotSwitch.moves.end(), 'A');
		toVisit.insert(topBotSwitch);

		node rightShift(top);
		//0 1 2 3  ==>   3 0 1 2  .    (4+i-1)%4  0:3  1:0 2:1 3:2 
		//7 6 5 4  ==>   4 7 6 5         7->4 6->7 5->6 4->5

		FORE(i, 0, 3)
			rightShift.state[i] = top.state[ (4 + i - 1) % 4];
		FORE(i, 4, 6)
			rightShift.state[i] = top.state[i + 1];
		rightShift.state[7] = top.state[4];

		rightShift.moves.insert( rightShift.moves.end(), 'B');

		toVisit.insert(rightShift);

		node middleShift(top);

		//0 1 2 3  ==>   0 6 1 3  
		//7 6 5 4  ==>   7 5 2 4
		middleShift.state[1] = top.state[6];
		middleShift.state[2] = top.state[1];
		middleShift.state[5] = top.state[2];
		middleShift.state[6] = top.state[5];

		middleShift.moves.insert( middleShift.moves.end(), 'C' );

		toVisit.insert(middleShift);
	}
            
    return 0;
}

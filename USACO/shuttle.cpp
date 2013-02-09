/*
ID: eric7231
PROG: shuttle
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
#include <iomanip>
#include <sstream>
#include <bitset>
#include <limits>
#include <cctype>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(int k=(a); k <= (int)(b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int)(b); ++k)

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

typedef set<uint> si;
typedef vector<set<uint> > vs;
typedef vector<vs> vvs;


#define sz(a) int((a).size()) 
#define pb push_back 
#define all(c) (c).begin(),(c).end() 
#define FOR_IT(c,i) for(typeof((c).begin() i = (c).begin(); i != (c).end(); i++) 
#define contains(c,x) ((c).find(x) != (c).end()) 
#define cpresent(c,x) (find(all(c),x) != (c).end()) 
#define SZ(x) ((int) (x).size())

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const V& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}


const int notConnected = numeric_limits<int>::max();


template<typename T>
ostream& operator<<( ostream& os, const vector<T>& vec )
{
    FOR(i, 0, vec.size())
    {
        os <<  vec[i] << endl;
    }
    return os;
}

class Node 
{
    //int hole;
    //int positions;
public:
    string board;
    //Node* prevNode;

	int move;
	int numMoves;

	int space;

	vector<int> moves;
    
    Node(string b= "") : board(b) {}

    
    ii toPair() const {
        int white = 0;
        int black = 0;
        for(int c = 0; c < board.length(); ++c)
        {
            if (board[c] == 'W')
                white |= 1 << c;
            if (board[c] == 'B')
                black |= 1 << c;
        }
        return mp(white,black);
    }
    
	static Node* createNode(Node* node, int move)
	{
		Node* newNode = new Node();
	    newNode->board = node->board;
		//newNode->prevNode = node;
		newNode->numMoves = node->numMoves + 1;
		newNode->moves.insert(newNode->moves.begin(), all(node->moves));
		newNode->moves.pb(move+1);
		newNode->move = move+1;
		newNode->space = move;
		swap( newNode->board[node->space], newNode->board[move] );

		assert(newNode->board[newNode->space] == ' ');

		return newNode;
	}
};

class NodeCompToVisit
{
public:
bool operator()(const Node* n1, const Node* n2)
{
	if (n1->numMoves != n2->numMoves) 
		return n1->numMoves < n2->numMoves;

	return n1->moves < n2->moves;

	if (n1->move != n2->move) 
		return n1->move < n2->move;

    return n1->board < n2->board;
}

};


class NodeCompVisited
{
public:
bool operator()(const Node* n1, const Node* n2)
{
	
    return n1->board < n2->board;
}

};


ostream& operator<<(ostream& os, const Node* newNode)
{
	os << newNode->board << " move " << newNode->move << " num " << newNode->numMoves << endl;
	return os;
}

int main()

{


	ofstream fout ("shuttle.out");
	ifstream fin ("shuttle.in");
	
	int n;
	fin >> n;
	
	int boardLen = 2 * n + 1;
	
	set<Node*, NodeCompToVisit> toVisit;
	set<ii> visited;
	
	string startStr;
	startStr.insert(startStr.begin(), n, 'W');
	startStr.insert(startStr.end(), 1, ' ');
	startStr.insert(startStr.end(), n, 'B');

	Node* first = new Node(startStr);
	first->space = n;
	first->numMoves = 0;
	first->move = 0;
	toVisit.insert(first);
	
	reverse(all(startStr));
	Node endNode(startStr);
	
	vector<int> ans;
	int iter = 0;
	
	while(!toVisit.empty())
	{
	    Node* node = *toVisit.begin();
	    toVisit.erase(toVisit.begin());
	    
	    if (contains(visited, node->toPair())) {
	        continue;
	    }
	    ++iter;
	    if (iter % 1000 == 0) {
	    cout << "Visited size " << visited.size();
	    cout << " To visit size " << toVisit.size() << endl;
	    }
		bool debug = false;
		//debug = debug || (node->numMoves == 9 && node->board == "BWBWBW ");
		if  (debug) {
			cout << "Looking at node " << node << endl;
		}

		visited.insert(node->toPair());
	    
		if (node->board == endNode.board)
		{
			ans = node->moves;
			/*
			Node* curNode = node;
			while(curNode)
			{
				cout << curNode->board << " move " << curNode->move << " num " << curNode->numMoves << endl;
				
				curNode = curNode->prevNode;
			}*/
			break;
		}

	    int space = node->space;
		assert(node->board[space] == ' ');
	    
	    if (space > 0) 
	    {
			Node* newNode = Node::createNode(node, space-1);

	        newNode->move = space-1+1;

			//cout << newNode->board << " move " << newNode->move << " num " << newNode->numMoves << endl;
			if (debug) {
				cout << "Adding 1 " << newNode << endl;
			}
	        toVisit.insert(newNode);
	    }
	    
	    if (space < boardLen - 1) 
	    {
	        Node* newNode = Node::createNode(node, space+1);

	        if (debug) {
				cout << "Adding 2 " << newNode << endl;
			}
	        toVisit.insert(newNode);
	    }
	    
	    if (space < boardLen - 2 && node->board[space+1] != node->board[space+2])
	    {
	        Node* newNode = Node::createNode(node, space+2);
	        
			if (debug) {
				cout << "Adding 3 " << newNode << endl;
			}
	        toVisit.insert(newNode);
	    }
	
	    if (space > 1 && node->board[space-1] != node->board[space-2])
	    {
	        Node* newNode = Node::createNode(node, space-2);
			if (debug) {
				cout << "Adding 4 " << newNode << endl;
			}
	        toVisit.insert(newNode);
	    }
	    
	    delete(node);
	}
	
	FOR(i, 0, ans.size())
	{
		
		if (i > 0 && i % 20 != 0)
			fout << " ";
		fout << ans[i];

		if ((i>0 && (i+1) % 20 == 0) || i == ans.size() - 1)
			fout << endl;
	}

	return 0;
}




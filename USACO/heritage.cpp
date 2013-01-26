/*
ID: eric7231
PROG: heritage
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
#include <sstream>
#include <bitset>
#include <iomanip>
#include <cctype>
#include <limits>
#include <cmath>
#include <functional>
#include <queue>
using namespace std;

typedef unsigned int uint;
typedef unsigned long long ull;

#define FORE(k,a,b) for(uint k=(a); k <= (b); ++k)
#define FOR(k,a,b) for(int k=(a); k < (int) (b); ++k)

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

bool debug = false;

template <class K, class V> 
V getMapValue( const map<K,V>& aMap, const K& key, const K& defaultValue )
{
    typename map<K, V>::const_iterator it = aMap.find(key);
    if ( it == aMap.end() )
        return defaultValue;
    return it->second;
}

class Node {
public:
    Node* left;
    Node* right;
    char c;
	Node() : left(0), right(0) {}
	Node(char cc) : left(0), right(0), c(cc) {}
};


void buildInOrderIndex(const string& inorder, vector<int>& nodeIndex) {
  for (int i = 0; i < inorder.size(); i++) {
      nodeIndex[ inorder[i] - 'A' ] = i;
  }
}
 
Node* buildTree(string inOrder, string preOrder, 
    int n, int offset, const vector<int>& inOrderIndex) {
  assert(n >= 0);
  if (n == 0) 
      return NULL;
  
  int rootVal = preOrder[0] - 'A';
  int rootPos = inOrderIndex[rootVal] - offset;  // the divider's index
  Node *root = new Node( preOrder[0] );
  root->left = buildTree(inOrder, preOrder.substr(1, string::npos),
	  rootPos, offset,inOrderIndex);
  
  root->right = buildTree(inOrder.substr(rootPos+1, string::npos),
      preOrder.substr(rootPos+1, string::npos), 
      n-rootPos-1, 
      offset+rootPos+1,
	  inOrderIndex);
  return root;
}

void printPostOrder(Node* tree, ostream& out)
{
    if (tree == NULL)
        return;
    
    printPostOrder(tree->left,out);
    printPostOrder(tree->right,out);
    
    out << tree->c;
}

int main() {
    
	ofstream fout ("heritage.out");
    ifstream fin ("heritage.in");
	
    string inorder, preorder;
    
    fin >> inorder >> preorder;
    
    vector<int> inOrderIndex(26);
	buildInOrderIndex(inorder, inOrderIndex);
	
	Node* tree = buildTree(inorder, preorder, inorder.length(), 0, inOrderIndex);
	
	printPostOrder(tree, fout);
	fout << endl;
	
    return 0;
}

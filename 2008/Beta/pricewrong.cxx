#include <vector>
#include <iostream>
#include <ctime>
#include <set>
#include <boost/algorithm/string.hpp>
#include <boost/shared_ptr.hpp>
#include <boost/bind.hpp>

using namespace std;

class Node;

//Boost sloows things down a lot
//typedef boost::shared_ptr<Node> NodePtr_t;
typedef Node* NodePtr_t;

struct NodeCompare
{
  bool operator()(NodePtr_t a, NodePtr_t b);
};

struct Node {
	int index, value, level;

	Node* parent;

	typedef set<NodePtr_t, NodeCompare> Children_t;
	Children_t children;

	static vector<NodePtr_t> nodes;
	static int maxLevel;

	static void reset() {
		nodes.clear();
		maxLevel = 0;
	}

	Node(int index, int value) :
		parent(NULL) {
		this->index = index;
		this->value = value;
		this->level = 0;
	}

	Node(Node* _parent, int _index, int _value) :
		parent(_parent), index(_index), value(_value) {
		this->level = parent->level + 1;
		maxLevel = max(this->level, maxLevel);
	}

	//Only add nodes that have a value greater than this node
	int add(const Node& newNode) {
		NodePtr_t childNode(new Node(this, newNode.index, newNode.value));

		if (children.empty()) {
		  children.insert(childNode);
		  nodes.push_back(childNode);
		  return childNode->level;
		} else {
		  //newNodes value must be greater to add the node
      Children_t::iterator end = children.lower_bound(childNode);

      int deepestLevelAdded = childNode->level;

      for (Children_t::iterator it = children.begin(); it != end; ++it) {
        deepestLevelAdded = max(deepestLevelAdded, (*it)->add(newNode));
      }

      if (deepestLevelAdded == childNode->level) {
        children.insert(childNode);
        nodes.push_back(childNode);
      }
		}
	}

	void names(set<string>& names, const vector<string>& nameList) {
		Node* n = this;
		while (n->parent) {
			names.insert(nameList[n->index]);
			n = n->parent;
		}
	}

};

int Node::maxLevel;
vector<NodePtr_t> Node::nodes;

bool NodeCompare::operator()(NodePtr_t a, NodePtr_t b)
{
  return a->value < b->value;
}

int main(int argc, char** args)
{
  int testCases;

  cin >> testCases;
  cin.ignore();

  std::clock_t start = std::clock();

  for(int testCase = 1; testCase <= testCases; ++testCase)
  {

    Node::reset();

    vector<string> names;

    string line;
    std::getline(cin, line);
    boost::split(names, line, boost::is_any_of("\t "));

    Node root(-1, 0);
    //cout << "names " << line << endl;

    for (int nameNum = 0; nameNum < names.size(); ++nameNum)
    {
      int val;
      cin >> val;
      //cout << val << ' ';
      root.add(Node(nameNum, val));
    }
    //cout << endl;
    cin.ignore();

    set<string> sortedNames(names.begin(), names.end());

    //smallest change according to lexicographic order
    set<string> minLexChange;

    for(vector<NodePtr_t>::const_iterator it = Node::nodes.begin(); it!= Node::nodes.end(); ++it)
    {
      if ((*it)->level != Node::maxLevel) {
        continue;
      }

      set<string> nodeNames;
      (*it)->names(nodeNames, names);

      set<string> dif;
      set_difference(sortedNames.begin(), sortedNames.end(), nodeNames.begin(), nodeNames.end(), inserter(dif, dif.begin()));
      if (minLexChange.empty() || minLexChange > dif) {
        minLexChange = dif;
      }
    }

    cout << "Case #" << testCase << ": ";

    for (set<string>::const_iterator it = minLexChange.begin(); it != minLexChange.end(); ++it)
    {
      cout << *it << ' ';
    }

    cout << endl;

  }

  cout<< "Time taken " << ( ( std::clock() - start ) / (double)CLOCKS_PER_SEC ) <<'\n';

}

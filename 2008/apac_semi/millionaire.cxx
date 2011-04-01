//
#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <map>
//#include <deque>
//#include <queue>
#include <sstream>
#include <time.h>
#include <assert.h>
#include <iomanip>
//#include <boost/smart_ptr.hpp>
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>
#include <boost/unordered_set.hpp>
#define SHOW_TIME 1
#include "util.h"
//#include "grid.h"
//#include <boost/math/common_factor.hpp>

#include <boost/shared_ptr.hpp>

using namespace std;


void do_test_case(int test_case, ifstream& input);

int main(int argc, char** args)
{
  if (argc < 2) {
    cerr << "Usage: <exe> input_file" << endl;
    return -1;
  }
  
  ifstream input;
  input.open(args[1]);
  
  int T;
  input >> T;

  SHOW_TIME_BEGIN(g) 
  	
  for (int test_case = 0; test_case < T; ++test_case) 
  {
    //try 
    {
      do_test_case(test_case, input);
    } 
    //catch(...) 
    {
      //error("Error exception caught\n"); 
    }
  }
  
  SHOW_TIME_END(g)
}

typedef pair<int, int> HW;
 
double round( double x )
{ 
const double sd = 1000000; //for accuracy to 3 decimal places
return int(x*sd + (x<0? -0.5 : 0.5))/sd;
}

ostream& operator<<(ostream& os, const HW& hw) {
  os << "H: " << hw.first << " W: " << hw.second;
  return os;
}

class Node;
typedef boost::shared_ptr<Node> NodePtr;

class Node
{
public:
  double minMoney;
  double prob;
  int level;  
  

  Node(double minMoney, double level, double prob) :
  minMoney(minMoney), prob(prob), level(level)
  {
  }
  
  static NodePtr createWinOnlyNode(const Node& sourceNode, int level, double P) 
  {
    assert(level > sourceNode.level);
    
    NodePtr node(new Node(
      sourceNode.minMoney / 2,
      level,
      sourceNode.prob * P));
    
    return node;
  }
  
  static NodePtr createWinLoseNode
    (const Node& winNode, const Node& loseNode, int level, double P) 
  {
    assert(level > winNode.level);
    
    assert(winNode.prob > loseNode.prob);
    assert(winNode.minMoney > loseNode.minMoney);
    
    NodePtr node(new Node(
      (loseNode.minMoney + winNode.minMoney) / 2,
      level,
      winNode.prob * P + loseNode.prob * (1-P)));
    
    return node;
  }
};


typedef vector<NodePtr> NodeList;
typedef boost::shared_ptr<NodeList> NodeListPtr;
//typedef boost::unordered_set<NodePtr> NodeSet;
typedef map<double, NodePtr, greater<double> > NodeMap;
//typedef map<int, NodeListPtr> NodeLevelMap;

ostream& operator<<(ostream& os, const Node& n) {
  os << "Level: " << n.level << " Min money: " << n.minMoney << " Prob " << n.prob;
  return os;
}

ostream& operator<<(ostream& os, const NodePtr& n) {
  os << *n;
  return os;
}

ostream& operator<<(ostream& os, NodeListPtr nl) {
  os << *nl;
  return os;
}

//step through half steps 
void generateNextLevel(NodeMap& nmap, int level, double P)
{
  NodeList newNodes;
  
  for(NodeMap::const_iterator oit = nmap.begin();
    oit != nmap.end();
    ++oit) 
  {
    NodePtr winNode = oit->second;
    //LOG(winNode);
    if (winNode->level == level - 1) {
      //LOG_STR("Creating straight shot node, betting all for node");
      NodePtr n = Node::createWinOnlyNode(*winNode, level, P);
      newNodes.push_back(n);
    }
    
    NodeMap::const_iterator in = oit;
    ++in;
    for(;
    in != nmap.end();
    ++in)
    {
      //LOG(*in);
      NodePtr loseNode = in->second;
      //LOG(winNode);
      //LOG(loseNode);
      if (loseNode->minMoney >= winNode->minMoney) {
        throw 3;
        continue;
      } 
      //assert
      if (loseNode->prob >= winNode->prob) {
        //throw 3;
        continue;
      }
      NodePtr n = Node::createWinLoseNode(*winNode, *loseNode, level, P);
      newNodes.push_back(n);
    }
  }
  
  for(NodeList::const_iterator it = newNodes.begin();
    it != newNodes.end();
    ++it)
  {
    NodeMap::iterator sit = nmap.find(it->get()->minMoney);
    if (sit != nmap.end()) {
      if (it->get()->prob < sit->second->prob) {
        continue;
      }
      
      nmap.erase(sit);
      
    }
    
    pair<NodeMap::iterator, bool> r = nmap.insert(NodeMap::value_type(it->get()->minMoney, *it));
    assert(r.second);
  }
  //copy(newNodes.begin(), newNodes.end(), insert_iterator<NodeSet>(nlist, nlist.begin()));
 
}

int operator<(NodePtr n1, NodePtr n2) {
  throw 4;
  return n1->minMoney > n2->minMoney;  
}

void do_test_case(int test_case, ifstream& input)
{
  int M;
  double P;
  int X;
  input >> M >> P >> X;
  //const int MAX_M = 5;
  LOG(M);
  
  const int GOAL = 1000000;
  
  NodeMap nmap;
  
  nmap.insert(NodeMap::value_type(GOAL, NodePtr(new Node(GOAL, 0, 1))));
  
  for(int m = 1; m<=M; ++m) {
    generateNextLevel(nmap, m, P);
  }
  
  //sort(nlist.begin(), nlist.end());
  
  //LOG(nlist);
  {
    /*
    NodeSet::const_iterator in = nlist.begin();
    NodePtr prevNode = *(in++);
    for(int i = 0; in != nlist.end(); ++in) {
      LOG(*in);
      LOG((*in)->prob - prevNode->prob);
      LOG((*in)->minMoney - prevNode->minMoney);
      //cout << endl;
      prevNode = *in;
      
    }*/
  }
  
  double max_p = 0;
  
  for(NodeMap::const_iterator in = nmap.begin();
    in != nmap.end();
    ++in)
  {
    const Node& node = *in->second;
    if (X >= node.minMoney) {
      max_p = max(max_p, node.prob);
    }
  }
  
  cout << "Case #" << (test_case+1) << ": " 
  << std::setprecision(7)
  //<< setiosflags(ios::fixed)
    << round(max_p) << endl;
  
  return;    
}


  

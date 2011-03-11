#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <map>
#include <deque>
#include <queue>
#include <sstream>
#include <time.h>
#include <assert.h>
#include <boost/smart_ptr.hpp>
#include <boost/bind.hpp>
#include "util.h"

using namespace std;

typedef vector<bool> VectorBool;
typedef vector<VectorBool> ArrayBool;

class Graph 
{
private:
  int numberOfNodes;
  
  ArrayBool connections;
  
public:
  Graph(int numberOfNodes);
  
  void addConnection(int nodeA, int nodeB);
  
  void removeNode(int node);
  
  void inverse();
  
  void findLargestSuperConnectedSubGraph(set<int>&);

};

Graph::Graph(int numberOfNodes) : 
  numberOfNodes(numberOfNodes), 
  connections(numberOfNodes, VectorBool(numberOfNodes, false))
{
  
}
  
void Graph::addConnection(int nodeA, int nodeB) 
{
  assert(nodeA >= 0 && nodeA < numberOfNodes);
  assert(nodeB >= 0 && nodeB < numberOfNodes);
  
  connections[nodeA][nodeB] = true;
  connections[nodeB][nodeA] = true;
}

void Graph::removeNode(int node)
{
  assert(node >= 0 && node < numberOfNodes);
  connections[node].resize(0); 
}

void Graph::inverse() 
{
  for(ArrayBool::iterator it = connections.begin();
    it != connections.end();
    ++it) 
  {
    VectorBool& vBool = *it;
    transform(vBool.begin(), vBool.end(), vBool.begin(), 
      boost::bind(logical_not<bool>(), _1));
  }
}

typedef set<int> SetInt;

ostream& operator<<(ostream& os, const set<int>& s)
{
  for(SetInt::const_iterator it = s.begin();
    it != s.end();
    ++it) 
  {
    os << *it << ' ';
  }
  
  return os;
}

void Graph::findLargestSuperConnectedSubGraph(set<int>& returnSet)
{
  if (numberOfNodes == 0) 
  {
    return;
  }
/*

11011
11111
00110
10011
10001

*/

  //Seed  
  typedef set<int> SetInt;
  typedef deque<SetInt> Queue;
  
  Queue q;
  
  for(int i = 0; i < numberOfNodes; ++i) 
  {
    if(connections[i].empty()) 
    {
      continue;
    }
    SetInt setInt;
    setInt.insert(i);
    q.push_back(setInt);
  }
  
  if(q.empty()) {
    return;
  }
  int count = 0;
  int max_set_size = 0;
  //cout << "q size " << q.size() << endl;
  
  while(q.size() > 0) 
  {
    ++count;
    if (count % 3000 == 0) {
      cout << count << " is  there" << endl;
    }
    SetInt s = q.front();
    
    if (s.size() > max_set_size) {
      max_set_size = s.size();
      returnSet = s;
      //cout << "Max size is " << max_set_size << endl;
    }
    //returnSet = q.front();
    q.pop_front();
    LOG_ON();
    LOG_STR("Super set: " << s);
    
    //for(int i = *s.rbegin(); i < numberOfNodes; ++i)
    for(int i = 0; i < numberOfNodes; ++i)
    {
      if (i >= *s.begin() && i <= *s.rbegin()) {
        //continue;  
      }
      
      if (s.size() == 1 && i <= *s.begin()) {
        //continue;
      }
      //cout << i << endl;
      //already in set, continue
      if(s.find(i) != s.end()) 
      {
        continue;
      }
       
      //if node removed, continue
      if(connections[i].empty()) 
      {
        continue;
      }
      
      bool can_add = true;
      
      for(SetInt::const_iterator it = s.begin();
          it != s.end();
          ++it)
      {
        if(!connections[i][*it]) 
        {
          //cout << "Nothing between " << i << " and " << (*it) << endl;
          can_add = false;
          break;
        }
      } 
      
      if(can_add) 
      {
        //set<int> ss = s;
        s.insert(i);
        q.push_back(s);
        break;
      }
    }
  }
  
  
  //returnSet = q.front();
}


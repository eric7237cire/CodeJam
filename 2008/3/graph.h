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

//http://en.wikipedia.org/wiki/Independent_set_problem

using namespace std;

namespace CompleteGraph
{
  
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

  friend ostream& operator<<(ostream& os, const Graph& g);
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

ostream& operator<<(ostream& os, const Graph& g)
{
  os << endl;
  for(int i = 0; i < g.numberOfNodes; ++i)
  {
    for(int s = 0; s < i; ++s)
    {
      os << ' ';
    }
    for(int j = i; j < g.numberOfNodes; ++j)
    {
      if (g.connections[i].empty()) {
        os << '-';
      } else {
        os << (g.connections[i][j] ? '1' : '0');
      }
    }
    os << endl;
  }
  return os;
}

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
  
  bool grew = false;
  LOG_OFF();
  do
  {
    grew = false;
    LOG_STR("Starting");
    cout << "q size " << q.size() << endl;
    for(Queue::iterator it = q.begin();
      it != q.end();      ) 
    {
      
      set<int>& s = *it;
      int old_size = s.size();
      LOG(s);      
      for(int i = 0; i < numberOfNodes; ++i)
      {
        //LOG_STR(i);
        if(s.find(i) != s.end()) 
        {
          continue;
        }
        
        if (connections[i].empty()) {
          continue;
        }
        
        bool can_add = true;
        
        for(SetInt::const_iterator it = s.begin();
          it != s.end();
          ++it)
        {
          ++count;
      //LOG_STR(count);
      if (count % 3000 == 0) {
        cout << count << " is  there" << endl;
      }
          //LOG_STR("it " << (*it) << " s: " << s);
          assert(i >= 0 && i < numberOfNodes);
          assert((*it) >= 0 && (*it) < numberOfNodes);
          //cout << (*it) << " " << i << endl;
          if(!connections[i][*it]) 
          {
            //cout << "Nothing between " << i << " and " << (*it) << endl;
            can_add = false;
            break;
          }
        }
                        
        if(can_add) 
        {
          s.insert(i);
          //LOG_STR(s);
          
          break;
        }
        
      }
      //LOG_STR("wah" << grew << " " << s.size() << " " << old_size << " " << q.size());
      if (s.size() > old_size) {
        grew = true;
        ++it;
      } else {
        if (q.size() == 1) {
          returnSet = q.front();
          return;
        } 
        it = q.erase(it);
      }
    }
    
    LOG_STR("size " <<  q[0].size() << "  " << q[0]);
    
  } while(grew);
  
    
  returnSet = q.front();
  
} 
}

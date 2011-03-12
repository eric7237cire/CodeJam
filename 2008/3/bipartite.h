#include <fstream>
#include <iostream>
#include <algorithm>
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


namespace Bipartite
{
  
typedef set<int> NodeConnections;
typedef boost::shared_ptr<NodeConnections> NodeConnectionsPtr;
typedef map<int, NodeConnectionsPtr> GraphConnections;
typedef set<int> SetInt;
typedef set<int> SetNode;
typedef set<int> NodeSet;
typedef pair<int, int> Edge;
typedef set<Edge> EdgeSet;

class Node;
typedef boost::shared_ptr<Node> NodePtr;

class Node
{
public:
  NodePtr parent;
  int value;
  
  Node(NodePtr parent, int node);
};

Node::Node(NodePtr parent, int node) : parent(parent), value(node)
{
  
}

template<typename T> ostream& operator<<(ostream& os, const set<T>& s);

ostream& operator<<(ostream& os, const Edge& edge);

template<typename T> bool isMember(const set<T>& aSet, const T& value)
{
  return aSet.find(value) != aSet.end();
};

template<typename K, typename V> bool isMember(const map<K, V>& aSet, const K& value)
{
  return aSet.find(value) != aSet.end();
};


class Graph 
{
private:
  int numberOfNodes;
  
  GraphConnections connections;
  
  SetNode xNodes;
  SetNode yNodes;
  SetNode nodes;
  
public:
  Graph();
  
  void addNode(int node);
  void partition();  
  void addConnection(int nodeA, int nodeB);
  
  
  void removeNode(int node);
  
  //Correspond à complément de minimum vertex cover
  void findMaximumIndependantSet(set<int>&) const;
  void findMinimumVertexCover(const EdgeSet& match, NodeSet& vertexCover) const;
  
  friend ostream& operator<<(ostream& os, const Graph& g);
  
private:
  bool nodeExists(int node) const;
  bool isConnected(int nodeA, int nodeB) const;
  void createInitialMatching(EdgeSet& match, SetNode& freeX, SetNode& freeY) const;
  static Edge buildEdge(int nodeA, int nodeB);
  bool growMatch(EdgeSet& match, SetNode& freeX, SetNode& freeY) const;
  void augmentMatch(NodePtr freeYNode, EdgeSet& match) const;
  static int getUnmatchedVertex(NodeSet& nodeSet); 
};

Graph::Graph() : 
  numberOfNodes(0), 
  connections()
{
  
}

bool Graph::nodeExists(int node) const
{
  if(connections.find(node) == connections.end()) {
    return false;
  }
  
  assert(xNodes.find(node) != xNodes.end() ||
    yNodes.find(node) != yNodes.end());
  
  return true;
}

Edge Graph::buildEdge(int nodeA, int nodeB)
{
  if (nodeA < nodeB) {
    return Edge(nodeA, nodeB);
  }
  
  return Edge(nodeB, nodeA);
}

int Graph::getUnmatchedVertex(NodeSet& nodeSet)
{
  assert(!nodeSet.empty());
  NodeSet::iterator it = nodeSet.begin();
  int retVal = *it;
  nodeSet.erase(it);
  return retVal;
}

void Graph::addNode(int node)
{
  ++numberOfNodes;
  nodes.insert(node);
  assert(nodes.size() == numberOfNodes);
  
  connections.insert(GraphConnections::value_type(node, 
    NodeConnectionsPtr(new NodeConnections())));
  
  assert(connections.size() == numberOfNodes);
}

void Graph::partition()
{
  if (nodes.empty()) {
    return;
  }
  
  assert(xNodes.empty());
  assert(yNodes.empty());
  
  LOG_STR("partition");
  set<int> visited;
  deque<int> to_visit;
  
  to_visit.push_back(*nodes.begin());
  xNodes.insert(*nodes.begin());
  
  while(!to_visit.empty())
  {
    int node = to_visit.front();
    to_visit.pop_front();
    
    visited.insert(node);
    
    bool isXNode = isMember(xNodes, node);
    
    //assert(nodeExists(node));
    
    assert(isXNode || yNodes.find(node) != yNodes.end());
    
    assert(isMember(connections, node));
    
    NodeConnectionsPtr neighbors = connections.find(node)->second;
    
    for(NodeConnections::const_iterator it = neighbors->begin();
      it != neighbors->end();
      ++it)
    {
      if (!isMember(visited, *it))
      {
        to_visit.push_back(*it);
        if (isXNode) {
          yNodes.insert(*it);
        } else {
          xNodes.insert(*it);
        }
      }
    }
  }
    
  
}
  
void Graph::addConnection(int nodeA, int nodeB) 
{
  assert(isMember(connections, nodeA));
  assert(isMember(connections, nodeB));

  GraphConnections::iterator it_a = connections.find(nodeA);
  GraphConnections::iterator it_b = connections.find(nodeB);

  pair<NodeConnections::iterator, bool> r;
  
  r = it_a->second->insert(nodeB);
  assert(r.second);
  r = it_b->second->insert(nodeA);
  assert(r.second);
  
}

void Graph::removeNode(int node)
{
  assert(false); 
}



ostream& operator<<(ostream& os, const Graph& g)
{
  os << "X nodes: " << g.xNodes << endl;
  os << "Y nodes: " << g.yNodes << endl;
  
  /*
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
  }*/
  return os;
}

template<typename T> ostream& operator<<(ostream& os, const set<T>& s)
{
  for(typename set<T>::const_iterator it = s.begin();
    it != s.end();
    ++it) 
  {
    os << *it << ' ';
  }
  
  return os;
}

ostream& operator<<(ostream& os, const Edge& edge)
{
  os << "[ " << edge.first << ", " << edge.second << "]" << endl;
  return os;
}


void Graph::createInitialMatching(EdgeSet& match, SetNode& freeX, SetNode& freeY) const
{
  
}

void Graph::findMaximumIndependantSet(set<int>& returnSet) const
{
  if (numberOfNodes == 0) 
  {
    return;
  }
  
  EdgeSet match;
  
  SetNode freeX(xNodes);
  SetNode freeY(yNodes);
  
  createInitialMatching(match, freeX, freeY);
  
  while(growMatch(match, freeX, freeY))
  {
    LOG_STR("match grew"); 
  }
  
  LOG_STR("Match: [" << match << "]");
  
  set<int> vertexCover;
  findMinimumVertexCover(match, vertexCover);
  
  set<int> allVertexes;
  set_union(xNodes.begin(), xNodes.end(),
    yNodes.begin(), yNodes.end(),
    insert_iterator<NodeSet>(allVertexes, allVertexes.begin()));
  
  assert(allVertexes.size() == xNodes.size() + yNodes.size());
  
  set<int> maxIndependantSet;
  
  set_difference(allVertexes.begin(), allVertexes.end(),
    vertexCover.begin(), vertexCover.end(),
    insert_iterator<NodeSet>(maxIndependantSet, maxIndependantSet.begin()));
  
  returnSet = maxIndependantSet;
  
}

void Graph::findMinimumVertexCover(const EdgeSet& match, NodeSet& vertexCover) const
{
  
}

bool edgesIntersect(const Edge& edge1, const Edge& edge2)
{
  if (edge1.first == edge2.first ||
    edge1.first == edge2.second ||
    edge1.second == edge2.first ||
    edge1.second == edge2.second) 
  {
    return true;
  }

  return false;  
}

void Graph::augmentMatch(NodePtr freeYNode, EdgeSet& match) const
{
  //match.erase(match.begin(), match.end());
  
  NodePtr node = freeYNode;
  
  while(node && node->parent)
  {
    Edge edge = buildEdge(node->value, node->parent->value);
    for(EdgeSet::iterator it = match.begin(); it != match.end();)
    {
      if (edgesIntersect(edge, *it)) {
        match.erase(it++);
      } else {
        ++it;
      }
        
    }
    match.insert(edge);
    node = node->parent;
  }
}

bool Graph::growMatch(EdgeSet& match, SetNode& freeX, SetNode& freeY) const
{
  int freeVertexFromX = getUnmatchedVertex(freeX);
  
  SetNode visited;
  deque<NodePtr> toVisit;
  
  toVisit.push_back(NodePtr(new Node(NodePtr(), freeVertexFromX)));
 
  LOG_STR("growMatch starting.  Match: " << match << "\n" 
    << " FreeX: " << freeX << "\n"
    << " FreeY: " << freeY << "\n"
    << " selected x " << freeVertexFromX);
    
  while(!toVisit.empty())
  {
    NodePtr nodePtr = toVisit.front();
    int node = nodePtr->value;
    
    NodeSet::iterator freeYit = freeY.find(node); 
    if(freeYit != freeY.end())
    {
      LOG_STR("Augmented match!");
      augmentMatch(nodePtr, match);
      freeY.erase(freeYit);
      LOG_STR(match);
      return true;      
    }
    
    toVisit.pop_front();
    visited.insert(node);
    
    //X to Y use an unmatched edge
    //Y to X, use a matched edge
    bool useUnmatchedEdge = xNodes.find(node) != xNodes.end();
    LOG_STR("useUnmatchedEdge " << useUnmatchedEdge);
    assert(nodeExists(node));
    
    GraphConnections::const_iterator connIt = connections.find(node);  
    NodeConnections& connections = *(connIt->second);
    
    for(NodeConnections::const_iterator it = connections.begin();
      it != connections.end();
      ++it)
    {
      Edge e = buildEdge(node, *it);
      int otherNode = *it;
      bool isMatched = match.find(e) != match.end();
      LOG_STR("Edge " << e << " is matched " << isMatched);
      
      bool hasVisited = visited.find(otherNode) != visited.end();
        LOG_STR("Has visited: " << hasVisited);
        
      if ( !hasVisited && ( (useUnmatchedEdge && !isMatched) || 
        (!useUnmatchedEdge && isMatched) ) )
      {
        LOG_STR("Going to visit " << otherNode);
        
        toVisit.push_back(NodePtr(new Node(nodePtr, otherNode)));
      }
    }
    
    
  }
  
  return false;
}



} //namespace

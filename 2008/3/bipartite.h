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
#include <boost/unordered_map.hpp>
#include <boost/unordered_set.hpp>
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
typedef boost::shared_ptr<NodeSet> NodeSetPtr;
typedef pair<int, int> Edge;
typedef boost::unordered_set<Edge> EdgeSet;
typedef boost::unordered_set<int> NodeHashSet;

typedef boost::unordered_map<int, Edge> NodeToEdge;

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

template<typename C, typename T> bool isMember(const C& aSet, const T& value)
{
  return aSet.find(value) != aSet.end();
};

template<typename C, typename T> bool remove(C& aSet, const T& value)
{
  typename C::iterator it = aSet.find(value);
  if (it == aSet.end()) {
    return false;
  }
  
  aSet.erase(it);
  return true;
}

template<typename T> bool removeAll(set<T>& aSet, const set<T>& aSetToRemove)
{
  typename set<T>::const_iterator it = aSetToRemove.begin();
  
  for( ; it != aSetToRemove.end(); ++it)
  {
    remove(aSet, *it);
  }
  
  return true;
}






class Graph 
{
private:
  int numberOfNodes;
  
  GraphConnections connections;
  
  SetNode xNodes;
  SetNode yNodes;
  SetNode nodes;
  
  NodeToEdge nodeToMatchEdge;
  EdgeSet matchEdges;
  
public:
  Graph();
  
  void addNode(int node);
  void partition();  
  void addConnection(int nodeA, int nodeB);
  
  
  void removeNode(int node);
  
  //Correspond à complément de minimum vertex cover
  void findMaximumIndependantSet(set<int>&);
  
  friend ostream& operator<<(ostream& os, const Graph& g);
  
private:
  bool nodeExists(int node) const;
  bool isConnected(int nodeA, int nodeB) const;
  void createInitialMatching(SetNode& freeX, SetNode& freeY);
  static Edge buildEdge(int nodeA, int nodeB);
  bool growMatch(SetNode& freeX, SetNode& freeY);
  void augmentMatch(NodePtr freeYNode);
  static int getUnmatchedVertex(const NodeSet& nodeSet);
  void findUnmatchedVertices(NodeSet& unmatchedVertices);
  void findMatchedVertices(NodeSet& matchedVertices);
  void findMinimumVertexCover(NodeSet& vertexCover);
  bool isUnmatchedEdge(int unmatchedNode, int matchedNode) const;
  
  void addMatchEdge(const Edge& e);
};

Graph::Graph() : 
  numberOfNodes(0), 
  connections()
{
  
}

//TODO handle freeX freeY aussi
void Graph::addMatchEdge(const Edge& edge)
{
  assert(2 * matchEdges.size() == nodeToMatchEdge.size()); 
  assert(!isMember(nodeToMatchEdge, edge.first));
  assert(!isMember(nodeToMatchEdge, edge.second));
  matchEdges.insert(edge);
  nodeToMatchEdge.insert(NodeToEdge::value_type(edge.first, edge));
  nodeToMatchEdge.insert(NodeToEdge::value_type(edge.second, edge));
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

bool Graph::isConnected(int nodeA, int nodeB) const
{
  GraphConnections::const_iterator it = connections.find(nodeA);
  
  assert(it != connections.end());
  
  NodeConnectionsPtr nodeConnections = it->second;
  
  assert(nodeConnections);
  
  return isMember(*nodeConnections, nodeB);
  
}

Edge Graph::buildEdge(int nodeA, int nodeB)
{
  if (nodeA < nodeB) {
    return Edge(nodeA, nodeB);
  }
  
  return Edge(nodeB, nodeA);
}

int Graph::getUnmatchedVertex(const NodeSet& nodeSet)
{
  assert(!nodeSet.empty());
  NodeSet::iterator it = nodeSet.begin();
  int retVal = *it;
  //nodeSet.erase(it);
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
    
    //LOG_STR("Node " << node << " visited: " << visited.size()) ;
    
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
        if (isXNode && !isMember(yNodes, *it)) {
          yNodes.insert(*it);
          to_visit.push_back(*it);
        } else if (!isXNode && !isMember(xNodes, *it)) {
          xNodes.insert(*it);
          to_visit.push_back(*it);
        }
      }
    }
  }
  
  assert(xNodes.size() + yNodes.size() == nodes.size());
    
  
}
  
void Graph::addConnection(int nodeA, int nodeB) 
{
  assert(isMember(connections, nodeA));
  assert(isMember(connections, nodeB));

  //LOG_STR("Adding connection " << nodeA << ", " << nodeB);
  
  GraphConnections::iterator it_a = connections.find(nodeA);
  GraphConnections::iterator it_b = connections.find(nodeB);

  pair<NodeConnections::iterator, bool> r;
  
  r = it_a->second->insert(nodeB);
  //assert(r.second);
  r = it_b->second->insert(nodeA);
  //assert(r.second);
  
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

template<typename T> ostream& operator<<(ostream& os, const boost::unordered_set<T>& s)
{
  for(typename boost::unordered_set<T>::const_iterator it = s.begin();
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


void Graph::createInitialMatching(SetNode& freeX, SetNode& freeY)
{
  
    
  int x = 0;
  int y = 0;
  
  for(SetNode::iterator it_x = freeX.begin();
    it_x != freeX.end();
    )
  {
    bool added_x = false;
    
    GraphConnections::const_iterator connIt = connections.find(*it_x);  
    NodeConnections& connections = *(connIt->second);
      
    for(NodeConnections::const_iterator it = connections.begin();
        it != connections.end();
        ++it)
    {
      const int yNode = *it;
      
      bool isFreeY = isMember(freeY, yNode);
      
      Edge e = buildEdge(*it_x, yNode);
      
      bool isMatched = isMember(matchEdges, e);
      if (isMatched) {
        assert(!isMember(yNodes, yNode));
        continue;
      }
      
      if (!isFreeY) {
        continue;
      }
      
      
      assert(isFreeY);
      addMatchEdge(e);
      
      remove(freeY, yNode);
      added_x = true;
      break;
    }
      
    if (added_x) {
      freeX.erase(it_x++);
    } else {
      ++it_x;
    }
  
      
  }
}

void Graph::findMaximumIndependantSet(set<int>& maxIndependantSet)
{
  if (numberOfNodes == 0) 
  {
    return;
  } 
  
  
  SetNode freeX(xNodes);
  SetNode freeY(yNodes);
  
  createInitialMatching(freeX, freeY);
  //cout << "Match is now " << match.size() << endl;
  
  while(growMatch(freeX, freeY))
  {
    LOG_ON();
    LOG_STR("Match is now " << matchEdges.size());
    LOG_OFF();
  } 
  
  LOG_STR("MATCH DONE");
  LOG_STR("Match: [" << matchEdges << "]");
  LOG_STR("Match size: " << matchEdges.size());
  LOG_STR("Number of vertices: " << nodes.size());
  
  set<int> vertexCover;
  findMinimumVertexCover(vertexCover);
  
  set_difference(nodes.begin(), nodes.end(),
    vertexCover.begin(), vertexCover.end(),
    insert_iterator<NodeSet>(maxIndependantSet, maxIndependantSet.begin()));
  
}

void Graph::findUnmatchedVertices(NodeSet& unmatchedVertices)
{
  unmatchedVertices = nodes;
  
  for(EdgeSet::iterator it = matchEdges.begin(); it != matchEdges.end(); ++it)
  {
    remove(unmatchedVertices, it->first);
    remove(unmatchedVertices, it->second);
  }

  
}

void Graph::findMatchedVertices(NodeSet& matchedVertices)
{
  for(EdgeSet::iterator it = matchEdges.begin(); it != matchEdges.end(); ++it)
  {
    matchedVertices.insert(it->first);
    matchedVertices.insert(it->second);
  }
}

bool Graph::isUnmatchedEdge(int unmatchedNode, int matchedNode) const
{
  if (unmatchedNode == 8) {
    LOG_STR("isUnmatchedEdge " << unmatchedNode << ", " << matchedNode);  
  }
  
  
  if (!isConnected(unmatchedNode, matchedNode)) {
    return false;
  }
  
  if (unmatchedNode == 8) {
    LOG_STR("Connected " << unmatchedNode << ", " << matchedNode);  
  }
    
  Edge edge = buildEdge(unmatchedNode, matchedNode);
  bool isInMatched = isMember(matchEdges, edge);
  LOG_STR("Edge: " << edge << " matched? " << isInMatched);
  
  if (isInMatched) {
    return false;
  }
  
  return true;
}

//TODO
int getMatchedVertex(const EdgeSet& matchEdges, int node)
{
  for(EdgeSet::const_iterator it = matchEdges.begin(); it != matchEdges.end(); ++it)
  {
    const Edge& edge = *it;
    if (edge.first == node) {
      return edge.second;
    }
    if (edge.second == node) {
      return edge.first;
    }
  }
  
  throw "Could not find matched vertex";
}

void addNodeToOddLevel(int nodeToAdd, const EdgeSet& matchEdges, NodeSetPtr oddSet, NodeSetPtr evenSet, const NodeSet& matchedVertices)
{
  oddSet->insert(nodeToAdd);
          
  int matchedVertex = getMatchedVertex(matchEdges, nodeToAdd);
          
  //if hasn't been placed yet, place it
  if (isMember(matchedVertices, matchedVertex)) {
    evenSet->insert(matchedVertex); 
  } 
}

void Graph::findMinimumVertexCover(NodeSet& vertexCover) 
{
  //http://en.wikipedia.org/wiki/K%C3%B6nig's_theorem_(graph_theory)
  
  /*
  Suppose that G=(V,E) is a bipartite graph, where V = A ∪ B. Let M be a matching for G.
We must show either G has a vertex cover C of size |M|, or M is not a maximum matching.
First, if M is a perfect matching, then M is maximum. In this case, every edge is incident to exactly one vertex on either side, so any partition of G is a vertex cover of size |M| and we are done.
Otherwise, use an alternating path argument. An alternating path is a path where the edges alternate between M and E \ M. Partition the vertices of G into subsets Si as follows. Let S0 consist of all vertices unmatched by M. For integer j ≥ 0, let S2j+1 be the set of vertices that:
Are adjacent to vertices in S2j via some edge e ∈ E \ M.
Have not been included in any previously-defined set Sk, where k < j.
Each vertex v ∈ S2j+1 must be adjacent to another vertex u via an edge e = ∈ M (otherwise, v is unmatched by M and would have been placed in S0). If the u has not yet been included in a set Si, insert u in S2j+2. If there are no vertices adjacent to S2j, arbitrarily pick an unused vertex and continue in S2j+1.
*/
  NodeSetPtr unmatchedVertices(new NodeSet());  
  findUnmatchedVertices(*unmatchedVertices);
  
  NodeSet matchedVertices;  
  findMatchedVertices(matchedVertices);
  
  assert(unmatchedVertices->size() + matchedVertices.size() == nodes.size());
  
  //S0
  vector<NodeSetPtr> levels;
  levels.push_back(unmatchedVertices);
  
  int j = 0;
  
  do
  {
    LOG_STR("Beginning level");
    LOG_STR("S[2j] = " << *levels[2*j]);
    NodeSetPtr TwoJ = levels[2 * j];
    //odd (S1 S3 etc)
    NodeSetPtr TwoJPlusOne(new NodeSet());
    
    //even
    NodeSetPtr TwoJPlusTwo(new NodeSet());
    
    levels.push_back(TwoJPlusOne);
    levels.push_back(TwoJPlusTwo);
    
    for(NodeSet::const_iterator it = TwoJ->begin();
      it != TwoJ->end();
      ++it)
    {
      //must connect to a matched vertex via an unmatched edge
      for(NodeSet::iterator mit = matchedVertices.begin();
        mit != matchedVertices.end();
        ++mit)
      {
        if (isUnmatchedEdge(*it, *mit)) {
          addNodeToOddLevel(*mit, matchEdges, levels[2*j+1], levels[2*j+2], matchedVertices);
          
           
        }
        
        
      }
    }
    
    //If there are no vertices adjacent to S2j, arbitrarily pick an unused vertex and continue in S2j+1.
    if (levels[2*j+1]->size() == 0 && matchedVertices.size() > 0) {
      int toAdd = *matchedVertices.begin();
      addNodeToOddLevel(toAdd, matchEdges, levels[2*j+1], levels[2*j+2], matchedVertices);
    }
    
    LOG_STR("S[2j+1] = " << *levels[2*j+1]);
    LOG_STR("S[2j+2] = " << *levels[2*j+2]);
    
    removeAll(matchedVertices, *TwoJPlusOne);
    removeAll(matchedVertices, *TwoJPlusTwo);

    copy(TwoJPlusOne->begin(), TwoJPlusOne->end(),
      insert_iterator<NodeSet>(vertexCover, vertexCover.begin()));
    
    if (matchedVertices.empty()) {
      return;
    }
    
    LOG_STR("Matched vertices " << matchedVertices);
      
    if (TwoJPlusOne->size() == 0 || TwoJPlusTwo->size() == 0) {
      throw 3;
    }
    
    ++j;
    
  } while(true);

    
  
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

void Graph::augmentMatch(NodePtr freeYNode)
{
  //match.erase(match.begin(), match.end());
  const int oldSize = matchEdges.size();
  
  NodePtr node = freeYNode;
  
  while(node && node->parent)
  {
    Edge edge = buildEdge(node->value, node->parent->value);
    for(EdgeSet::iterator it = matchEdges.begin(); it != matchEdges.end();)
    {
      if (edgesIntersect(edge, *it)) {
        
        remove(nodeToMatchEdge, it->first);
        remove(nodeToMatchEdge, it->second);
        matchEdges.erase(it++);
      } else {
        ++it;
      }
        
    }
    
    addMatchEdge(edge);
    node = node->parent->parent;
  }
  
  assert(matchEdges.size() == oldSize + 1);
}

bool Graph::growMatch(SetNode& freeX, SetNode& freeY)
{
  if (freeX.empty()) {
    LOG_STR("No more free x");
    return false;
  }
  
  LOG_OFF();
  
  for(NodeSet::const_iterator it = freeX.begin();
    it != freeX.end();
    ++it)
  {
    const int freeVertexFromX = *it; //getUnmatchedVertex(freeX);
  
    SetNode visited;
    NodeHashSet inToVisit;
    deque<NodePtr> toVisit;
    
    toVisit.push_back(NodePtr(new Node(NodePtr(), freeVertexFromX)));
   
    LOG_STR("growMatch starting.\n  Match: " << matchEdges << "\n" 
      << " FreeX: " << freeX << "\n"
      << " FreeY: " << freeY << "\n"
      << " selected x " << freeVertexFromX);
     
    int edgesConnected = 0;
    
    while(!toVisit.empty())
    {
      NodePtr nodePtr = toVisit.front();
      int node = nodePtr->value;
      
      NodeSet::iterator freeYit = freeY.find(node); 
      if(freeYit != freeY.end())
      {
        LOG_STR("Augmented match!");
        augmentMatch(nodePtr);
        freeY.erase(freeYit);
        remove(freeX, freeVertexFromX);
        LOG_STR(matchEdges);
        return true;      
      }
      
      toVisit.pop_front();
      
      assert(!isMember(visited, node));
      
      visited.insert(node);
      
      //X to Y use an unmatched edge
      //Y to X, use a matched edge
      bool useUnmatchedEdge = isMember(xNodes, node);
      LOG_STR("useUnmatchedEdge " << useUnmatchedEdge);
      assert(nodeExists(node));
      
      GraphConnections::const_iterator connIt = connections.find(node);  
      NodeConnections& connections = *(connIt->second);
      
      for(NodeConnections::const_iterator it = connections.begin();
        it != connections.end();
        ++it)
      {
        const int otherNode = *it;
        
        bool hasVisited = isMember(visited, otherNode);
        LOG_STR("Has visited: " << hasVisited);
        
        if (hasVisited) {
          continue;
        }
        
        ++edgesConnected;
        if (edgesConnected % 5000 == 0) {
          //cout << "Edges: " << edgesConnected << endl;
        }
        Edge e = buildEdge(node, *it);
        
        bool isMatched = matchEdges.find(e) != matchEdges.end();
        LOG_STR("Edge " << e << " is matched " << isMatched);
        
          
        if ( !isMember(inToVisit, otherNode) && ( (useUnmatchedEdge && !isMatched) || 
          (!useUnmatchedEdge && isMatched) ) )
        {
          LOG_STR("Going to visit " << otherNode);
          
          toVisit.push_back(NodePtr(new Node(nodePtr, otherNode)));
          inToVisit.insert(otherNode);
        }
      }
      
      
    }
  }
  
  return false;
}



} //namespace

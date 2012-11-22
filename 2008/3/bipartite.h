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

//#undef assert
//#define assert(x) ((void)0)

namespace Bipartite
{
  
typedef set<int> NodeConnections;
typedef boost::shared_ptr<NodeConnections> NodeConnectionsPtr;
typedef map<int, NodeConnectionsPtr> GraphConnections;
typedef set<int> SetInt;
typedef set<int> NodeSet;
typedef boost::shared_ptr<NodeSet> NodeSetPtr;

typedef pair<int, int> Edge;
typedef boost::unordered_set<Edge> EdgeSet;
typedef boost::unordered_set<int> NodeHashSet;

typedef boost::unordered_map<int, Edge> NodeToEdge;

class Node;
typedef boost::shared_ptr<Node> NodePtr;

typedef boost::unordered_map<int, NodePtr> AugmentingTree;

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







class Graph 
{
private:
  int numberOfNodes;
  
  GraphConnections connections;
  
  NodeSet xNodes;
  NodeSet yNodes;
  NodeSet nodes;
  
  NodeSet freeX;
  NodeSet freeY;
  
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
  void createInitialMatching();
  Edge buildEdge(int nodeA, int nodeB) const;
  bool growMatch();
  void augmentMatch(NodePtr freeYNode);
  static int getUnmatchedVertex(const NodeSet& nodeSet);
  void findUnmatchedVertices(NodeSet& unmatchedVertices);
  void findMatchedVertices(NodeSet& matchedVertices);
  void findMinimumVertexCover(NodeSet& vertexCover);
  bool isUnmatchedEdge(int unmatchedNode, int matchedNode) const;
  
  void addMatchEdge(const Edge& e);
  void removeMatchEdge(const Edge& edge);
  
  void addNodeToOddLevel(int nodeToAdd, NodeSetPtr oddSet, NodeSetPtr evenSet, const NodeSet& matchedVertices);
  const NodeConnections& getConnections(int node) const;
  
  void addToSNeighbors(int xnode, 
  NodeHashSet& S_neighbors,
  AugmentingTree& tree) const;
  
  void addToS(int xnode,
  int ynode,
  NodeHashSet& S,
  NodeHashSet& S_neighbors,
  NodeHashSet& T,
  AugmentingTree& tree) const;
  
  void addToS(int xnode, 
  NodeHashSet& S,
  NodeHashSet& S_neighbors,
  AugmentingTree& tree) const;

};

Graph::Graph() : 
  numberOfNodes(0), 
  connections()
{
  
}

bool isInEdge(const Edge& edge, int node)
{
  return edge.first == node || edge.second == node; 
}

void Graph::addMatchEdge(const Edge& edge)
{
  assert(2 * matchEdges.size() == nodeToMatchEdge.size()); 
  assert(!isMember(nodeToMatchEdge, edge.first));
  assert(!isMember(nodeToMatchEdge, edge.second));
  
  assert(isMember(freeX, edge.first));
  assert(isMember(freeY, edge.second));
  
  remove(freeX, edge.first);
  remove(freeY, edge.second);
  
  matchEdges.insert(edge);
  nodeToMatchEdge.insert(NodeToEdge::value_type(edge.first, edge));
  nodeToMatchEdge.insert(NodeToEdge::value_type(edge.second, edge));
}

void Graph::removeMatchEdge(const Edge& edge)
{
  assert(2 * matchEdges.size() == nodeToMatchEdge.size()); 
  assert(isMember(nodeToMatchEdge, edge.first));
  assert(isMember(nodeToMatchEdge, edge.second));
  
  assert(!isMember(freeX, edge.first));
  assert(!isMember(freeY, edge.second));
  
  assert(isMember(xNodes, edge.first));
  assert(isMember(yNodes, edge.second));
  
  freeX.insert(edge.first);
  freeY.insert(edge.second);
  
  remove(matchEdges, edge);
  remove(nodeToMatchEdge, edge.first);
  remove(nodeToMatchEdge, edge.second);
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

Edge Graph::buildEdge(int nodeX, int nodeY) const
{
  assert(isMember(xNodes, nodeX));
  assert(isMember(yNodes, nodeY));
  
  return Edge(nodeX, nodeY);
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


void Graph::createInitialMatching()
{
  
  int x = 0;
  int y = 0;
  
  //Ne modifie pas directement freeX
  NodeSet freeXToSearch(freeX);
  
  for(NodeSet::iterator it_x = freeXToSearch.begin();
    it_x != freeXToSearch.end();
    ++it_x)
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
      
      if (isMember(matchEdges, e)) {
        assert(!isMember(yNodes, yNode));
        continue;
      }
      
      if (!isFreeY) {
        continue;
      }
      
      addMatchEdge(e);
      added_x = true;
      break;
    }      
  }
}

void Graph::findMaximumIndependantSet(set<int>& maxIndependantSet)
{
  if (numberOfNodes == 0) 
  {
    return;
  } 
  
  
  freeX = xNodes;
  freeY = yNodes;
  
  createInitialMatching();
  
  while(growMatch())
  {
    LOG_ON();
    LOG_STR("Match is now " << matchEdges.size());
    LOG_OFF();
  } 
  LOG_OFF();
  LOG_STR("MATCH DONE");
  LOG_STR("Match: [" << matchEdges << "]");
  LOG_STR("Match size: " << matchEdges.size());
  LOG_STR("Number of vertices: " << nodes.size());
  //LOG_OFF();
  LOG_STR("Vertex cover");
  LOG_OFF();
  set<int> vertexCover;
  findMinimumVertexCover(vertexCover);
  
  //LOG_OFF();
  LOG_STR("Vertex cover done");
  LOG_OFF();
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
  if (!isConnected(unmatchedNode, matchedNode)) {
    return false;
  }
  
  NodeToEdge::const_iterator it = nodeToMatchEdge.find(matchedNode);
  
  assert(it != nodeToMatchEdge.end());
    
  const Edge& edge = it->second;
  bool isInMatched = isInEdge(edge, unmatchedNode);
  LOG_STR("Edge: " << edge << " matched? " << isInMatched);
  
  if (isInMatched) {
    return false;
  }
  
  return true;
}


void Graph::addNodeToOddLevel(int nodeToAdd, NodeSetPtr oddSet, NodeSetPtr evenSet, const NodeSet& matchedVertices)
{
  oddSet->insert(nodeToAdd);
  
  NodeToEdge::const_iterator it = nodeToMatchEdge.find(nodeToAdd);
  assert(it != nodeToMatchEdge.end());
  
  const Edge& matchingEdge = it->second;
  
  int matchedVertex = (matchingEdge.first == nodeToAdd ? matchingEdge.second : matchingEdge.first);
          
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
      const int unmatchedNode = *it;
      
      GraphConnections::const_iterator gcit = connections.find(unmatchedNode);
      assert(gcit != connections.end());
      NodeConnectionsPtr nodeConnections = gcit->second;
      
      for(NodeConnections::const_iterator ncit = nodeConnections->begin();
        ncit != nodeConnections->end();
        ++ncit)
      {
        if (!isMember(matchedVertices, *ncit)) {
          continue;
        }
        
        int matchedNode = *ncit;
      
      //must connect to a matched vertex via an unmatched edge
      
        if (isUnmatchedEdge(unmatchedNode, matchedNode)) {
          addNodeToOddLevel(matchedNode, levels[2*j+1], levels[2*j+2], matchedVertices);
        }
        
        
      }
    }
    
    //If there are no vertices adjacent to S2j, arbitrarily pick an unused vertex and continue in S2j+1.
    if (levels[2*j+1]->size() == 0 && matchedVertices.size() > 0) {
      int toAdd = *matchedVertices.begin();
      addNodeToOddLevel(toAdd, levels[2*j+1], levels[2*j+2], matchedVertices);
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
      
    assert(!TwoJPlusOne->empty() && !TwoJPlusTwo->empty());
    
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
    Edge newMatchEdge = buildEdge(node->parent->value, node->value);
    
    NodeToEdge::const_iterator it = nodeToMatchEdge.find(newMatchEdge.first);
    
    if (it != nodeToMatchEdge.end()) 
    {
      const Edge& edgeToDelete = it->second;
      removeMatchEdge(edgeToDelete);
    }
    
    it = nodeToMatchEdge.find(newMatchEdge.second);
    
    if (it != nodeToMatchEdge.end()) 
    {
      const Edge& edgeToDelete = it->second;
      removeMatchEdge(edgeToDelete);
    }
    
    addMatchEdge(newMatchEdge);
    node = node->parent->parent;
  }
  
  assert(matchEdges.size() == oldSize + 1);
}

bool getY(const NodeHashSet& S_neighbors, 
  const NodeHashSet& T, deque<int>& yNodesToProcess)
{
  for(NodeHashSet::const_iterator it = S_neighbors.begin();
    it != S_neighbors.end();
    ++it)
  {
    if (!isMember(T, *it)) {
      yNodesToProcess.push_back(*it);
      return true;
    }
  }
  
  return false;
}



const NodeConnections& Graph::getConnections(int node) const
{
  GraphConnections::const_iterator connIt = connections.find(node);  
  return *(connIt->second);    
}

void Graph::addToSNeighbors(int xnode, 
  NodeHashSet& S_neighbors,
  AugmentingTree& tree) const
{
  LOG_STR("add neighbors of " << xnode << " to N(S): " << S_neighbors);
  assert(isMember(tree, xnode));
  
  NodePtr xNodePtr = tree.find(xnode)->second;
  
  const NodeConnections& nodeConnections = getConnections(xnode);
  
  for(NodeConnections::const_iterator it = nodeConnections.begin();
    it != nodeConnections.end();
    ++it)
  {
    const int yNode = *it;
    if (!isMember(S_neighbors, yNode)) 
    {
      assert(!isMember(tree, yNode));
      tree.insert(AugmentingTree::value_type(yNode,
        NodePtr(new Node(xNodePtr, yNode))));
      
      S_neighbors.insert(yNode);
    }
  }
  
  LOG_STR("N(S) [" << S_neighbors << "]");
}

//adding matching edge from ynode to xnode
void Graph::addToS(int xnode,
  int ynode,
  NodeHashSet& S,
  NodeHashSet& S_neighbors,
  NodeHashSet& T,
  AugmentingTree& tree) const
{
  LOG_STR("Adding " << xnode << " to S, " << ynode << " to T");
  assert(!isMember(T, ynode));
  T.insert(ynode);
  
  if (isMember(S, xnode)) {
    LOG_STR("Node " << xnode << " already in S");
    return;
  }
  
  assert(!isMember(S, xnode));  
  assert(isMember(tree, ynode));
  
  NodePtr yNodePtr = tree.find(ynode)->second;  
  assert(!isMember(tree, xnode));
  
  tree.insert(AugmentingTree::value_type(xnode,
    NodePtr(new Node(yNodePtr, xnode))));
   
  S.insert(xnode);
  
  addToSNeighbors(xnode, S_neighbors, tree);
}

void Graph::addToS(int xnode, 
  NodeHashSet& S,
  NodeHashSet& S_neighbors,
  AugmentingTree& tree) const
{
  LOG_STR("Adding " << xnode << " to S" );
  assert(!isMember(S, xnode));
  
  assert(!isMember(tree, xnode));
  
  tree.insert(AugmentingTree::value_type(xnode,
    NodePtr(new Node(NodePtr(), xnode))));
  
  S.insert(xnode);
  
  addToSNeighbors(xnode, S_neighbors, tree);   
}



bool Graph::growMatch()
{
  if (freeX.empty()) {
    LOG_STR("No more free x");
    return false;
  }
  
  LOG_OFF();
  LOG_STR("Starting");
  int count = 0;
  
  NodeHashSet S;
  NodeHashSet T;
  NodeHashSet S_neighbors;
  boost::unordered_map<int, NodePtr> augmentingTree;
   
  for(NodeSet::const_iterator freeXit = freeX.begin();
    freeXit != freeX.end();
    ++freeXit)
  {
    const int freeVertexFromX = *freeXit;
    
    addToS(freeVertexFromX, S, S_neighbors, augmentingTree);
    
    deque<int> yNodesToProcess;
    
    while(true)
    {
      if (yNodesToProcess.empty()) {
        getY(S_neighbors, T, yNodesToProcess);
      }
          
      if (yNodesToProcess.empty()) {
        LOG_STR("Done processing, finding another freex");
        break;
      }
      
      assert(!yNodesToProcess.empty());
      
      const int yNode = *yNodesToProcess.begin();
      yNodesToProcess.pop_front();
      
      //LOG_STR("In loop processing y node " << yNode << " yNodesToProcess " << yNodesToProcess);
      LOG_STR("T: " << T);
      LOG_STR("N(S): " << S_neighbors);
      
      if(isMember(freeY, yNode))
      {
        LOG_STR("Augmented match!");
        assert(augmentingTree.find(yNode) != augmentingTree.end());
        augmentMatch(augmentingTree.find(yNode)->second);
        LOG_STR(matchEdges);
        return true;      
      } else {
        assert(!isMember(T, yNode));
        //find matching edge containing y
        LOG_STR("Not an augmenting path, growing augmenting tree: " << yNode);
        NodeToEdge::const_iterator it = nodeToMatchEdge.find(yNode);
        assert(it != nodeToMatchEdge.end());
        const Edge& matchingEdge = it->second;
        assert(matchingEdge.second == yNode);
        
        const int z = matchingEdge.first;
        int oldTsize = T.size();  
        addToS(z, yNode, S, S_neighbors, T, augmentingTree);
        assert(T.size() == oldTsize + 1);
      }      
       
    }
  }
  
  return false;
}  
 


} //namespace

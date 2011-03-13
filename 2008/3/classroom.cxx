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
#define SHOW_TIME 1
#include "util.h" 
#include "bipartite.h"

#include <boost/shared_ptr.hpp>

using namespace std;


//#define LOG_OFF LOG_OFF

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

enum SquareType {
  UNINIT = 14, CHAIR = 0, BROKEN = 1, STUDENT = 3, FORCED_EMPTY = 4  
};

char SquareCh[4] = {'.', 'x', 'i', 's'}; 


class Node;
typedef boost::shared_ptr<Node> NodePtr;

typedef vector<NodePtr> VectorNodes;
typedef vector<VectorNodes> GridNodes;

typedef vector<bool> VecBool;
  typedef vector<VecBool> GridBool;
  typedef boost::shared_ptr<GridBool> GridBoolPtr;

ostream& operator<<( ostream& os, NodePtr rhs);
ostream& operator<<( ostream& os, Node* rhs);

int operator==(NodePtr lhs, Node* rhs);

class Node
{
public:
  
  unsigned int row;
  unsigned int col;
  
  
public:
  
  int label; //1, 2
  
  Node(int row, int col) : row(row), col(col), label(0) {
  }
  
  void disconnectFromNeighbors() //GridNodes& nodes)
  {
    for(vector<NodePtr>::iterator it = connections.begin();
      it != connections.end();
      ++it) 
    {
      NodePtr neighbor = *it;
      vector<NodePtr>::iterator removeMe = find(neighbor->connections.begin(),
        neighbor->connections.end(), this);
      
      assert(removeMe != neighbor->connections.end());
      
      int old_size = neighbor->connections.size();
      
      LOG_OFF();
      LOG_INFO(this);
      LOG_STR_INFO("Removing node");
      LOG_INFO(*removeMe);
      //LOG_OFF();
      
      neighbor->connections.erase(removeMe);
      
      assert(old_size - 1 == neighbor->connections.size());
      
    }
  }
  
  vector<NodePtr> connections;
  
  friend ostream& operator<<( ostream& os, Node* rhs);
  friend ostream& operator<<( ostream& os, NodePtr rhs);
};

int operator==(NodePtr lhs, Node* rhs)
{
  assert(lhs);
  assert(rhs);
  
  return lhs->row == rhs->row && lhs->col == rhs->col;
  
}

ostream& operator<<( ostream& os, Node* rhsPtr)
{
  if (!rhsPtr) {
    return os;
  }
  const Node& rhs = *rhsPtr;
  os << "Node (" << rhs.row << ", " << rhs.col << ") " << endl;
  /*
  os << "Connections: " << rhs.connections.size() << endl;
  for (vector<NodePtr>::const_iterator it = rhs.connections.begin();
    it != rhs.connections.end();
    ++it) {
  os << " Connected Node (" << (*it)->row << ", " << (*it)->col << ") " << endl;
    }
    */
    return os;
}

ostream& operator<<( ostream& os, NodePtr rhsPtr)
{
  os << rhsPtr.get();
  return os;
}

class Grid;
ostream& operator<<(ostream& os, const Grid& grid);

typedef map<NodePtr, int> VisitedMap;
typedef boost::shared_ptr<VisitedMap> VisitedMapPtr;
typedef deque<NodePtr> UnVisitedList;
typedef set<NodePtr> UnvisitedSet;
  
ostream& printMap(ostream& oos, const Grid& grid, const VisitedMap& visitedMap);

  


class Grid
{
public:
  const unsigned int rows, cols;
  
  typedef vector<SquareType> VectorCells;
  typedef vector<VectorCells> VectorRows;
  VectorRows cells;
  
  GridNodes nodes;
  
  unsigned int num_spaces;
  
  Grid(unsigned int rows, unsigned int cols) : rows(rows), cols(cols), cells(rows), nodes(rows, VectorNodes(cols, NodePtr())), num_spaces(0)
  {
    for(VectorRows::iterator it = cells.begin(); it != cells.end(); ++it) {
      *it = VectorCells(cols, UNINIT);      
    }
  }
  
  int getIndex(int row, int col)
  {
    return row * cols + col;
  }
  
  void setSquare(unsigned int row, unsigned int col, char sq)
  {
    
    assert(row >= 0);
    assert(row < rows);
    assert(col >= 0);
    assert(col < cols);
   
    switch(sq) {
    case '.':
      if (cells[row][col] == UNINIT) {
        cells[row][col] = CHAIR;
        nodes[row][col].reset(new Node(row, col));
        ++num_spaces;
      }
      break;
    case 'x':
      if (cells[row][col] == UNINIT) {
        cells[row][col] = BROKEN;
      }
      
      break;
    case 's':
      setStudent(row, col);
      break;
    
    }
  }
  
  void setSquare(int row, int col, SquareType sq)
  {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return;
    }
    
    if (sq == FORCED_EMPTY) {
      assert(cells[row][col] != STUDENT);
      //assert(cells[row][col] != BROKEN);
    }
    
    if (sq == STUDENT) {
      assert(cells[row][col] != FORCED_EMPTY);
      assert(cells[row][col] != BROKEN);
      assert(cells[row][col] != STUDENT);
      assert(cells[row][col] == CHAIR);
    }
    
    if (cells[row][col] != BROKEN) {
      cells[row][col] = sq;
    }
      
  }
  
  SquareType getSquare(int row, int col) const 
  {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      assert(false);
      return UNINIT;
    }
    
    return cells[row][col];
  }
  
  bool isOpen(int row, int col) const 
  {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return false;
    }
    
    if (cells[row][col] == BROKEN || 
      cells[row][col] == STUDENT ||
      cells[row][col] == FORCED_EMPTY 
      ) {
      return false;
    }
    
    assert(cells[row][col] == CHAIR);
    
    return true;
        
  }
  
  void setForcedEmpty(int row, int col) 
  {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return;
    }
    assert(row >= 0);
    assert(row < rows);
    
    assert(col >= 0);
    assert(col < cols);
    
    NodePtr emptyChairNode = nodes[row][col];
    if (emptyChairNode) {
      emptyChairNode->disconnectFromNeighbors();
      setSquare(row, col, FORCED_EMPTY);
      nodes[row][col].reset();
    }
  }
    
  void setStudent(int r, int c)
  {
    setSquare(r, c, STUDENT);
    
    setForcedEmpty(r+1, c-1);
    setForcedEmpty(r+1, c+1);
    setForcedEmpty(r, c+1);
    setForcedEmpty(r, c-1);
    setForcedEmpty(r-1, c-1);
    setForcedEmpty(r-1, c+1);
    
    nodes[r][c].reset();
  }
  
  void connectNodes(NodePtr node1, NodePtr node2)
  {
    assert(node1);
    assert(node2);
    //LOG_INFO("Connecting");
    //cout << *node1 ;
    //LOG_INFO(*node1.get());
    //cout << *node2 ;
    node1->connections.push_back(node2);
    node2->connections.push_back(node1);
    
  }
  
  
  void createNodeConnections() {
    
    for(int i = 0; i < rows*cols; ++i) {
      const int row_i = i / cols;
      const int col_i = i % cols;
      assert(row_i >= 0);
      assert(row_i < rows);
      assert(col_i >= 0);
      assert(col_i < cols);
      if (!getSquare(row_i, col_i) == CHAIR) {
        continue;
      }
      for(int j = i+1; j < rows*cols; ++j) {
        const int row_j = j / cols;
        const int col_j = j % cols;
        assert(row_j >= 0);
        assert(row_j < rows);
        assert(col_j >= 0);
        assert(col_j < cols);
        if (!getSquare(row_j, col_j) == CHAIR) {
          continue; 
        }
        
        if (std::abs(row_i - row_j) <= 1 && std::abs(col_i - col_j) == 1) {
          LOG_INFO("connectNodes calling");
          LOG_INFO(row_i);
          LOG_INFO(col_i);
          LOG_INFO(row_j);
          LOG_INFO(row_i);
          connectNodes(nodes[row_i][col_i], nodes[row_j][col_j]); 
        }
      }
    }
  }
  
  
  bool hasNoChair(int row, int col) {
    if (row < 0 || row >= rows) {
      return true;
    }
    
    if (col < 0 || col >= cols) {
      return true;
    }
    
    if (!nodes[row][col]) {
      return true;
    }
    
    return false;
  }
  
  bool hasNoConnections(int row, int col, int connecting_col) {
    if (hasNoChair(row, col)) {
      return true;
    }
    
    NodePtr node = nodes[row][col];
    
    assert(node);
    
    for (vector<NodePtr>::const_iterator it = node->connections.begin();
          it != node->connections.end();
          ++it)
    {
      if (it->get()->col == connecting_col) {
        return false;
      }
    }
    
    return true;
  }
  
  int do_bipartite() {
    
    Bipartite::Graph graph;
    
     NodePtr startingNode;
     
    for(int i = 0; i < rows*cols; ++i) {
      const int row_i = i / cols;
      const int col_i = i % cols;
      
      if (!startingNode && nodes[row_i][col_i]) {
        startingNode = nodes[row_i][col_i];
        startingNode->label = 1;        
      }
      
    }
    
    queue<NodePtr> nodesToSearch;
    if (startingNode) {
      nodesToSearch.push(startingNode);
      graph.addNode(getIndex(startingNode->row, startingNode->col));
    }
    LOG_OFF();
    while(!nodesToSearch.empty()) {
      NodePtr node = nodesToSearch.front();
      nodesToSearch.pop();
      
      for (vector<NodePtr>::const_iterator it = node->connections.begin();
        it != node->connections.end();
        ++it) 
      {
        NodePtr otherNode = *it;
        //LOG_STR(getIndex(otherNode->row, otherNode->col) << " index");
        
        if (otherNode->label == 0) {
          graph.addNode(getIndex(otherNode->row, otherNode->col));
          nodesToSearch.push(otherNode);
          otherNode->label = 1;
        }
         
        graph.addConnection(
            getIndex(otherNode->row, otherNode->col), 
            getIndex(node->row, node->col));
          
                 
        
      }
    }

    LOG_OFF();
    
    graph.partition();
    
    set<int> studentSet;
    graph.findMaximumIndependantSet(studentSet);
    
    LOG_OFF();
    LOG_STR("Avant bipartite");
    LOG(*this);
    
    for (Bipartite::NodeSet::const_iterator it = studentSet.begin();
      it != studentSet.end();
      ++it)
    {
      int node = *it;
      const int row = node / cols;
      const int col = node % cols;
      
      LOG_STR("Would place a student at " << row << ", " << col);
    }
    
    for (Bipartite::NodeSet::const_iterator it = studentSet.begin();
      it != studentSet.end();
      ++it)
    {
      int node = *it;
      const int row = node / cols;
      const int col = node % cols;
     
      setStudent(row, col);
     
    }
    LOG_OFF();
    LOG_STR("Après bipartite");
    LOG(*this);
    return studentSet.size(); 
  }
  
  
  
  //returns size of larger set 
  int visitNodes()  
  {
    //find a node that has connections
    //int count = searchForEdges();
    int count = 0;
    int local_count = 0;
    
    do { 
      local_count = do_bipartite();
      count += local_count;
      LOG_STR("bipartitie" << *this);
    } while(local_count > 0);
    
    LOG(*this);
    
    LOG_STR("Finit avec search");
    LOG(*this);
    
    return count; 
  }
  
  
  
  int getCost(int row, int col) const
  {
    int cost = 0;
    if (isOpen(row + 1, col + 1) ) {
      ++cost;
    }
    if (isOpen(row + 1, col - 1) ) {
      ++cost;
    }
    if (isOpen(row, col + 1) ) {
      ++cost;
    }    
    if (isOpen(row, col - 1) ) {
      ++cost;
    }
    if (isOpen(row - 1, col - 1) ) {
      ++cost;
    }
    if (isOpen(row - 1, col + 1) ) {
      ++cost;
    }
    return cost;
  }
  
  
  
};

ostream& operator<<(ostream& os, const Grid& grid)
{
  int global_count = 0;
    //os << "\nCols  :  ";
    os << endl;
    for(unsigned int c = 0; c < grid.cols; ++c) {
      os << (c % 10);
    }
    os << endl << endl;
    for(int r = grid.rows - 1; r >= 0; --r) {
      
      int students = 0;
      int empty = 0;
      int connected = 0;
      int broken = 0;
      
      for(unsigned int c = 0; c < grid.cols; ++c) {
        
        if (grid.cells[r][c] == CHAIR && grid.isOpen(r, c)) {
          os << grid.getCost(r, c);
          ++connected;
        } else if (grid.cells[r][c] == FORCED_EMPTY) {
          os << '.';
          ++empty;
        } else {
          os << SquareCh[grid.cells[r][c]];
          if (grid.cells[r][c] == STUDENT) {
            ++students;
          }
          if (grid.cells[r][c] == BROKEN) {
            ++broken;
          }
        }
      }
      global_count += students;
      os << " :" << r;
      
      os << "  s: " << students << " x: " << broken << " .: " << empty << " n: " << connected;
      os << endl;
    }
    
    os << endl << endl << " Global count:  " << global_count << endl << endl;
  return os;    
}

int getSquareFromVisited(int row, int col, const VisitedMap& vMap)
{
  for(VisitedMap::const_iterator it = vMap.begin();
    it != vMap.end();
    ++it) {
  NodePtr node = it->first;
  int sq = it->second;
  if (node->row == row && node->col == col) {
    return sq;
  }
    }
    
    return -1;
}
  
    
typedef deque<VecBool> Perms;

ostream& operator<<(ostream& os, const VecBool& rhs)
{
  for(std::vector<bool>::const_iterator iter = rhs.begin(); iter != rhs.end(); ++iter)
  {
    if (*iter) {
      os << "1";
    } else {
      os << "0";
    }
  }
  return os;
}

void generate_perms(int length)
{
  Perms list;  
  VecBool startingNode(1, true);
  
  
  
  list.push_back(startingNode);
  list.push_back(VecBool(1, false));
    
  LOG(list.front());
  
  while(list.front().size() < length) {
    VecBool perm = list.front();
    list.pop_front();
    
    bool lastElem = *perm.rbegin();
    
    //last element is a chair, can always add a student
    if (!lastElem) {
      VecBool addStudent(perm);
      addStudent.push_back(true);
      list.push_back(addStudent);
    }
    
    //last element was a student, must add a chair
    if (lastElem) {
      perm.push_back(false);
      list.push_back(perm);
    } 
      
    //add another chair next to a chair  
    if (!lastElem 
      && ( perm.size() < 2 || (*(++perm.rbegin()) != false)) //2nd to last must be a student
      && ( length <= 2 || perm.size() != length - 1)
      && ( perm.size() != 1 )
      ) 
    {
      perm.push_back(false);
      list.push_back(perm);
    } 
    
  }
  
  for(Perms::const_iterator it = list.begin(); it != list.end(); ++it) {
    LOG(*it); 
  }
}

void do_test_case(int test_case, ifstream& input)
{
  //generate_perms(10);
  LOG_OFF();
  LOG_STR("abc " << 3 << " . " << 4.15);
  
  LOG_OFF();
  unsigned int R, C;
  input >> R >> C;
  
  Grid grid(R, C);
  int total_placed = 0;
  
  for(int r = R-1; r >= 0; --r) {
    for(unsigned int c = 0; c < C; ++c) {
      char sq;
      input >> sq;
      //LOG_INFO(r);
      //LOG_INFO(R);
      grid.setSquare(r, c, sq);
      if (sq == 's') {
        total_placed++;
      }
    }
  } 
  
  LOG_OFF();
//  LOG(grid);
  LOG_OFF();
  
  grid.createNodeConnections();
   total_placed = grid.visitNodes();


   
  
  LOG_OFF();
  LOG(grid);
  
  //int max_ss = grid.findCompleteGraphOfInverse();
  //LOG(max_ss);
  //printf("Case #%d: %d\n", test_case+1, max_ss);
  printf("Case #%d: %d\n", test_case+1, total_placed);
   
  return;    
}
  

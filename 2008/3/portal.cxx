#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <algorithm>
#include <stack>
#include <limits>
#include <string>
#include <cstring>
#include <bitset>
#include <queue>
#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>
#include <boost/smart_ptr.hpp>
#include <cmath>
#include "util.h" 
#include "tri_logger.hpp"
#include <boost/shared_ptr.hpp>

using namespace std;

#if 0
#define TRI_LOG_STR_DEBUG TRI_LOG_STR
#define TRI_LOG_DEBUG TRI_LOG
#else
#define TRI_LOG_STR_DEBUG(str) do{}while(false)
#define TRI_LOG_DEBUG(str) do{}while(false)
#endif

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
    try {
      //SHOW_TIME_BEGIN(test_case)
      do_test_case(test_case, input);
      //SHOW_TIME_END(test_case)
    } catch(...) {
      error("Error exception caught\n"); 
    }
  }
  
  SHOW_TIME_END(g)
}

enum SquareType {
  EMPTY = 0, WALL = 1, YOU = 2, CAKE = 3  
};

char SquareCh[4] = {'.', '#', 'O', 'X'}; 

/*
enum Direction {
  NORTH, SOUTH, EAST, WEST
};*/
const int NORTH = 0;
const int SOUTH = 1;
const int EAST = 2;
const int WEST = 3;
typedef int Direction;

class Position
{
public:
  unsigned int row;
  unsigned int col;
  Direction dir;
  bool valid;
  
  Position() : valid(false) {
  }
  
  int operator<(const Position& rhs) const {
    if (row != rhs.row) {
      return row < rhs.row;
    }
    if (col != rhs.col) {
      return col < rhs.col;
    }
    if (dir != rhs.dir) {
      return dir < rhs.dir;
    }
    return valid < rhs.valid;
  }
  
  int operator==(const Position& rhs) const {
    return (row == rhs.row && col == rhs.col && 
      dir == rhs.dir && valid == rhs.valid);
  }
  
  int operator!=(const Position& rhs) const {
    return !(*this == rhs);
  }
};

class Node;

ostream& operator<<( ostream& os, const Node& rhs);

class Node
{
public:
  typedef boost::shared_ptr<Node> NodePtr;
  Position yellowPortal;
  Position bluePortal;
  unsigned int row;
  unsigned int col;
  unsigned int depth;
  NodePtr parent;
  
  Node() : depth(0) {
  }
  
  bool samePosition(const Node& rhs) {
    return rhs.row == row && rhs.col == col;
  }
  
  static Node createNode(const Node& curNode, unsigned int new_row, unsigned int new_col)
  {
    Node ret;
    ret.depth = curNode.depth + 1;
    ret.parent.reset(new Node(curNode));
    ret.row = new_row;
    ret.col = new_col;
    ret.yellowPortal = curNode.yellowPortal;
    ret.bluePortal = curNode.bluePortal;
    return ret;
  }
  
  int operator<(const Node& rhs) const {
    if (row != rhs.row) {
      return row < rhs.row;
    }
    if (col != rhs.col) {
      return col < rhs.col;
    }
    if (yellowPortal != rhs.yellowPortal) {
      return yellowPortal < rhs.yellowPortal;
    }
    return bluePortal < rhs.bluePortal;
  }
  
  void printPath(ostream& os) const
  {
    os << "Path: " << endl;
    os << *this << endl;
    NodePtr ptr = parent;
    while(ptr) {
      os << *ptr << endl;
      ptr = ptr->parent;
    }
  }
  
};

ostream& operator<<( ostream& os, const Node& rhs)  {
  os << "Row: " << rhs.row << " Col: " << rhs.col;
  return os;
}

class Grid
{
public:
  unsigned int rows, cols;
  
  unsigned int startingRow, startingCol;
  unsigned int cakeRow, cakeCol;
  
  typedef vector<SquareType> VectorCells;
  typedef vector<VectorCells> VectorRows;
  VectorRows cells;
  

  Grid(unsigned int rows, unsigned int cols) : rows(rows), cols(cols), cells(rows+1)
  {
    for(VectorRows::iterator it = cells.begin(); it != cells.end(); ++it) {
      *it = VectorCells(cols+1, EMPTY); 
    }
  }
  
  void setSquare(unsigned int row, unsigned int col, char sq)
  {
    assert(row >= 1);
    assert(row <= rows);
    assert(col >= 1);
    assert(col <= cols);
   
    switch(sq) {
    case '.':
      cells[row][col] = EMPTY;
      break;
    case '#':
      cells[row][col] = WALL;
      break;
    case 'O':
      cells[row][col] = YOU;
      startingRow = row;
      startingCol = col;
      break;
    case 'X':
      cells[row][col] = CAKE;
      cakeRow = row;
      cakeCol = col;
      break;
    }
  }
  
  Node getStartingNode() const {
    Node startingNode;
    startingNode.row = startingRow;
    startingNode.col = startingCol;
    return startingNode;
  }
  
  Node getCakeNode() const {
    Node cakeNode;
    cakeNode.row = cakeRow;
    cakeNode.col = cakeCol;
    return cakeNode;
  }
  
  bool canMove(const Node& curNode, Direction dir, unsigned int& new_row, unsigned int& new_col) const
  {
    unsigned int cur_row = curNode.row;
    unsigned int cur_col = curNode.col;
    
    new_row = cur_row;
    new_col = cur_col;
    
    assert(cur_row >= 1);
    assert(cur_row <= rows);
    assert(cur_col >= 1);
    assert(cur_col <= cols);
    
    switch(dir) {
    case NORTH:
      if (cur_row == rows) {
        return false;
      }
      ++new_row; 
      break;
    case SOUTH:
      if (cur_row == 1) {
        return false;
      }
      --new_row; 
      break;
    case EAST:
      if (cur_col == cols) {
        return false;
      }
      ++new_col; 
      break;
    case WEST:
      if (cur_col == 1) {
        return false;
      }
      --new_col; 
      break;  
    }
    
    assert(new_row >= 1);
    assert(new_row <= rows);
    assert(new_col >= 1);
    assert(new_col <= cols);
    
    debug("New square %d, %d\n", new_row, new_col);
    return true;
  }
  
  
  
};

ostream& operator<<(ostream& os, const Grid& grid)
{
    os << "\nCols  :  ";
    for(unsigned int c = 0; c < grid.cols; ++c) {
      os << (c % 10);
    }
    os << endl;
    for(unsigned int r = grid.rows; r >= 1; --r) {
      
      os << "Row " << r << " :  ";
      for(unsigned int c = 1; c <= grid.cols; ++c) {
        os << SquareCh[grid.cells[r][c]];
      }
      os << endl;
    }
  return os;    
}

void generateNodes(const Node& curNode, queue<Node>& nodes, const set<Node>& visited, const Grid& grid)
{
  //movement
  for(Direction dir = 0; dir < 4; ++dir) {
    unsigned int new_row, new_col;
    if (grid.canMove(curNode, dir, new_row, new_col) ) {
      Node newNode = Node::createNode(curNode, new_row, new_col);
      if (visited.find(newNode) == visited.end()) {
        TRI_LOG_STR_DEBUG("Pushing Node");
        TRI_LOG_DEBUG(newNode);
        nodes.push(newNode); 
      } else {
        TRI_LOG_STR_DEBUG("Node is used");
        TRI_LOG_DEBUG(newNode);
      }
    }
  }
  
  //portals
  //if (cur
}

void do_test_case(int test_case, ifstream& input)
{
  
  unsigned int R, C;
  input >> R >> C;
  
  TRI_LOG(R);
  TRI_LOG(C);
  
  Grid grid(R, C);
  
  TRI_LOG_STR("Done with grid");
  
  for(unsigned int r = R; r >= 1; --r) {
    for(unsigned int c = 1; c <= C; ++c) {
      char sq;
      input >> sq;
  
    /*  TRI_LOG(r);
      TRI_LOG(c);
      TRI_LOG(sq);*/
      grid.setSquare(r, c, sq);
    }
  }
  
  TRI_LOG(grid);
  
  unsigned int visitedNodes;
  
  queue<Node> nodes;
  set<Node> visited;
  
  nodes.push(grid.getStartingNode());
  
  while(!nodes.empty()) {
    Node curNode = nodes.front();
    nodes.pop();
    
    //TRI_LOG(curNode);
    
    ++visitedNodes;
    
    
    if (curNode.samePosition( grid.getCakeNode()) ) {
      TRI_LOG(visitedNodes);
      curNode.printPath(cout);
      printf("Case #%d: %d\n", test_case+1, curNode.depth);
      return;
    }
    
    visited.insert(curNode);
    generateNodes(curNode, nodes, visited, grid);
  }
  
      
  printf("Case #%d: THE CAKE IS A LIE\n", test_case+1);
   
  return;
    
}
  

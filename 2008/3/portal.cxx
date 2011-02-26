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
#include <deque>
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
#define TRI_LOG_STR_INFO TRI_LOG_STR
#define TRI_LOG_INFO TRI_LOG
#else
#define TRI_LOG_STR_INFO(str) do{}while(false)
#define TRI_LOG_INFO(str) do{}while(false)
#endif   

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


  enum PortalsFired {
    NO_PORTALS,
    YELLOW,
    BLUE,
    BOTH_PORTALS
  };
  
/*
enum Direction {
  NORTH, SOUTH, EAST, WEST
};*/
const char NORTH = 'N';
const char SOUTH = 'S';
const char EAST = 'E';
const char WEST = 'W';
typedef char Direction;

Direction directions[4] = { NORTH, SOUTH, EAST, WEST };

Direction opposite(Direction dir) {
  switch(dir) {
    case NORTH:
      return SOUTH;      
      break;
    case SOUTH:
      return NORTH; 
      break;
    case EAST:
      return WEST; 
      break;
    case WEST:
      return EAST; 
      break;  
    }
    throw 3;
}

class Position
{
public:
  unsigned int row;
  unsigned int col;
  Direction dir;
  bool valid;
  
  Position() : valid(false) {
  }
  
  void getOutputRowCol(unsigned int& out_row, unsigned int& out_col) const
  {
    switch(dir) {
    case NORTH:
      out_row = row + 1; 
      out_col = col;
      break;
    case SOUTH:
      out_row = row - 1;
      out_col = col;
      break;
    case EAST:
      out_col = col + 1;
      out_row = row;
      break;
    case WEST:
      out_col = col - 1;
      out_row = row;
      break;  
    }
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

ostream& operator<<( ostream& os, const Position& rhs)
{
  if (rhs.valid) { 
    os << "(" << rhs.row << ", " << rhs.col << ") Direction: " << rhs.dir;
  } else {
    os << "None";
  }
  return os;
}

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
  
  static Node createNode(const Node& curNode, const Position& newYellowPortal, const Position& newBluePortal)
  {
    Node ret;
    
    //Shooting portals costs nothing
    ret.depth = curNode.depth;
    ret.parent.reset(new Node(curNode));
    ret.row = curNode.row;
    ret.col = curNode.col;
    ret.yellowPortal = newYellowPortal;
    ret.bluePortal = newBluePortal;
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
  os << "Depth: " << rhs.depth << " Row: " << rhs.row << " Col: " << rhs.col << " Blue portal: " << rhs.bluePortal << " Yellow portal: " << rhs.yellowPortal;
  return os;
}

class Grid;
ostream& operator<<(ostream& os, const Grid& grid);

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
      *it = VectorCells(cols+1, WALL); 
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
      ++new_row; 
      break;
    case SOUTH:
      --new_row; 
      break;
    case EAST:
      ++new_col; 
      break;
    case WEST:
      --new_col; 
      break;  
    }
    
    if (curNode.bluePortal.valid && curNode.yellowPortal.valid) {
      if (curNode.bluePortal.row == new_row && curNode.bluePortal.col == new_col) {
        TRI_LOG_STR_DEBUG("Walking through blue portal");
        TRI_LOG_DEBUG(curNode.bluePortal);
        TRI_LOG_DEBUG(curNode.yellowPortal);
        curNode.yellowPortal.getOutputRowCol(new_row, new_col);
        TRI_LOG_DEBUG(new_row);
        TRI_LOG_DEBUG(new_col);
        assert(new_row >= 1);
        assert(new_row <= rows);
        assert(new_col >= 1);
        assert(new_col <= cols);
      } else 
      if (curNode.yellowPortal.row == new_row && curNode.yellowPortal.col == new_col) {
        //Grid& grid = *this;
        TRI_LOG_DEBUG(*this);
        TRI_LOG_STR_DEBUG("Walking through yellow portal");
        TRI_LOG_DEBUG(curNode.yellowPortal);
        TRI_LOG_DEBUG(curNode.bluePortal);
        curNode.bluePortal.getOutputRowCol(new_row, new_col);
        TRI_LOG_DEBUG(new_row);
        TRI_LOG_DEBUG(new_col);
        assert(new_row >= 1);
        assert(new_row <= rows);
        assert(new_col >= 1);
        assert(new_col <= cols);
      }
      
    }
    
    if(new_row < 1 || new_row > rows || new_col < 1 || new_col > cols) {
      return false;
    }
    
    if (cells[new_row][new_col] == WALL) {
      return false;
    }
    
    TRI_LOG_STR_DEBUG("New square\n");
    TRI_LOG_DEBUG(new_row);
    TRI_LOG_DEBUG(new_col);
    return true;
  }
  
  
  PortalsFired canFirePortal(const Node& curNode, Direction dir, Position& newYellowPortal, Position& newBluePortal) const
  {
    unsigned int cur_row = curNode.row;
    unsigned int cur_col = curNode.col;
    
    int delta_row = 0;
    int delta_col = 0;
    
    switch(dir) {
    case NORTH:
      delta_row = 1;      
      break;
    case SOUTH:
      delta_row = -1; 
      break;
    case EAST:
      delta_col = 1; 
      break;
    case WEST:
      delta_col = -1; 
      break;  
    }
    
    bool foundWall = false;
    
    while(cur_row >= 1 && cur_row <= rows &&
      cur_col >= 1 && cur_col <= cols) 
    {
      if(cells[cur_row][cur_col] == WALL) {
        foundWall = true;
        break; 
      }
      
      cur_row += delta_row;
      cur_col += delta_col;
    }
    
    if (!foundWall) {
      //cur_row += delta_row;
      //cur_col += delta_col;
    }
    
    //cur_row / cur_col is now where the portal is
    
    Position newPortal;
    newPortal.row = cur_row;
    newPortal.col = cur_col;
    newPortal.dir = opposite(dir);
    newPortal.valid = true;
    
    if (curNode.bluePortal == newPortal || curNode.yellowPortal == newPortal) {
      return NO_PORTALS;
    }
      
    if (!curNode.bluePortal.valid) {
      //use the blue first
      newBluePortal = newPortal;
      return BLUE;
    }
    
    if (!curNode.yellowPortal.valid) {
      //use the blue first
      newYellowPortal = newPortal;
      return YELLOW;
    }
    
    newBluePortal = newPortal;
    newYellowPortal = newPortal;
    return BOTH_PORTALS;
    
  }
  
  
  
};

ostream& operator<<(ostream& os, const Grid& grid)
{
    os << "\nCols  :  ";
    for(unsigned int c = 1; c <= grid.cols; ++c) {
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

void generateNodes(const Node& curNode, deque<Node>& nodes, const set<Node>& visited, const Grid& grid)
{
  
  
  //portals
  //if (cur
  for(int dirIdx = 0; dirIdx < 4; ++dirIdx) {
    Direction dir = directions[dirIdx];
    Position newYellowPortal, newBluePortal;
    newYellowPortal = curNode.yellowPortal;
    newBluePortal = curNode.bluePortal;
    PortalsFired pf = grid.canFirePortal(curNode, dir, newYellowPortal, newBluePortal); 
    if (pf != NO_PORTALS) {
      Node newNode;
      if (pf == BOTH_PORTALS) {
        TRI_LOG_STR_DEBUG("Firing both portals");
        newNode = Node::createNode(curNode, newYellowPortal, curNode.bluePortal);
        if (visited.find(newNode) == visited.end()) {
          TRI_LOG_STR_DEBUG("Pushing portal Node");
          TRI_LOG_DEBUG(newNode);
          nodes.push_front(newNode); 
        } else {
          TRI_LOG_STR_DEBUG("Portal Node is used");
          TRI_LOG_DEBUG(newNode);
        }
        
        newNode = Node::createNode(curNode, curNode.yellowPortal, newBluePortal);
        if (visited.find(newNode) == visited.end()) {
          TRI_LOG_STR_DEBUG("Pushing portal Node");
          TRI_LOG_DEBUG(newNode);
          nodes.push_front(newNode); 
        } else {
          TRI_LOG_STR_DEBUG("Portal Node is used");
          TRI_LOG_DEBUG(newNode);
        }
      } else {
        newNode = Node::createNode(curNode, newYellowPortal, newBluePortal);
        if (visited.find(newNode) == visited.end()) {
          TRI_LOG_STR_DEBUG("Pushing portal Node");
          TRI_LOG_DEBUG(newNode);
          nodes.push_front(newNode); 
        } else {
          TRI_LOG_STR_DEBUG("Portal Node is used");
          TRI_LOG_DEBUG(newNode);
        }
      }
    }
  }
  
  //movement
  for(int dirIdx = 0; dirIdx < 4; ++dirIdx) {
    Direction dir = directions[dirIdx];
    unsigned int new_row, new_col;
    if (grid.canMove(curNode, dir, new_row, new_col) ) {
      Node newNode = Node::createNode(curNode, new_row, new_col);
      if (visited.find(newNode) == visited.end()) {
        TRI_LOG_STR_DEBUG("Pushing Node");
        TRI_LOG_DEBUG(newNode);
        nodes.push_back(newNode); 
      } else {
        TRI_LOG_STR_DEBUG("Node is used");
        TRI_LOG_DEBUG(newNode);
      }
    }
  }
}

void do_test_case(int test_case, ifstream& input)
{
  
  unsigned int R, C;
  input >> R >> C;
  
  TRI_LOG_INFO(R);
  TRI_LOG_INFO(C);  
  
  Grid grid(R, C);
  
  TRI_LOG_STR_INFO("Done with grid");
  
  for(unsigned int r = R; r >= 1; --r) {
    for(unsigned int c = 1; c <= C; ++c) {
      char sq;
      input >> sq;
  
    /*  TRI_LOG_INFO(r);
      TRI_LOG_INFO(c);
      TRI_LOG_INFO(sq);*/
      grid.setSquare(r, c, sq);
    }
  }
  
  TRI_LOG_INFO(grid);
  
  unsigned int visitedNodes;
  
  deque<Node> nodes;
  set<Node> visited;
  
  nodes.push_back(grid.getStartingNode());
  
  while(!nodes.empty()) {
    Node curNode = nodes.front();
    nodes.pop_front();
    
    TRI_LOG_STR_DEBUG("Poping off node");
    TRI_LOG_DEBUG(curNode);
    
    ++visitedNodes;
    
    
    if (curNode.samePosition( grid.getCakeNode()) ) {
      //TRI_LOG(visitedNodes);
      //curNode.printPath(cout);
      printf("Case #%d: %d\n", test_case+1, curNode.depth);
      return;
    }
    
    visited.insert(curNode);
    generateNodes(curNode, nodes, visited, grid);
  }
  
      
  printf("Case #%d: THE CAKE IS A LIE\n", test_case+1);
   
  return;
    
}
  

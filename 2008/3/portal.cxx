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

#define INFO 0
#if INFO
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
typedef boost::shared_ptr<Node> NodePtr;

ostream& operator<<( ostream& os, const Node& rhs);

class Node
{
public:
  
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
    return ret;
  }
  
  
  int operator<(const Node& rhs) const {
    if (row != rhs.row) {
      return row < rhs.row;
    }
    return col < rhs.col;
    
  
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
  os << "Depth: " << rhs.depth << " Row: " << rhs.row << " Col: " << rhs.col ;
  
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
  

  Grid(unsigned int rows, unsigned int cols) : rows(rows), cols(cols), cells(rows+2)
  {
    for(VectorRows::iterator it = cells.begin(); it != cells.end(); ++it) {
      *it = VectorCells(cols+2, WALL); 
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
    
    
    if(new_row < 1 || new_row > rows || new_col < 1 || new_col > cols) {
      return false;
    }
    
    if (cells[new_row][new_col] == WALL) {
      return false;
    }
    /*
    TRI_LOG_STR_DEBUG("New square\n");
    TRI_LOG_DEBUG(new_row);
    TRI_LOG_DEBUG(new_col);*/
    return true;
  }
  
  
  void firePortal(const Node& curNode, Direction dir, Position& newPortal) const
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
    
    //cur_row / cur_col is now where the portal is
    
    newPortal.row = cur_row;
    newPortal.col = cur_col;
    newPortal.dir = opposite(dir);
    newPortal.valid = true;
    
    
  }
  
  bool nextToWall(const Node& curNode) const {
    for(int deltaRow = -1; deltaRow <= 1; ++deltaRow) {
      if (cells[curNode.row + deltaRow][curNode.col] == WALL) {
        return true;
      }
    }

    for(int deltaCol = -1; deltaCol <= 1; ++deltaCol) {
      if (cells[curNode.row][curNode.col + deltaCol] == WALL) {
        return true;
      }
    }
    return false;
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

void addNode(const Node& newNode, deque<Node>& nodes, set<Node>& visited)
{
  if (visited.find(newNode) == visited.end()) {
    TRI_LOG_STR_DEBUG("Pushing portal Node");
    TRI_LOG_DEBUG(newNode);
    nodes.push_back(newNode); 
    visited.insert(newNode);
  } else {
    TRI_LOG_STR_DEBUG("Portal Node is used");
    TRI_LOG_DEBUG(newNode);
  }
}

void generateNodes(const Node& curNode, deque<Node>& nodes, set<Node>& possibleNodes, set<Node>& visited, const Grid& grid)
{
  //portals
  bool isNextToWall = grid.nextToWall(curNode);
  
  if (isNextToWall) {
    //TRI_LOG_STR_INFO("Next to wall");
    //TRI_LOG_STR(curNode);
    for(set<Node>::iterator it = possibleNodes.begin(); it != possibleNodes.end(); ) {
      Node nodeToAdd = *it;
      nodeToAdd.depth = curNode.depth + 1;
      bool foundCommonParent = false;
      NodePtr p = curNode.parent;
      while(p) {
        if (p->samePosition(*nodeToAdd.parent)) {
          foundCommonParent = true;
          break;
        }
        p = p->parent;
      }
      if (foundCommonParent) {
        NodePtr aparent(new Node(curNode));
        nodeToAdd.parent = aparent;
        TRI_LOG_STR_INFO("Adding possible" );
        addNode(nodeToAdd, nodes, visited);
        possibleNodes.erase(it++);
        continue;
      }
      
      ++it;
    }
    //anything we can't use we can throw out...
    possibleNodes.clear();
  }
  
  for(int dirIdx = 0; dirIdx < 4; ++dirIdx) {
    Direction dir = directions[dirIdx];
    Position newPortal;
    grid.firePortal(curNode, dir, newPortal); 
    Node newNode;
    unsigned int new_row, new_col;
      
    newPortal.getOutputRowCol(new_row, new_col);
    newNode = Node::createNode(curNode, new_row, new_col);          
 
    if (isNextToWall) {
      addNode(newNode, nodes, visited);
    } else {
      possibleNodes.insert(newNode); 
    }
     
    
  }
  
  //movement
  for(int dirIdx = 0; dirIdx < 4; ++dirIdx) {
    Direction dir = directions[dirIdx];
    unsigned int new_row, new_col;
    if (grid.canMove(curNode, dir, new_row, new_col) ) {
      Node newNode = Node::createNode(curNode, new_row, new_col);
      addNode(newNode, nodes, visited);
    }
  }
}

void do_test_case(int test_case, ifstream& input)
{
  
  unsigned int R, C;
  input >> R >> C;
  
  Grid grid(R, C);
  
  for(unsigned int r = R; r >= 1; --r) {
    for(unsigned int c = 1; c <= C; ++c) {
      char sq;
      input >> sq;
  
      grid.setSquare(r, c, sq);
    }
  }
  
  TRI_LOG_INFO(grid);
  
  unsigned int visitedNodes = 0;
  
  deque<Node> nodes;
  set<Node> possibleNodes;
  set<Node> visited;
  
  nodes.push_back(grid.getStartingNode());
  
  while(!nodes.empty()) {
    Node curNode = nodes.front();
    nodes.pop_front();
    
    //TRI_LOG_STR_DEBUG("Poping off node");
    //TRI_LOG(curNode);
    
    ++visitedNodes;
    if (visitedNodes % 20000 == 0) {
        //TRI_LOG(visitedNodes);
      }
    
    if (curNode.samePosition( grid.getCakeNode()) ) {
      //
      #if INFO 
        curNode.printPath(cout);      
      #endif
      
      printf("Case #%d: %d\n", test_case+1, curNode.depth);
      return;
    }
    
    generateNodes(curNode, nodes, possibleNodes, visited, grid);
  }
  
      
  #if INFO 
  TRI_LOG(visitedNodes);
  #endif
  printf("Case #%d: THE CAKE IS A LIE\n", test_case+1);
   
  return;
    
}
  

#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <deque>
#include <time.h>
#include <assert.h>
#include <boost/smart_ptr.hpp>
#include "util.h" 

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
    try {
      do_test_case(test_case, input);
    } catch(...) {
      error("Error exception caught\n"); 
    }
  }
  
  SHOW_TIME_END(g)
}

enum SquareType {
  CHAIR = 0, BROKEN = 1, INVALID = 2, STUDENT = 3  
};

char SquareCh[4] = {'.', 'x', 'i', 's'}; 


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
  
  typedef vector<SquareType> VectorCells;
  typedef vector<VectorCells> VectorRows;
  VectorRows cells;
  

  Grid(unsigned int rows, unsigned int cols) : rows(rows), cols(cols), cells(rows)
  {
    for(VectorRows::iterator it = cells.begin(); it != cells.end(); ++it) {
      *it = VectorCells(cols, CHAIR); 
    }
  }
  
  void setSquare(unsigned int row, unsigned int col, char sq)
  {
    
    assert(row >= 0);
    assert(row < rows);
    assert(col >= 0);
    assert(col < cols);
   
    switch(sq) {
    case '.':
      cells[row][col] = CHAIR;
      break;
    case 'x':
      cells[row][col] = BROKEN;
      break;
    
    }
  }
  
  void setSquare(int row, int col, SquareType sq)
  {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return;
    }
    
    if (cells[row][col] != BROKEN) {
      cells[row][col] = sq;
    }
      
  }
  
  SquareType getSquare(int row, int col) const 
  {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return INVALID;
    }
    
    return cells[row][col];
  }
  
  bool isOpen(int row, int col) const 
  {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return false;
    }
    
    if (cells[row][col] == BROKEN || 
      cells[row][col] == INVALID || 
      cells[row][col] == STUDENT ) {
      return false;
    }
    
    if (getSquare(row + 1, col + 1) == STUDENT) {
      return false;
    }
    if (getSquare(row + 1, col - 1) == STUDENT) {
      return false;
    }
    if (getSquare(row, col + 1) == STUDENT) {
      return false;
    }
    if (getSquare(row, col - 1) == STUDENT) {
      return false;
    }
    
    return true;
        
  }
    
  int getLowestCost() const
  {
    int lowestCost = 50;
    for(int r = 0; r < rows; ++r) {
      for(int c = 0; c < cols; ++c) {
        if (isOpen(r, c)) {
          //LOG(r);
          //LOG(c);
          //LOG(lowestCost);
          lowestCost = min<int>(lowestCost, getCost(r, c));   
        }
      }
    }
    return lowestCost;
  }
  
  int placeStudents(int cost) {
    int placed = 0;
    for(int r = 0; r < rows; ++r) {
      for(int c = 0; c < cols; ++c) {
        if (isOpen(r, c) && getCost(r, c) <= cost) {
          ++placed;
          setSquare(r, c, STUDENT);
          setSquare(r+1, c-1, INVALID);
          setSquare(r+1, c+1, INVALID);
          setSquare(r, c+1, INVALID);
          setSquare(r, c-1, INVALID);
        }
      }
    }
    return placed;
  }
  
  int getCost(int row, int col) const
  {
    int cost = 0;
    if (isOpen(row + 1, col + 1) ) {
      ++cost;
    }
    if (isOpen(row, col + 1) ) {
      ++cost;
    }
    if (isOpen(row + 1, col - 1) ) {
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
    //os << "\nCols  :  ";
    //os << endl;
    for(unsigned int c = 0; c < grid.cols; ++c) {
      //os << (c % 10);
    }
    os << endl << endl;
    for(int r = grid.rows - 1; r >= 0; --r) {
      
      //
      for(unsigned int c = 0; c < grid.cols; ++c) {
        
        if (grid.cells[r][c] == CHAIR && grid.isOpen(r, c)) {
          os << grid.getCost(r, c);
        } else {
          os << SquareCh[grid.cells[r][c]];
        }
      }
      
      //os << "Row " << r << " :  ";
      os << endl;
    }
  return os;    
}

void do_test_case(int test_case, ifstream& input)
{
  
  unsigned int R, C;
  input >> R >> C;
  
  Grid grid(R, C);
  
  for(int r = R-1; r >= 0; --r) {
    for(unsigned int c = 0; c < C; ++c) {
      char sq;
      input >> sq;
      //LOG(r);
      //LOG(R);
      grid.setSquare(r, c, sq);
    }
  }
  
  LOG_INFO(grid);
  
  int lowest_cost = grid.getLowestCost();
  int placed = 1;
  int total_placed = 0;
  
  while(placed > 0) {
    LOG(lowest_cost);
    placed = grid.placeStudents(lowest_cost);
    LOG(placed);
    LOG(grid);
    
    lowest_cost = grid.getLowestCost();
    total_placed += placed;
    //LOG(total_placed);
  }
  
  printf("Case #%d: %d\n", test_case+1, total_placed);
   
  return;    
}
  

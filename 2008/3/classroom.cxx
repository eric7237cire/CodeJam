#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <deque>
#include <queue>
#include <time.h>
#include <assert.h>
#include <boost/smart_ptr.hpp>
#include "util.h" 

#include <boost/shared_ptr.hpp>

using namespace std;

//#define LOG_ON LOG_OFF

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
  UNINIT = 4, CHAIR = 0, BROKEN = 1, INVALID = 2, STUDENT = 3  
};

char SquareCh[4] = {'.', 'x', 'i', 's'}; 


class Node;
typedef boost::shared_ptr<Node> NodePtr;

typedef vector<NodePtr> VectorNodes;
typedef vector<VectorNodes> GridNodes;

ostream& operator<<( ostream& os, NodePtr rhs);
ostream& operator<<( ostream& os, Node* rhs);

int operator==(NodePtr lhs, Node* rhs);

class Node
{
public:
  
  unsigned int row;
  unsigned int col;
  
  
public:
  
  unsigned int label; //1, 2
  
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
  os << "Connections: " << rhs.connections.size() << endl;
  for (vector<NodePtr>::const_iterator it = rhs.connections.begin();
    it != rhs.connections.end();
    ++it) {
  os << " Connected Node (" << (*it)->row << ", " << (*it)->col << ") " << endl;
    }
    
    return os;
}

ostream& operator<<( ostream& os, NodePtr rhsPtr)
{
  os << rhsPtr.get();
  return os;
}

class Grid;
ostream& operator<<(ostream& os, const Grid& grid);

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
          //LOG_INFO(r);
          //LOG_INFO(c);
          //LOG_INFO(lowestCost);
          lowestCost = min<int>(lowestCost, getCost(r, c));   
        }
      }
    }
    return lowestCost;
  }
  
  void setStudent(int r, int c)
  {
    setSquare(r, c, STUDENT);
    setSquare(r+1, c-1, INVALID);
    setSquare(r+1, c+1, INVALID);
    setSquare(r, c+1, INVALID);
    setSquare(r, c-1, INVALID);
  }
  
  int placeStudents(int cost) {
    int placed = 0;
    vector<pair<int, int> > to_set;
    
    for(int r = 0; r < rows; ++r) {
      for(int c = 0; c < cols; ++c) {
        if (isOpen(r, c) && getCost(r, c) <= cost) {
          ++placed;
          to_set.push_back(make_pair(r, c));
        }
      }
    }
    
    for(int i = 0; i < placed; ++i) {
      setStudent(to_set[i].first, to_set[i].second);
      return 1;
    }
    return placed;
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
  
  int fillEdge(int startRow, int endRow, int studentCol, int emptyCol) {
    LOG_OFF();
    int local_count = 0;
    LOG_STR("Filling edge");
    LOG(startRow);
    LOG(endRow);
    
    LOG(studentCol);
    LOG(emptyCol);
    
    assert(startRow <= endRow);
    assert(startRow >= 0);
    assert(startRow < rows);
    assert(endRow >= 0);
    assert(endRow < rows);
    
    assert(studentCol >= 0);
    assert(studentCol < cols);
    
    for(int r = startRow; r <= endRow; ++r) {
        NodePtr studentNode = nodes[r][studentCol];
        
          
        if (studentNode) {
          studentNode->disconnectFromNeighbors();
          setStudent(r, studentCol);
          ++local_count;
        }
        
        if (emptyCol >= 0 && emptyCol < cols) {
          NodePtr emptyChairNode = nodes[r][emptyCol];
          if (emptyChairNode) {
            emptyChairNode->disconnectFromNeighbors();
          }
          nodes[r][emptyCol].reset();
        }
        
        nodes[r][studentCol].reset();
        
        
    }
    
    LOG_OFF();
    
    return local_count;
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
  
  int searchForEdges() {
    LOG_ON();
    int potential_student_count = 0;
    int potential_empty_chair_count = 0;
    bool invalidUntilNextBlock = false;
    int start_row = 0;
    int count = 0;
    
    for(int c = 0; c < cols; ++c) {
      for(int col_offset = -1; col_offset <= 1; col_offset += 2) {
        start_row = 0;
        invalidUntilNextBlock = false;
        const int student_col = c;
        const int all_blocked_col = c + col_offset;
        const int empty_chair_col = c - col_offset;
        potential_student_count = 0;
        potential_empty_chair_count = 0;
        
      for(int r = 0; r < rows; ++r) {
        //r, c must have no connections towards c-col_offset
        
        
        
        //r, c must have f
        LOG_OFF();
        
        if (c == 9) {
          //LOG_ON();
        }
        
        LOG(r);
        LOG(c);
        LOG(student_col);
        LOG(empty_chair_col);
        LOG(all_blocked_col);
        LOG(hasNoChair(r, c));
        LOG(hasNoChair(r, student_col));
        LOG(hasNoChair(r, empty_chair_col));
        LOG(potential_student_count);
        
        if(hasNoChair(r, student_col) 
          && hasNoChair(r, empty_chair_col)) {
          //LOG(r);
          //LOG(c);
          LOG_STR("seperator");
          if (!invalidUntilNextBlock && potential_student_count >= potential_empty_chair_count && potential_student_count > 0) {
            count += fillEdge(start_row, r-1, student_col, empty_chair_col);
          }
          potential_student_count = 0;
          potential_empty_chair_count = 0;
          start_row = r + 1;
          invalidUntilNextBlock = false;
          continue;
        } else {
          
        }
        
        if (invalidUntilNextBlock) {
          continue;
        }
        
        if (!hasNoConnections(r, c, all_blocked_col)) {
          //StackLogSwitch s(false);
          LOG_STR("Invalid!");
          LOG(r);
          LOG(c);
          LOG(col_offset);
          LOG(hasNoChair(r, -1));
          LOG(!hasNoChair(r, c - col_offset));
          potential_student_count = 0;
          potential_empty_chair_count = 0;
          invalidUntilNextBlock = true;
          continue;
        }
        
        //has a chair
        if (!hasNoChair(r, student_col)) {
          ++potential_student_count;
        }
        
        //next door
        if (!hasNoChair(r, empty_chair_col)) {
          ++potential_empty_chair_count;
        }
      }
      
        if (!invalidUntilNextBlock && potential_student_count >= potential_empty_chair_count && potential_student_count > 0) {
            count += fillEdge(start_row, rows-1, student_col, empty_chair_col);
          }
      }
    }
    
    LOG_OFF();
    return count;
  }
  
  int searchForIsolated()
  {
    int local_count = 0;
       
     for(int i = 0; i < rows*cols; ++i) {
      const int row_i = i / cols;
      const int col_i = i % cols;
      if (nodes[row_i][col_i]) {
        //cout << row_i << " " << col_i << nodes[row_i][col_i] << endl;
      }
      
      if (nodes[row_i][col_i] && nodes[row_i][col_i]->connections.size() == 0) {
        ++local_count;
        setStudent(row_i, col_i);
        nodes[row_i][col_i].reset();
      }
      
      //if connections == 1, take it out of graph, remove connections to other nodes
      if (nodes[row_i][col_i] && nodes[row_i][col_i]->connections.size() == 1) {
        NodePtr connectedNodeToRemove = nodes[row_i][col_i]->connections[0];
        NodePtr isolatedNodeToRemove = nodes[row_i][col_i];
        assert(isolatedNodeToRemove->row == row_i);
        assert(isolatedNodeToRemove->col == col_i);
        assert(abs(static_cast<int>(connectedNodeToRemove->row) - row_i) <= 1);
        assert(abs(static_cast<int>(connectedNodeToRemove->col) - col_i) == 1);
          
        const int remove_row =  nodes[row_i][col_i]->connections[0]->row;
        const int remove_col =  nodes[row_i][col_i]->connections[0]->col;
        connectedNodeToRemove->disconnectFromNeighbors();
        
        //LOG_ON();
        LOG_STR_INFO("Removing single connection");
        LOG_INFO(row_i);
        LOG_INFO(col_i);
        LOG_STR_INFO("and");
        LOG_INFO(remove_row);
        LOG_INFO(remove_col);
        //LOG_OFF();
        setStudent(row_i, col_i);
        nodes[remove_row][remove_col].reset();
        nodes[row_i][col_i].reset();
        ++local_count;
      }
     }
      
    return local_count;
    
  }
  
  //returns size of larger set 
  int visitNodes()  {
     
     //find a node that has connections
     //int count = searchForEdges();
     int count = 0;
     int local_count = 0;
     
     LOG_ON();
     int s_count = 0;
     
     do {
       s_count = count;
       
       
       do {
         local_count = searchForEdges();
       LOG_ON();
       LOG_STR("Edges");
       LOG(*this);
       count += local_count;
       
       
       local_count = searchForIsolated();
       LOG_ON();
       LOG_STR("Isolated");
       LOG(*this);
       count += local_count;
       } while (local_count > 0);
       
       
       
       
     } while (s_count < count);
     
     LOG_STR("Done with initial searches");
     LOG(*this);
     LOG_OFF();
     int s1_count = 0;
     int s2_count = 0;
     
     local_count = 0;
     
     LOG_ON();
     LOG_STR("After single nodes");
     LOG(*this);
     LOG_OFF();
     
     
     NodePtr startingNode;
     do 
     {
       startingNode.reset();
       s1_count = 0;
       s2_count = 0;
     
      for(int i = 0; i < rows*cols; ++i) {
        const int row_i = i / cols;
        const int col_i = i % cols;
        if (nodes[row_i][col_i]) {
          //cout << row_i << " " << col_i << nodes[row_i][col_i] << endl;
        }
        
        
        if (!startingNode && nodes[row_i][col_i] && nodes[row_i][col_i]->connections.size() > 0) {
          startingNode = nodes[row_i][col_i];
          startingNode->label = 1;        
        }
        
      }
      
      queue<NodePtr> nodesToSearch;
      if (startingNode) {
        nodesToSearch.push(startingNode);
      }
      
      vector<NodePtr> s1_nodes;
      vector<NodePtr> s2_nodes;
      
      while(!nodesToSearch.empty()) {
        NodePtr node = nodesToSearch.front();
        nodesToSearch.pop();
        nodes[node->row][node->col].reset();
        LOG_INFO(node);
        if (node->label == 1) {
          s1_count ++;
          s1_nodes.push_back(node);
          LOG_INFO("Logging 1");
          LOG_INFO(node);
        } else if (node->label == 2) {
          LOG_INFO("Logging 2");
          LOG_INFO(node);
          s2_nodes.push_back(node);
          s2_count++;
        } else {
          throw 3;
        }
        
        for (vector<NodePtr>::const_iterator it = node->connections.begin();
          it != node->connections.end();
          ++it) 
        {
          if (it->get()->label == 0) {
            
            it->get()->label = (node->label == 1) ? 2 : 1;
            nodesToSearch.push(*it);        
          }
        }
      }
      
      if (s1_count >= s2_count) {
         for (vector<NodePtr>::const_iterator it = s1_nodes.begin();
          it != s1_nodes.end();
          ++it)
         {
           setStudent(it->get()->row, it->get()->col);
         }
      } else {
        for (vector<NodePtr>::const_iterator it = s2_nodes.begin();
          it != s2_nodes.end();
          ++it)
         {
           setStudent(it->get()->row, it->get()->col);
         }
      }
      
      count += max(s1_count, s2_count);
    
     } while(startingNode);
    
    return count; 
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
        } else if (grid.cells[r][c] == INVALID) {
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
      
      os << " :" << r;
      
      os << "  s: " << students << " x: " << broken << " .: " << empty << " n: " << connected;
      os << endl;
    }
  return os;    
}

void do_test_case(int test_case, ifstream& input)
{
  
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
  
  LOG_ON();
  LOG(grid);
  LOG_OFF();
  
  int lowest_cost = grid.getLowestCost();
    LOG_DEBUG(lowest_cost);
    //placed = grid.placeStudents(lowest_cost);
    //LOG_DEBUG(placed);
   grid.createNodeConnections();
   total_placed = grid.visitNodes();
    LOG(grid);
    
    lowest_cost = grid.getLowestCost();
    //LOG_INFO(total_placed);
  
  LOG_ON();
  LOG(grid);
  printf("Case #%d: %d\n", test_case+1, total_placed);
   
  return;    
}
  

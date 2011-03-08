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
  
  Node(int row, int col) : row(row), col(col), label(-1) {
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

class BoardAssignment;
  typedef boost::shared_ptr<BoardAssignment> BoardAssignmentPtr;
  typedef deque<BoardAssignmentPtr> BoardAssignmentQueue;

  typedef map<NodePtr, int> VisitedMap;
  typedef boost::shared_ptr<VisitedMap> VisitedMapPtr;
  typedef deque<NodePtr> UnVisitedList;
  typedef set<NodePtr> UnvisitedSet;
  
  class BoardAssignment
  {
    public:
      Grid& grid;
      
    private:
    VisitedMap visitedMap;
    UnvisitedSet unvisitedSet;
    int score;
    
  public:
    void addVisited(NodePtr node, int assignment)
    {
      this->visitedMap.insert(VisitedMap::value_type(node, assignment));
      
      UnvisitedSet::iterator it = unvisitedSet.find(node);
      if (it != unvisitedSet.end()) {
        unvisitedSet.erase(it);
      }
      
      for (vector<NodePtr>::const_iterator it = node->connections.begin();
        it != node->connections.end();
        ++it)
        {
          NodePtr connectedNode = *it;
          VisitedMap::const_iterator visited_it = visitedMap.find(connectedNode);
          if (visited_it == visitedMap.end()) {
            LOG_OFF();
            LOG_STR("Found non visited node " << connectedNode);
            unvisitedSet.insert(connectedNode);            
          }
        }
    }
    //current node assigning
    
    BoardAssignment(Grid& grid, const VisitedMap& visitedMap, const UnvisitedSet& unvisitedSet, int score ) 
    : grid(grid), visitedMap(visitedMap), unvisitedSet(unvisitedSet), score(score) 
    {
      
    }
    
    void GenerateOtherBoardAssignments(BoardAssignmentQueue& queue) const;
    static void GenerateInitialBoardAssignments(BoardAssignmentQueue& queue, NodePtr startingNode, Grid& grid);
    void AssignStudents();
    
    int getScore() const;

void placeStudent(NodePtr node)
{
  addVisited(node, STUDENT);
  ++score;
  for (vector<NodePtr>::const_iterator it = node->connections.begin();
        it != node->connections.end();
        ++it) 
        {
          NodePtr connectedNode = *it;
          VisitedMap::const_iterator visited_it = visitedMap.find(connectedNode);
          if (visited_it == visitedMap.end()) {
            addVisited(connectedNode, CHAIR);
          } else {
            assert(visited_it->second == CHAIR);            
          }
        }
}     
    
    friend ostream& operator<<(ostream& os, const BoardAssignment& rhs);
  };
  
  ostream& printMap(ostream& oos, const Grid& grid, const VisitedMap& visitedMap);

  
  ostream& operator<<(ostream& os, const BoardAssignment& rhs)
  {
    os << "BoardAssignment.  Visited: " << rhs.getScore() << endl; 
    printMap(os, rhs.grid, rhs.visitedMap);
    
    return os;
  }




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
  
  int fillEdge(int startRow, int endRow, int studentCol, int emptyCol) {
    LOG_OFF();
    int local_count = 0;
    LOG_STR("Filling edge");
    LOG(startRow);
    LOG(endRow);
    
    LOG(studentCol);
    LOG(emptyCol);
    LOG_OFF();
    
    assert(startRow <= endRow);
    assert(startRow >= 0);
    assert(startRow < rows);
    assert(endRow >= 0);
    assert(endRow < rows);
    
    assert(studentCol >= 0);
    assert(studentCol < cols);
    assert(emptyCol >= 0);
    assert(emptyCol < cols);
    
    for(int r = startRow; r <= endRow; ++r) {
        NodePtr studentNode = nodes[r][studentCol];
        
          
        if (studentNode) {
          studentNode->disconnectFromNeighbors();
          setStudent(r, studentCol);
          ++local_count;
          nodes[r][studentCol].reset();
        }
        
        setForcedEmpty(r, emptyCol);
        
        
        
    }
  
    LOG_OFF();    
    //LOG_ON();
    LOG(*this);
    validateGrid();
    
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
  
  bool isBlockSeparation(int row, int col1, int col2)
  {
    assert(col1 >= 0 && col1 < cols);
    assert(col2 >= 0 && col2 < cols);
    
    if(hasNoChair(row, col1) 
      && hasNoChair(row, col2)) 
    {
      return true;
    }
    
    if (row < rows) {
    if (hasNoChair(row, col1) == hasNoChair(row + 1, col1) &&
        hasNoChair(row, col2) == hasNoChair(row + 1, col2) &&
      hasNoChair(row, col1) != hasNoChair(row, col2)) 
      {
        return true;
      }
    }
    
    return false;
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
        
        if (empty_chair_col < 0 || empty_chair_col >= cols) {
          continue;
        }
      for(int r = 0; r < rows; ++r) {
        //r, c must have no connections towards c-col_offset
        
        
        
        //r, c must have f
        LOG_OFF();
        LOG(r);
        LOG(c);
        LOG(student_col);
        LOG(empty_chair_col);
        LOG(all_blocked_col);
        LOG(hasNoChair(r, c));
        LOG(hasNoChair(r, student_col));
        LOG(hasNoChair(r, empty_chair_col));
        LOG(potential_student_count);
        
        
        //has a chair
        if (!hasNoChair(r, student_col)) {
          ++potential_student_count;
        }
        
        //next door
        if (!hasNoChair(r, empty_chair_col)) {
          ++potential_empty_chair_count;
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
        
        if(isBlockSeparation(r, student_col, empty_chair_col)) {
          //LOG(r);
          //LOG(c);
          LOG_STR("seperator"); 
          if (!invalidUntilNextBlock && potential_student_count >= potential_empty_chair_count && potential_student_count > 0) {
            count += fillEdge(start_row, r, student_col, empty_chair_col);
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
        
        
        
        /*
        //has a chair
        if (!hasNoChair(r, student_col)) {
          ++potential_student_count;
        }
        
        //next door
        if (!hasNoChair(r, empty_chair_col)) {
          ++potential_empty_chair_count;
        }*/
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
        
        //LOG_ON();
        LOG_STR_INFO("Removing single connection");
        LOG_INFO(row_i);
        LOG_INFO(col_i);
        LOG_STR_INFO("and");
        LOG_INFO(remove_row);
        LOG_INFO(remove_col);
        //LOG_OFF();
        setStudent(row_i, col_i);
        ++local_count;
      }
     }
      
    return local_count;
    
  }
  
  void validateGrid() {
    for(int r = 0; r < rows; ++r) {
      for(int c = 0; c < cols; ++c) {
        if (cells[r][c] == BROKEN) {
          assert(!nodes[r][c]);
        } else
        if (cells[r][c] == FORCED_EMPTY) {
          if (nodes[r][c]) {
            LOG_ON();
            LOG(r);
            LOG(c);
            LOG(*this);
          }
          assert(!nodes[r][c]);
        } else
        if (cells[r][c] == STUDENT) {
          assert(!nodes[r][c]);
        } else
        if (cells[r][c] == CHAIR) {
          if (!nodes[r][c]) {
            cout << "what" << endl;
            LOG_ON();
            LOG(r);
            LOG(c);
            LOG(*this);
          }
          assert(nodes[r][c]);
          assert(nodes[r][c]->connections.size() == getCost(r, c));
        } else {
          assert(false);
        }
      }
    }
        
  }
  
  //returns size of larger set 
  int visitNodes()  {
     
     //find a node that has connections
     //int count = searchForEdges();
     int count = 0;
     int local_count = 0;
     
     //LOG_ON();
     int s_count = 0;
     validateGrid();
     
     do {
       s_count = count;
       
       
       do {
       
       local_count = searchForIsolated();

       LOG_OFF();
       LOG_STR("Isolated");
       LOG(*this);
       validateGrid();
       count += local_count;
       //break;
       } while (local_count > 0);
       
       local_count = searchForEdges();
       LOG_OFF();
       LOG_STR("Edges");
       LOG(*this);
       count += local_count;
       validateGrid();
       
       
       
       
     } while (s_count < count);
      
     //LOG_ON();
     LOG_STR("Done with initial searches");
     LOG(*this);
     LOG_OFF();
     
     local_count = 0;
     
     NodePtr startingNode;
     do 
     {
       startingNode.reset();
     
       for(int col = 0; col < cols; ++col) {
         for(int row = 0; row < rows; ++row) {
        
        
          if (!startingNode 
            && nodes[row][col] 
            && nodes[row][col]->connections.size() > 0) 
          {
            startingNode = nodes[row][col];
            //startingNode->label = 0;
            break;
          }
        
         }
         
         if (startingNode) {
           break;
         }
      }
      
      if (!startingNode) {
        continue;
      }
      
      LOG_OFF();
      LOG(*this);
      
      local_count = searchOptimalBackTracking(startingNode);
      
      LOG_OFF();
      LOG_STR("alternating");
      LOG(local_count);
      LOG(*this);
      count += local_count;
    
     } while(startingNode);
    
    return count; 
  }
  
  int searchOptimalBackTracking(NodePtr startingNode)
  {
    LOG_STR("Starting!");
    BoardAssignmentQueue queue;

    BoardAssignment::GenerateInitialBoardAssignments(queue, startingNode, *this);
    
    assert(queue.size() == 2);
    int max_score = 0;
    BoardAssignmentPtr max_ba;
    int processed_nodes = 0;
    
    while(!queue.empty()) 
    {
      BoardAssignmentPtr ba = queue.front();
      ++processed_nodes;
      if (processed_nodes % 500 == 0) {
        LOG_ON(); 
        LOG(processed_nodes);
        LOG_STR("Board assignment " << *ba);
        LOG_OFF();
      }
      
      queue.pop_front();
      LOG_OFF();
      LOG_STR("Board assignment " << *ba);
      LOG_STR("Queue size: " << queue.size());
      LOG_OFF();
      int score = ba->getScore();
      LOG_STR("Score: " << score);
      if (score > max_score) {
        max_ba = ba;
        max_score = score;
      }
      
      ba->GenerateOtherBoardAssignments(queue); 
    }
    
    assert(max_ba);
    
    max_ba->AssignStudents();
    LOG_ON();
    LOG_STR("Processed nodes: " << processed_nodes << " " << max_score);
    return max_score;
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



void BoardAssignment::GenerateInitialBoardAssignments(BoardAssignmentQueue& queue, NodePtr startingNode, Grid& grid)
{
  VisitedMap visitedMap;
  UnvisitedSet unvisitedSet;  
  {
  BoardAssignmentPtr new_ba(new BoardAssignment(grid, visitedMap, unvisitedSet, 0));
  new_ba->placeStudent(startingNode);
  queue.push_back(new_ba);
   
  }
  {
  BoardAssignmentPtr new_ba(new BoardAssignment(grid, visitedMap, unvisitedSet, 0));
  new_ba->addVisited(startingNode, CHAIR);
  queue.push_back(new_ba);
  }
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

ostream& printMap(ostream& oos, const Grid& grid, const VisitedMap& visitedMap)
{
 // return oos;
  ostringstream os;
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
        int sq = getSquareFromVisited(r, c, visitedMap);
        if (sq < 0) {
          os << '_';
        } else { 
          os << SquareCh[sq];
        }
      
      
      }
      os << " :" << r;
      
      os << endl;
    }
    
    os << endl << endl ;
    
    LOG_STR(os.str());
  return oos;    
} 
    
    void BoardAssignment::GenerateOtherBoardAssignments(BoardAssignmentQueue& queue) const
    {
      
      bool adjacent_to_student = false;
      bool adjacent_to_empty = false;
      
      if (unvisitedSet.empty()) {
        return;
      }
      
      UnvisitedSet::iterator it = unvisitedSet.begin();
      
      NodePtr node = *it;
      
      //LOG_ON();
      LOG_STR("Processing node: " << node);
      //printMap(cout, grid, visitedMap);
     
      //RE ADD
      //assert(visitedMap.find(node) == visitedMap.end());
        
      LOG_OFF();
      //LOG_ON();
      //printMap(cout, grid, visitedMap);
      LOG_STR("Processing node: " << node);
      
      for (vector<NodePtr>::const_iterator it = node->connections.begin();
        it != node->connections.end();
        ++it) 
      {
        NodePtr connectedNode = *it;
        LOG_STR(" Connected Node (" << (*it)->row << ", " << (*it)->col << ") " << endl);
        
        VisitedMap::const_iterator visited_it = visitedMap.find(connectedNode);
        if (visited_it == visitedMap.end()) {
          //LOG_STR("Adding unvisited node: " << connectedNode);
          //unvisited.push_back(connectedNode);
        } else {
          LOG_STR("Node already visited");
          int square = visited_it->second;
          assert(square == CHAIR || square == STUDENT);
          if (square == STUDENT) {
            adjacent_to_student = true;
          } else {
            adjacent_to_empty = true;
          }
        }
      }
      
      
      bool mustPutStudent = getSquareFromVisited(node->row - 1, node->col, visitedMap) == STUDENT
      || getSquareFromVisited(node->row + 1, node->col, visitedMap) == STUDENT;
           
      LOG_STR("Adding ba nodes");
      if (!mustPutStudent && adjacent_to_empty && node->connections.size() > 3) {
        LOG_STR("Adding chair + students");
        BoardAssignmentPtr new_ba(new BoardAssignment(grid, visitedMap, unvisitedSet, score));
        new_ba->addVisited(node, CHAIR);
        
        bool placedStudent = false;
        for (vector<NodePtr>::const_iterator it = node->connections.begin();
        it != node->connections.end();
        ++it) 
        {
          NodePtr connectedNode = *it;
          
          VisitedMap::const_iterator visited_it = visitedMap.find(connectedNode);
          if (visited_it == visitedMap.end()) {
            //newVisitedMap.insert(VisitedMap::value_type(connectedNode, STUDENT));
            new_ba->placeStudent(connectedNode); 
            placedStudent = true;
          }
        }
      
        if (placedStudent) {
        LOG_STR("Adding chair");
        //printMap(cout, grid, new_ba->visitedMap);
        queue.push_front(new_ba);
        }
      }
      
      if (!adjacent_to_student)  {
        LOG_STR("Adding student");
        
        BoardAssignmentPtr new_ba(
          new BoardAssignment(grid, visitedMap, unvisitedSet, score));
        
        new_ba->placeStudent(node);
        
        LOG_STR("Placeing student");
        //printMap(cout, grid, new_ba->visitedMap);
        
        queue.push_front(new_ba);  
        
      }
      
      
    }
    
    void BoardAssignment::AssignStudents() {
       for (VisitedMap::const_iterator it = visitedMap.begin();
         it != visitedMap.end();
         ++it) 
       {
         VisitedMap::value_type entry = *it;
         if (entry.second == STUDENT) {
            grid.setStudent(entry.first->row, entry.first->col);
         }
         
       }
    }
    
    int BoardAssignment::getScore() const
    {
      return score;
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
  LOG_ON();
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
  
  LOG_ON();
//  LOG(grid);
  LOG_OFF();
  
  grid.createNodeConnections();
   total_placed = grid.visitNodes();
  
  LOG_ON();
  LOG(grid);
  printf("Case #%d: %d\n", test_case+1, total_placed);
   
  return;    
}
  

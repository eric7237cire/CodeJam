//http://en.wikipedia.org/wiki/Modular_multiplicative_inverse
//Lucas's theorum
//
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
#include <boost/numeric/conversion/bounds.hpp>
#include <boost/limits.hpp>
#define SHOW_TIME 0
#include "util.h"
#include "grid.h"
#include <boost/math/common_factor.hpp>

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

typedef pair<int, int> HW;



ostream& operator<<(ostream& os, const HW& hw) {
  os << "H: " << hw.first << " W: " << hw.second;
  return os;
}

bool do_turn(int meIndex, int indexToAttack, Grid<int>& grid) {
  Grid<int> diffs(grid.rows, grid.cols);
  diffs.reset(0);
 //LOG_OFF();
  bool r = false;
  
  for(Grid<int>::iterator it = grid.begin(); it != grid.end(); ++it) 
  {
    //LOG_STR(grid.getIndex(it) << " is value: " << *it);
    
    if (grid.getIndex(it) == meIndex) {
      //LOG(meIndex);
      if (indexToAttack >= 0) {
        diffs[indexToAttack] -= grid[meIndex];
        //cout << diffs[indexToAttack]  << endl;
        //cout << endl;
        //cout << indexToAttack;
      }
      continue;
    }
    if(*it == 0) {
      //LOG_STR("It 0 skipping");
      continue;
    }
    
    vector<int> adjSquares = grid.getAdjacentSquaresIndexes(grid.getIndex(it));
    
    //Grid<int>::iterator strongestNeighbor = grid.end();
    int strongestNeighbor = -1;
    
    for(vector<int>::const_iterator adj_it = adjSquares.begin();
      adj_it != adjSquares.end();
      ++adj_it) 
    {
      
      //cout << "A" << endl;
      if (grid[*adj_it] == 0) { //**adj_it == 0) {
        continue;
      }
      
      if (strongestNeighbor == -1 ||
          grid[*adj_it] > grid[strongestNeighbor] ) 
      {
        r = true;
        //cout << **adj_it << endl;
        strongestNeighbor = *adj_it;    
      }
      //LOG(*strongestNeighbor);
    }
    
    if (strongestNeighbor >= 0) {
      diffs[strongestNeighbor] -= *it;
      //LOG_STR("Diffs " << snIndex << " " << diffs[snIndex]);
    }
    
  }
  
  //cout << "Diffs\n" << diffs;
  
  Grid<int>::iterator it = grid.begin();
  Grid<int>::iterator diffs_it = diffs.begin();
  for( ;it != grid.end(); ++it, ++diffs_it) 
  {
    int idx = grid.getIndex(it);
    grid[idx] = max(0, grid[idx] + diffs[idx]);
    //(*it) = max(*it + *diffs_it, 0);
  }
  
  return r;
}

typedef queue<pair<int, Grid<int> > > Queue;

void do_test_case(int test_case, ifstream& input)
{
 //LOG_OFF();
 if (test_case == 25) {
   LOG_ON();
 } else {
   //LOG_OFF();
 }
  int C, R, c, r;
  input >> C >> R >> c >> r;
  
  Grid<int> g(R, C);
  
  for(int rIdx = 0; rIdx < R; ++rIdx) {
    for(int cIdx = 0; cIdx < C; ++cIdx) {
      int s;
      input >> s;
      g.set(rIdx, cIdx, s);
    }
  }
    
  cout << "Case #" << (test_case+1) << ": ";
    
  //cout << g << endl;
  
  int t = 0;
  Queue q;
  q.push(Queue::value_type(0, g));
  LOG(g);
  int max_t = 0;
  
  const int meIndex = g.getIndex(r-1, c-1);
  
  while(!q.empty()) {
    const Queue::value_type& item = q.front();
    
    const Grid<int> itemGrid = item.second;
    q.pop();
        
    if(itemGrid[meIndex] <= 0) {
      continue;
    }
    
    LOG_STR("Grid off stack: " << itemGrid << " Turns: " << item.first);
    max_t = max(max_t, item.first);
    
    vector<int> adjSquares = 
      itemGrid.getAdjacentSquaresIndexes(meIndex);
    
    for(vector<int>::const_iterator adj_it = adjSquares.begin();
      adj_it != adjSquares.end();
      ++adj_it) 
    { 
      if (itemGrid[*adj_it] == 0) { 
        continue;
      }
      Grid<int> newGrid(itemGrid);
      LOG_STR("Grid before: " << newGrid 
        << " turns: " << item.first
        << " Attacking: " << *adj_it);
      LOG(meIndex);
      bool r = do_turn(meIndex, 
        *adj_it, newGrid);
     //LOG_OFF();
      LOG_STR("Grid after: " << newGrid);
      q.push(Queue::value_type(item.first+1, newGrid));
      if (!r) {
        cout << "forever" << endl;
        return;
      }
    }
    //LOG(itemGrid);
    Grid<int> newGrid(itemGrid);
    LOG_STR("Before doing nothing: " << newGrid);
    bool r2 = do_turn(meIndex, -1, newGrid);
    q.push(Queue::value_type(item.first+1, newGrid));
   //LOG_OFF();
    LOG_STR("After doing nothing: " << newGrid);
    LOG(item.first+1);
    //return;
    if (!r2) {
      cout << "forever"  << endl;
      return;
    }
    
    
  }
  
  cout << max_t << " day(s)" << endl;
  
  return;    
}


  

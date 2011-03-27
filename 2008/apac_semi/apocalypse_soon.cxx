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

typedef pair<int, int> HW;



ostream& operator<<(ostream& os, const HW& hw) {
  os << "H: " << hw.first << " W: " << hw.second;
  return os;
}

bool do_turn(int meIndex, Grid<int>& grid) {
  Grid<int> diffs(grid.rows, grid.cols);
  diffs.reset(0);
  LOG_OFF();
  bool r = false;
  
  for(Grid<int>::iterator it = grid.begin(); it != grid.end(); ++it) 
  {
    LOG_STR(grid.getIndex(it) << " is value: " << *it);
    
    if (grid.getIndex(it) == meIndex) {
      //LOG(meIndex);
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

void do_test_case(int test_case, ifstream& input)
{
  LOG_OFF();
  LOG_ON();
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
    
  cout << "Case #" << (test_case+1) << ":" << endl;
    
  cout << g << endl;
  
  int t = 0;
  while(do_turn(g.getIndex(r-1, c-1), g) && t < 14) {
    ++t;
    cout << g << endl;
  }
  
  return;    
}


  

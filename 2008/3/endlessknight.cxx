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

typedef pair<unsigned int, unsigned int> RowCol;
typedef unsigned int uint;

RowCol getRowCol(const unsigned int level, const unsigned int index)
{
  return RowCol(2 * level - index, level + index);
}

bool getLevelIndex(uint& level, uint& index, const RowCol& rc)
{
  level = (rc.row + rc.col) / 3;
  index = rc.col - rc.level;
  
  if (getRowCol(level, index) != rc) {
    return false;
  } else {
    return true;
  }
}

void do_test_case(int test_case, ifstream& input)
{
  
  LOG_OFF();
  unsigned int H, W, R;
  input >> H >> W >> R;
  
  vector<RowCol> rocks;
  
  for(int i = 0; i < R; ++i) {
    unsigned int r, c;
    input >> r >> c;
    rocks.push_back(RowCol(r, c));
  }
  
  
  printf("Case #%d: %d\n", test_case+1, total_placed);
   
  return;    
}
  

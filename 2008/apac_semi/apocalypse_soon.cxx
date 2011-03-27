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
    
  
  
  return;    
}


  

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
//#include "grid.h"
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

typedef vector<double> LimitRow;
typedef vector<LimitRow> Limits;
typedef vector<double> ProbRow;
typedef vector<ProbRow> Prob;

void do_test_case(int test_case, ifstream& input)
{
  int M;
  double P;
  int X;
  input >> M >> P >> X;
  //const int MAX_M = 5;
  LOG(M);
  
  const int GOAL = 1000000;
  Limits limits;
  Prob prob;
  for(int m=1; m<=5; ++m) {
    LimitRow row;
    row.push_back(GOAL);
    for(int i = 1; i < m; ++i) {
      row.push_back(row[i-1] / 2);
    }
    cout << row << endl;
    ProbRow pRow(row.size(), 0);
    limits.push_back(row);
    prob.push_back(pRow);
  }
  
  
  
  for(int m=M-1; m>=0; --m) {
    LOG(m);
     cout << prob[m] << endl;
  }
  
  cout << " day(s)" << endl;
  
  return;    
}


  

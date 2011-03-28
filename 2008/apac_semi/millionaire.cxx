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
#include <iomanip>
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

double round( double x )
{
const double sd = 100000; //for accuracy to 3 decimal places
return int(x*sd + (x<0? -0.5 : 0.5))/sd;
}

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
  const int MAX_M = 15;
  LOG(M);
  
  const int GOAL = 1000000;
  Limits limits;
  Prob prob;
  for(int m=1; m<=MAX_M; ++m) {
    LimitRow row;
    row.push_back(GOAL);
    for(int i = 1; i < m; ++i) {
      row.push_back(row[i-1] / 2);
    }
    LOG(row);
    ProbRow pRow(row.size(), 0);
    limits.push_back(row);
    prob.push_back(pRow);
  }
  
  prob[0][0] = 1;
  
  //0:  1
  //1:  1 0,75 
  //2:  1 0,9375 0,5625
  
  for(int m=1; m<MAX_M; ++m) {
    prob[m][0] = 1;
    for(int i = 1; i < m; ++i) {
      prob[m][i] = P * prob[m-1][i-1]
         + (1-P) * prob[m-1][i];
    }
    prob[m][m] = P * prob[m-1][m-1];
  }
  
  double max_p = 0;
  for(int i=0; i < M; ++i) {
    //winning
    double p = 0;
    double bet_possible = X - limits[M-1][i];
    LOG(X);
    LOG(bet_possible);
    if (bet_possible < 0) {
      continue;
    }
    if (bet_possible == 0) {
      p = prob[M-1][i];
      LOG(p);
      max_p = max(p, max_p);
      continue;
    }
    
    p = prob[M-1][i] * P;
    
    //remaining
    double remaining = X - bet_possible;
    LOG(remaining);
    LOG(p);
    for(int ri = i + 1; ri < M; ++ri) {
      if (remaining >= limits[M-1][ri]) {
        p += prob[M-1][ri] * (1-P);
        break;
      }
    }
    LOG(p);
    max_p = max(p, max_p);
    LOG(max_p);
  }
  
  for(int i=0; i < M; ++i) {
    //winning
    double p = 0;
    double bet_needed = limits[M-1][i] - X;
    LOG(X);
    LOG(bet_needed);
    if (bet_needed > X) {
      continue;
    }
    if (bet_needed <= 0) {
      if (M==1) {
        LOG_STR("Win automatic");
        p = 1;
        max_p = max(p, max_p);
      } else if (i < M - 1) {
        LOG_STR("Not betting");
        p = prob[M-2][i];
        max_p = max(p, max_p);
      }
      
      LOG_STR("Need nothing " << p);
      
      continue;
    }
        
    p = prob[M-1][i] * P;
    
    //remaining
    double remaining = X - bet_needed;
    LOG(remaining);
    LOG(p);
    for(int ri = i + 1; ri < M; ++ri) {
      if (remaining >= limits[M-1][ri]) {
        p += prob[M-1][ri] * (1-P);
        break;
      }
    }
    LOG(p);
    max_p = max(p, max_p);
    LOG(max_p);
  }
  
  LOG(max_p);
  
  for(int m=M-1; m>=0; --m) {
    LOG(m);
    //cout << prob[m] << endl;
  }
  
  cout << "Case #" << (test_case+1) << ": " 
  << std::setprecision(7)
  //<< setiosflags(ios::fixed)
    << round(max_p) << endl;
  
  return;    
}


  

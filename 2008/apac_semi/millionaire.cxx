//
#include <fstream>
#include <iostream>
#include <vector>
#include <set>
#include <map>
//#include <deque>
//#include <queue>
#include <sstream>
#include <time.h>
#include <assert.h>
#include <iomanip>

#define SHOW_TIME 0
#include "util.h"


using namespace std;


void do_test_case(int test_case, istream& input);

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

const int MAX_ROUNDS = 15;
// [rounds left] [ steps ]
double roundProb[MAX_ROUNDS+1][ (1 << 15) + 1];

void do_test_case(int test_case, istream& input)
{
  int M;
  double P;
  int X;
  input >> M >> P >> X;
  
  
  memset(roundProb,0,sizeof(roundProb));

  roundProb[0][1] = 1;
  roundProb[0][0] = 0;

  for(int m = 1; m <= M; ++m) 
  {
	  const unsigned int lastRoundMax = (1 << m-1) + 1;
	  //Combine rounds
	  for(unsigned int highIndex = 0; highIndex < lastRoundMax; ++highIndex)	  
	  {
		  //Copy over rounds
		  roundProb[m][highIndex * 2] = max(roundProb[m][highIndex * 2], roundProb[m-1][highIndex]);

		  for(unsigned int lowIndex = 0; lowIndex < highIndex; ++lowIndex)
		  {
			  unsigned int thisRoundIdx = ( 2 * highIndex + 2 * lowIndex ) / 2;
			  roundProb[m][thisRoundIdx] = max(roundProb[m][thisRoundIdx], P * roundProb[m-1][highIndex] + (1-P) * roundProb[m-1][lowIndex]);
		  }
	  }	  
  }

  const int GOAL = 1000000;

  double stepSize = (double) GOAL / (1 << M);

  int steps = (double) X / stepSize;

  
  cout << "Case #" << (test_case+1) << ": " 
  << std::setprecision(6)
    << roundProb[M][steps] << endl;
  
  return;    
}


  

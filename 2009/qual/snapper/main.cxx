#include <fstream>
#include <iostream>
#include <iomanip>
#include <vector>
#include <algorithm>
#include <limits>

#include <stdio.h>
#include <time.h>
#include <assert.h>
#include <stdlib.h>

using namespace std;

double diffclock(clock_t clock1,clock_t clock2)
{
	double diffticks=clock1-clock2;
	double diffms=(diffticks*1000)/CLOCKS_PER_SEC;
	return diffms;
} 

void do_test_case(int test_case, ifstream& input);

#define SHOW_TIME 0
#define DEBUG_OUTPUT 0
//#undef assert
//#define assert(x) ((void)0)

#if SHOW_TIME
#define SHOW_TIME_BEGIN(A) clock_t begin_##A=clock();
#define SHOW_TIME_END(A) clock_t end_##A=clock(); cout << "Time elapsed: " #A << " " << double(diffclock(end_##A,begin_##A)) << " ms"<< endl;
#else
#define SHOW_TIME_BEGIN(A) 
#define SHOW_TIME_END(A) 
#endif

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
    do_test_case(test_case, input);  
  }
  
  SHOW_TIME_END(g)
}


void do_test_case(int test_case, ifstream& input)
{
  int N, K;
  input >> N >> K;
    
  vector<bool> snapper_state;
  snapper_state.assign(N, false);
  
  for(int i=0; i < K; ++i) {
      int powered_to = 0;
      
      for(int n=0; n<N; ++n) {
         // cout << setw(3) << n ;   
      }
     // cout << endl;
      
      for(int n=0; n<N; ++n) {
         // cout << setw(3) << snapper_state[n];   
      }
     // cout << endl;
     // cout << endl;
      
      for(int n=1; n<N; ++n) {
          if (!snapper_state[n-1]) {
              break;
          }
          powered_to = n;
        //
      }
      
      //snapper_state[0] = !snapper_state[0];
      
      for(int n=0; n<=powered_to; ++n) {
          snapper_state[n] = !snapper_state[n];
      }      
      
      
  }
  
  bool isOn = true;
  for(int n=0; n<N; ++n) {
    isOn = snapper_state[n] && isOn;
  } 
      
cout << "Case #" << (1+test_case) << ": " << ( isOn ? "ON" : "OFF" ) << endl;  
  return;
    
}
  

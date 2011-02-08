#include <fstream>
#include <iostream>
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
  int N, M, A;
  input >> N >> M >> A;
    
  for (int x1=0; x1<=N; ++x1)
  {
    for(int y1=0; y1<=M; ++y1)
    {
      for (int x2=0; x2<=N; ++x2)
      {
        for(int y2=0; y2<=M; ++y2)
        {
          if (A == abs( x1*y2 - x2 * y1 ) )
          {
            //printf("Case #%d: Xmax %d Ymax %d tar %d\n", test_case+1, N, M, A);
            printf("Case #%d: 0 0 %d %d %d %d\n", test_case+1, x1,y1,x2,y2);
            return;
            //throw 3;
          }
          
        }
      }
    }
  }
  
  /*
  for (int x1=0; x1<=N; ++x1)
  {
    for(int y1=0; y1<=M; ++y1)
    {
      for (int x2=0; x2<=N; ++x2)
      {
        for(int y2=0; y2<=M; ++y2)
        {
          for (int x3=0; x3<=N; ++x3)
          {
            for(int y3=0; y3<=M; ++y3)
            {
              if (A == abs( (x1-x3)*(y2-y1) - (x1 - x2)*(y3 - y1) ) )
              {
                printf("Case #%d: Xmax %d Ymax %d tar %d\n", test_case+1, N, M, A);
                printf("Case #%d: %d %d %d %d %d %d\n", test_case+1, x1,y1,x2,y2,x3,y3);
                return;
                //throw 3;
              }
            }
          }
        }
      }
    }
  }*/
  
  printf("Case #%d: IMPOSSIBLE\n", test_case+1);
    
}
  

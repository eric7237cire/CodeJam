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
    
  int narrow = min<int>(N, M);
  int wide = max<int>(N, M);
  
  if (wide * narrow < A) {
    printf("Case #%d: IMPOSSIBLE\n", test_case+1);
    return;
  }
  
  int n1 = A / wide;
  int w2 = wide;
  int w1, n2;

  if (A%wide == 0) {
    w1 = 0;
    n2 = 0;
  } else {
    ++n1;
    n2 = 1;
    w1 = n1*w2 - A;
    
  }
  
  //          printf("Case #%d: Xmax %d Ymax %d tar %d\n", test_case+1, N, M, A);
  //          printf("Case #%d: 0 0 %d %d %d %d\n", test_case+1, x1,y1,x2,y2);
  if (wide == M) {
    printf("Case #%d: 0 0 %d %d %d %d\n", test_case+1, n1, w1, n2, w2);
    assert(w1 >= 0);
    assert(w1 <= M);
    assert(w2 >= 0);
    assert(w2 <= M);
    assert(n1 >= 0);
    assert(n1 <= N);
    assert(n2 >= 0);
    assert(n2 <= N);
    
  } else {
    assert(w1 >= 0);
    assert(w1 <= N);
    assert(w2 >= 0);
    assert(w2 <= N);
    assert(n1 >= 0);
    assert(n1 <= M);
    assert(n2 >= 0);
    assert(n2 <= M);
    printf("Case #%d: 0 0 %d %d %d %d\n", test_case+1, w1, n1, w2, n2);
  }
          //cout << abs(x1 * y2 - x2 * y1) << endl;
            
  assert(A == abs(n1 * w2 - n2 * w1) );
    
  return;
    
}
  
